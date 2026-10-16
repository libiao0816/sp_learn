import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  { path: '/', redirect: '/product' },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录', standalone: true }
  },
  {
    path: '/product',
    name: 'ProductList',
    component: () => import('../views/ProductList.vue'),
    meta: { title: '商品管理' }
  },
  {
    path: '/order',
    name: 'OrderList',
    component: () => import('../views/OrderList.vue'),
    meta: { title: '订单管理' }
  },
  {
    path: '/user',
    name: 'UserList',
    component: () => import('../views/UserList.vue'),
    meta: { title: '用户管理' }
  },
  {
    path: '/sysUser',
    name: 'SysUserProfile',
    component: () => import('../views/SysUserProfile.vue'),
    meta: { title: '个人中心' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
