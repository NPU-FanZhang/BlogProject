// 这里是Vue3的用法
import { createRouter, createWebHistory } from 'vue-router'

const routers = [
    {
        path: "/",
        component: () => import("../views/home.vue")
    }, {
        path: '/login',
        component: () => import("../views/register.vue")
    }
]
const router = createRouter({
    history: createWebHistory("/"),
    routes: routers
})
export default router