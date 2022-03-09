import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AdminEbook from '@/views/admin/AdminEbook.vue'
import AdminCategoryVue from '@/views/admin/AdminCategory..vue'

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
      path: '/admin/category',
      name: 'AdminCategoryVue',
      component: AdminCategoryVue
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
