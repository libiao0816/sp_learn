import axios, { type AxiosError, type AxiosRequestConfig, type InternalAxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import { refreshTokenApi } from './auth'

// 后端统一响应体 { code, msg, data }
export interface ApiResult<T = any> {
  code: number
  msg: string
  data: T
}

// 封装类型：向调用方说明响应拦截器已把 AxiosResponse 解包成 ApiResult
interface HttpClient {
  <T = any>(config: AxiosRequestConfig): Promise<ApiResult<T>>
  <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResult<T>>
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<ApiResult<T>>
  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResult<T>>
}

// 统一 axios 实例
// baseURL 为 /api，由 vite.config.ts 的 proxy 转发到后端 8080 端口
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// ========== 双 token 存储 key ==========
const ACCESS_TOKEN_KEY = 'accessToken'
const REFRESH_TOKEN_KEY = 'refreshToken'

// ========== 4011（accessToken 过期）自动刷新机制 ==========
let isRefreshing = false           // 是否正在刷新（锁）
let failedQueue: Array<{
  resolve: (newAccessToken: string) => void
  reject: (error: unknown) => void
}> = []                            // 刷新期间积压的请求队列，拿到新 accessToken 后重发

// 将积压的请求重放或拒绝
function processQueue(error: unknown, newAccessToken?: string | null) {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error)
    } else {
      prom.resolve(newAccessToken as string)
    }
  })
  failedQueue = []
}

// 认证彻底失效：清除双 token 并跳转登录页
function handleAuthFailed(msg?: string) {
  localStorage.removeItem(ACCESS_TOKEN_KEY)
  localStorage.removeItem(REFRESH_TOKEN_KEY)
  ElMessage.error(msg || '登录已失效，请重新登录')
  // 已在登录页就不再重复跳转
  if (router.currentRoute.value.path !== '/login') {
    router.push('/login')
  }
}

// 请求拦截器
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 统一注入 accessToken 到 Authorization 头
    const token = localStorage.getItem(ACCESS_TOKEN_KEY)
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error: AxiosError) => Promise.reject(error)
)

// 响应拦截器
request.interceptors.response.use(
  // 返回类型标注 any：拦截器已解包出 ApiResult，不再是 AxiosResponse
  (response): any => {
    const res = response.data as ApiResult
    // 后端统一返回 { code, msg, data }，code 为 200 表示成功
    if (res && res.code !== undefined) {
      if (res.code === 200) {
        return res
      }
      // 4011：accessToken 过期（后端已明确告知）→ 用 refreshToken 换新 token 并重发原请求
      if (res.code === 4011) {
        return handle401(response.config)
      }
      // 401：未登录 / token 被篡改或已注销 → 刷新也救不了，直接跳登录页
      if (res.code === 401) {
        handleAuthFailed(res.msg)
        return Promise.reject(new Error(res.msg || '认证失败'))
      }
      ElMessage.error(res.msg || '请求失败')
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    return res
  },
  (error: AxiosError) => {
    // 后端以 HTTP 401 返回（非业务 code 4011）：说明不是"过期"场景，直接跳登录页
    if (error.response && error.response.status === 401) {
      handleAuthFailed()
      return Promise.reject(error)
    }
    ElMessage.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

/**
 * accessToken 过期（后端返回 code 4011）处理：用 refreshToken 换新 accessToken，成功则重发原请求
 *
 * 流程：
 *  1) 没 refreshToken → 直接跳登录页（异常兜底，正常过期场景本地一定有 refreshToken）
 *  2) 正在刷新（isRefreshing=true）→ 把当前请求放入队列等刷新完再重发
 *  3) 没有正在刷新 → 发起刷新请求，期间其他 4011 请求进队列
 *     3a) 刷新成功 → 存新 accessToken，重放队列里所有请求，再重发当前请求
 *     3b) 刷新失败（refreshToken 也过期了）→ 清双 token，跳登录页
 */
async function handle401(originalConfig: InternalAxiosRequestConfig): Promise<any> {
  const refreshToken = localStorage.getItem(REFRESH_TOKEN_KEY)
  if (!refreshToken) {
    // 连 refreshToken 都没有，说明根本没登录过或已彻底失效
    handleAuthFailed()
    throw new Error('未登录')
  }

  if (isRefreshing) {
    // 已有请求正在刷新：排队等待，刷新成功后用新 token 重发本次请求
    const newAccessToken = await new Promise<string>((resolve, reject) => {
      failedQueue.push({ resolve, reject })
    })
    originalConfig.headers['Authorization'] = `Bearer ${newAccessToken}`
    return request(originalConfig)
  }

  // 标记正在刷新，期间其他 4011 请求会进入 failedQueue 排队
  isRefreshing = true
  let newAccessToken: string
  try {
    // refreshTokenApi 已解包出业务体：{ code, msg, data }
    const refreshRes = await refreshTokenApi(refreshToken)
    if (!(refreshRes && refreshRes.code === 200 && refreshRes.data)) {
      throw new Error(refreshRes?.msg || '刷新 token 失败')
    }
    newAccessToken = refreshRes.data
    localStorage.setItem(ACCESS_TOKEN_KEY, newAccessToken)
    // 刷新成功：重放队列里积压的请求
    processQueue(null, newAccessToken)
  } catch (err) {
    // ⚠️ 只有"刷新 token 这一步"失败才算认证彻底失效 → 清 token 并跳登录页
    processQueue(err, null)
    handleAuthFailed('登录已失效，请重新登录')
    throw err
  } finally {
    isRefreshing = false
  }

  // 刷新成功：用新 accessToken 重发触发 4011 的原请求
  // 注意这一步在 try 之外，它自身的失败（网络抖动/业务错误）不会再误触发"清 token 跳登录页"
  originalConfig.headers['Authorization'] = `Bearer ${newAccessToken}`
  return request(originalConfig)
}

export default request as HttpClient
