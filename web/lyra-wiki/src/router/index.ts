import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AdminEbook from '@/views/admin/AdminEbook.vue'
import AdminCategoryVue from '@/views/admin/AdminCategory.vue'
import AdminDocVue from '@/views/admin/AdminDoc.vue'
import userCollectInfo from '@/views/UserCollectInfo.vue'
import Doc from '@/views/Doc.vue'
import AdminUser from '@/views/admin/AdminUser.vue'
import store from '@/store'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/admin/ebook',
      name: 'AdminEbook',
      component: AdminEbook,
      meta: {
        loginRequire: true
      }
    },
    {
      path: '/admin/user',
      name: 'adminUser',
      component: AdminUser,
      meta: {
        loginRequire: true
      }
    },
    {
      path: '/admin/category',
      name: 'AdminCategoryVue',
      component: AdminCategoryVue,
      meta: {
        loginRequire: true
      }
    },
    {
      path: '/admin/doc',
      name: 'AdminDocVue',
      component: AdminDocVue,
      meta: {
        loginRequire: true
      }
    },
    {
      path: '/doc',
      name: 'doc',
      component: Doc
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    }, 
    {
      path: '/userCollectInfo',
      name: 'userCollectInfo',
      component: userCollectInfo
    }
  ]
})

// 路由登录校验 每次路由跳转之前 to 新路由 from旧路由 next路由跳转 如果无参数表示不拦截 跳转下个路由
router.beforeEach((to, from, next) => {
  if (to.matched.some(function(item) {
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;

    if (loginUser.token === null || loginUser.token === '' || loginUser.token === undefined) {
      console.log("用户未登录, 路由拦截")
      next("/")
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router

