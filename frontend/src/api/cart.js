import request from './request'

// 购物车分页
// 后端接收 CartPageDTO（无 @RequestBody），所以用 query 参数传递
export function pageCart(params) {
  return request.post('/admin/cart/page', null, {
    params
  })
}

// 添加购物车，后端接收 @RequestBody CartAddDTO { productId, quantity }
export function addCart(data) {
  return request.post('/admin/cart/add', data)
}

// 删除购物车，后端接收 @RequestBody List<Long> ids
export function removeCart(ids) {
  return request.post('/admin/cart/remove', ids)
}