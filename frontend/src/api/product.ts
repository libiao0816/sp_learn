import request from './request'
import type { ApiResult } from './request'

// 商品信息
export interface Product {
  id: number
  name: string
  categoryId: number
  price: number
  stock: number
  status: number
  description?: string
  createdTime?: string
}

// 商品分页请求体（后端 ProductPageDTO）
export interface ProductPageDTO {
  pageNum: number
  pageSize: number
  categoryId?: number | null
  productName?: string | null
}

// 分页查询商品
export function pageProducts(data: ProductPageDTO): Promise<ApiResult<{ list: Product[]; total: number }>> {
  return request.post('/admin/product/page', data)
}

// 根据商品 id 查询详情：后端接收 @RequestParam id，用 query 参数传递
export function getProductDetail(id: number): Promise<ApiResult<Product>> {
  return request.post('/admin/product/getDetailById', null, {
    params: { id }
  })
}
