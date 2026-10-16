import request from './request'
import type { ApiResult } from './request'

// 商品分类
export interface Category {
  id: number
  name: string
}

// 获取所有分类
export function listCategories(): Promise<ApiResult<Category[]>> {
  return request.get('/admin/category/list')
}
