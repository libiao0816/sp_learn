import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/product' },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录', standalone: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { title: '注册', standalone: true }
  },
  {
    path: '/product',
    name: 'ProductList',
    component: () => import('../views/ProductList.vue'),
    meta: { title: '商品列表' }
  },
  {
    path: '/cart',
    name: 'CartList',
    component: () => import('../views/CartList.vue'),
    meta: { title: '购物车' }
  },
  {
    path: '/order',
    name: 'OrderList',
    component: () => import('../views/OrderList.vue'),
    meta: { title: '订单列表' }
  },
  {
    path: '/user',
    name: 'UserProfile',
    component: () => import('../views/UserProfile.vue'),
    meta: { title: '个人中心' }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router