import { createRouter, createWebHistory } from 'vue-router'

/**
 * 路由配置文件
 * 定义了系统内的所有页面路径及其对应的组件
 */
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginView.vue'), // 路由懒加载
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/RegisterView.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/',
    component: () => import('../layout/MainLayout.vue'), // 使用布局组件作为容器
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('../views/StudentManage.vue'),
        meta: { title: '学生管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 全局前置守卫：登录拦截
 * 在每次路由跳转前执行，判断用户是否已登录
 */
router.beforeEach((to, from, next) => {
  const user = localStorage.getItem('user') // 从本地存储检查登录信息
  const whiteList = ['Login', 'Register']   // 免登录白名单
  
  // 如果访问非白名单页面且未登录，强制重定向到登录页
  if (!whiteList.includes(to.name) && !user) {
    next({ name: 'Login' })
  } else {
    next() // 放行
  }
})

export default router
