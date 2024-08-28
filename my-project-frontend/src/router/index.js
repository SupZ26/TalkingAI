import { createRouter, createWebHistory } from "vue-router";
import { unauthorized } from "@/net";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "welcome",
            component: () => import("@/views/WelcomeView.vue"),
            children: [
                {
                    path: "",
                    name: "welcome-login",
                    component: () => import("@/views/welcome/LoginPage.vue"),
                },
                {
                    path: "register",
                    name: "welcome-register",
                    component: () => import("@/views/welcome/RegisterPage.vue"),
                },
                {
                    path: "forget",
                    name: "welcome-forget",
                    component: () => import("@/views/welcome/ForgetPage.vue"),
                },
            ],
        },
        {
            path: "/index",
            name: "index",
            component: () => import("@/views/IndexView.vue"),
        },
        {
            path: "/a",
            name: "a",
            component: () => import("@/views/a.vue"),
        },
        {
            path: "/WeChatLogin",
            name: "/WeChatLogin",
            component: () => import("@/views/WeChatLogin.vue"),
        },
        {
            path: "/charge",
            name: "charge",
            component: () => import("@/views/charge_list.vue"),
        },
        {
            path: "/packages",
            name: "packages",
            component: () => import("@/views/package_list.vue"),
        },
        {
            path: "/home",
            name: "home",
            component: () => import("@/views/HomeView.vue"),
        },
        {
            path: "/user",
            name: "user",
            component: () => import("@/views/user.vue"),
        },
    ],
});

router.beforeEach((to, from, next) => {
    const isUnauthorized = unauthorized();
    // 除了登录页面之外的所有页面在没登陆时都跳转到登录页面
    if (!to.name.startsWith("welcome") && isUnauthorized) {
        next("/");
    } else {
        next();
    }
    //next();
});

export default router;
