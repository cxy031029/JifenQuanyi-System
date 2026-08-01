import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/home',
    children: [
      { path: 'home', component: () => import('@/views/Home.vue'), meta: { title: '首页' } },
      { path: 'task', component: () => import('@/views/Task.vue'), meta: { title: '任务' } },
      { path: 'product', component: () => import('@/views/Product.vue'), meta: { title: '积分兑换' } },
      { path: 'seckill', component: () => import('@/views/Seckill.vue'), meta: { title: '限时抢购' } },
      { path: 'bill', component: () => import('@/views/Bill.vue'), meta: { title: '账单' } },
      { path: 'mine', component: () => import('@/views/Mine.vue'), meta: { title: '我的' } }
    ]
  },
  {
    path: '/admin/login',
    component: () => import('@/views/admin/AdminLogin.vue')
  },
  {
    path: '/admin',
    component: () => import('@/views/admin/AdminLayout.vue'),
    redirect: '/admin/dashboard',
    children: [
      { path: 'dashboard', component: () => import('@/views/admin/Dashboard.vue'), meta: { title: '数据概览' } },
      { path: 'users', component: () => import('@/views/admin/UserManage.vue'), meta: { title: '用户管理' } },
      { path: 'flows', component: () => import('@/views/admin/FlowManage.vue'), meta: { title: '积分流水' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  if (to.path.startsWith('/admin')) {
    if (to.path !== '/admin/login' && !localStorage.getItem('admin_token')) return '/admin/login'
    if (to.path === '/admin/login' && localStorage.getItem('admin_token')) return '/admin/dashboard'
    return true
  }
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) return '/login'
  if (to.path === '/login' && token) return '/home'
})

export default router
