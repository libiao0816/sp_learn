import request from './request'
import type { ApiResult } from './request'

// 管理端用户信息
export interface SysUserVO {
  id: number
  username: string
  nickname?: string
  email?: string
  phone?: string
  createTime?: string
}

// 管理端用户详情：后端从 Authorization 头解析当前登录用户，无需传参
export function getSysUserDetail(): Promise<ApiResult<SysUserVO>> {
  return request.post('/admin/sysUser/detail')
}
