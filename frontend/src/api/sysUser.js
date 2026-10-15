import request from './request'

// 管理端用户详情：后端从 Authorization 头解析当前登录用户，无需传参
export function getSysUserDetail() {
  return request.post('/sysUser/detail')
}
