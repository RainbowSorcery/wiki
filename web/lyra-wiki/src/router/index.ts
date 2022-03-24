import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AdminEbook from '@/views/admin/AdminEbook.vue'
import AdminCategoryVue from '@/views/admin/AdminCategory.vue'
import AdminDocVue from '@/views/admin/AdminDoc.vue'
import Doc from '@/views/Doc.vue'
import AdminUser from '@/views/admin/AdminUser.vue'


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
      component: AdminEbook
    },
    {
      path: '/admin/user',
      name: 'adminUser',
      component: AdminUser
    },
    {
      path: '/admin/category',
      name: 'AdminCategoryVue',
      component: AdminCategoryVue
    },
    {
      path: '/admin/doc',
      name: 'AdminDocVue',
      component: AdminDocVue
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
    }
  ]
})

export default router
