import request from './request'

// 用户详情：后端从 Authorization 头解析当前登录用户，无需传参
export function getUserDetail() {
  return request.post('/user/detail')
}