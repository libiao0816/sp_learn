import request from './request'
import type { ApiResult } from './request'

// C 端用户信息（对应后端 User PO）
export interface User {
  id: number
  userNo: string
  status: number
  registerSource: string
  nickname?: string
  avatar?: string
  gender?: number
  lastLoginTime?: string
  createTime?: string
  updateTime?: string
}

// C 端用户分页请求参数
export interface UserPageDTO {
  pageNum: number
  pageSize: number
}

// 分页查询 C 端用户（GET /admin/sysUser/page）
export function pageUsers(params: UserPageDTO): Promise<ApiResult<{ list: User[]; total: number }>> {
  return request.get('/admin/sysUser/page', { params })
}
