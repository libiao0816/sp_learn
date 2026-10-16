import request from './request'
import type { ApiResult } from './request'

// 购物车条目
export interface CartItemVO {
  id: number
  productId: number
  productName?: string
  price?: number
  quantity: number
}

// 购物车分页请求体（后端 CartPageDTO，无 @RequestBody，用 query 参数传递）
export interface CartPageDTO {
  pageNum: number
  pageSize: number
}

// 添加购物车请求体（后端 CartAddDTO）
export interface CartAddDTO {
  productId: number
  quantity: number
}

// 购物车分页
export function pageCart(params: CartPageDTO): Promise<ApiResult<{ list: CartItemVO[]; total: number }>> {
  return request.post('/admin/cart/page', null, {
    params
  })
}

// 添加购物车
export function addCart(data: CartAddDTO): Promise<ApiResult<null>> {
  return request.post('/admin/cart/add', data)
}

// 删除购物车，后端接收 @RequestBody List<Long> ids
export function removeCart(ids: number[]): Promise<ApiResult<null>> {
  return request.post('/admin/cart/remove', ids)
}
