import request from './request'

// 分页查询商品
// 后端接收 @RequestBody ProductPageDTO { pageNum, pageSize, categoryId, productName }
export function pageProducts(data) {
  return request.post('/admin/product/page', data)
}

// 根据商品 id 查询详情
// 后端接收 @RequestParam id，所以用 query 参数传递
export function getProductDetail(id) {
  return request.post('/admin/product/getDetailById', null, {
    params: { id }
  })
}