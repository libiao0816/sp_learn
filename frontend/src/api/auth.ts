import axios from 'axios'
import request from './request'
import type { ApiResult } from './request'

// 登录请求体
export interface LoginDTO {
  username: string
  password: string
}

// 登录成功返回的双 token
export interface LoginResult {
  accessToken: string
  refreshToken: string
}

// 登录：axios 会以 JSON 请求体发送
export function login(data: LoginDTO): Promise<ApiResult<LoginResult>> {
  return request.post('/admin/auth/login', data)
}

// 注册：axios 会以 JSON 请求体发送
export function register(data: LoginDTO): Promise<ApiResult<null>> {
  return request.post('/admin/auth/register', data)
}

// 退出登录：后端会把当前 accessToken 加入黑名单使其失效
// accessToken 由 request 拦截器统一放入 Authorization 头，无需额外传参
export function logout(): Promise<ApiResult<null>> {
  return request.post('/admin/auth/logout')
}

// 刷新 token：用 refreshToken 换新的 accessToken
// ⚠️ 必须用独立 axios 实例，不能走通用 request 拦截器
// 原因：如果 accessToken 已过期，通用拦截器的 4011 处理会再次触发 refresh 调用，形成死循环
// 另外后端 /auth/refresh 在白名单里，不需要 Authorization 头，请求体传 { refreshToken } 即可
const refreshClient = axios.create({
  baseURL: '/api',
  timeout: 10000
})

export function refreshTokenApi(refreshToken: string): Promise<ApiResult<string>> {
  // ⚠️ refreshClient 没有响应拦截器，拿到的是 Axios 完整响应对象（含 status/headers/config），
  // 这里统一解包出后端 { code, msg, data } 业务体，保持与 request 实例的返回结构一致
  return refreshClient.post('/admin/auth/refresh', { refreshToken }).then((res) => res.data)
}
