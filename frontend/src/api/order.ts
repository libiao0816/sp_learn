import request from './request'
import type { ApiResult } from './request'

// 订单明细条目
export interface OrderItemVO {
  productName: string
  productPrice: number
  productQuantity: number
  subtotal: number
}

// 订单分页列表项（已联表带好 orderItems）
export interface OrderPageVO {
  id: number
  orderNo: string
  userId: number
  totalAmount: number
  status: number
  remark?: string
  createTime?: string
  updateTime?: string
  orderItems?: OrderItemVO[]
}

// 订单分页请求体（后端 OrderPageDTO）
// orderId / userId / status 是精确匹配，传 null 后端忽略
export interface OrderPageDTO {
  pageNum: number
  pageSize: number
  /** 状态筛选: 0待支付 1已支付 2已发货 3已完成 4已取消 */
  status?: number | null
  /** 用户ID筛选 */
  userId?: number | null
  /** 订单ID筛选 */
  orderId?: number | null
  /** 1=按创建时间倒序 */
  createTimeOrderBy?: number | null
}

// 订单分页查询
export function pageOrders(data: OrderPageDTO): Promise<ApiResult<{ list: OrderPageVO[]; total: number }>> {
  return request.post('/admin/order/page', data)
}

// 删除订单：后端 POST /admin/order/deleteById?id=xxx（@RequestParam Long id）
export function deleteOrder(id: number): Promise<ApiResult<null>> {
  return request.post('/admin/order/deleteById', null, {
    params: { id }
  })
}
