import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from '@/stores/user.js'
import HomeView from "../views/HomeView.vue";
import ArticlesView from "../views/ArticlesView.vue";
import ArticleDetail from "../views/ArticleDetail.vue";
import MyProfileView from "@/views/MyProfileView.vue";
import EditProfileView from "@/views/EditProfileView.vue";
import CreateArticleView from "@/views/CreateArticleView.vue";
import EditArticleView from "@/views/EditArticleView.vue";
import EditSubscription from '@/views/EditSubscription.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    scrollBehavior(to, from, savedPosition) {
        return {
            top: 0,
            behavior: "smooth",
        };
    },
    routes: [
        {
            path: "/",
            name: "home",
            component: HomeView,
        },
        {
            path: "/articles",
            name: "articles",
            component: ArticlesView,
        },
        {
            path: "/articles/:id",
            name: "article.detail",
            component: ArticleDetail,
        },
        {
            path: "/my-profile",
            name: "my-profile",
            component: MyProfileView,
            meta: {
                requiresAuth: true,
            },
        },
        {
            path: "/edit-profile",
            name: "edit-profile",
            component: EditProfileView,
            meta: {
                requiresAuth: true,
            },
        },
        {
            path: "/create-article",
            name: "create-article",
            component: CreateArticleView,
            meta: {
                requiresAuth: true,
            },
        },
        {
            path: "/articles/:id/edit",
            name: "edit-article",
            component: EditArticleView,
            meta: {
                requiresAuth: true,
            },
        },
        {
            path: '/edit-subscription',
            name: 'edit-subscription',
            component: EditSubscription,
            meta: {
              requiresAuth: true
            }
          }
    ],
    linkActiveClass: 'mainHeader__nav-link--active'
});

router.beforeEach(async(to, from, next) => {
    const userStore = useUserStore();

    if (localStorage.getItem('token')) {
        await userStore.me();
    }

    if(to.meta.requiresAuth && userStore.isGuest) {
        next({ name: "home" })
    }

    next();

})

export default router;
