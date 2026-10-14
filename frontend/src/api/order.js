import request from './request'

/**
 * 订单分页查询
 * 后端：POST /order/page，body: OrderPageDTO
 * 返回的每条 OrderPageVO 已经联表带好 orderItems
 *
 * @param {Object} data
 * @param {number} data.pageNum
 * @param {number} data.pageSize
 * @param {number} [data.status]            状态筛选: 0待支付 1已支付 2已发货 3已完成 4已取消
 * @param {number} [data.userId]           用户ID筛选
 * @param {number} [data.orderId]          订单ID筛选
 * @param {number} [data.createTimeOrderBy] 1=按创建时间倒序
 */
export function pageOrders(data) {
  return request.post('/order/page', data)
}

/**
 * 删除订单
 * 后端：POST /order/deleteById?id=xxx（@RequestParam Long id）
 * @param {number} id 订单ID
 */
export function deleteOrder(id) {
  return request.post('/order/deleteById', null, {
    params: { id }
  })
}
