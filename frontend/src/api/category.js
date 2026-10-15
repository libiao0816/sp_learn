import request from './request'

// 获取所有分类
export function listCategories() {
  return request.get('/admin/category/list')
}
