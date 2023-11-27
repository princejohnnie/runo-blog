import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import ArticlesView from "../views/ArticlesView.vue";
import ArticleDetail from "../views/ArticleDetail.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
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
        }
    ],
    linkActiveClass: 'mainHeader__nav-link--active'
});

export default router;
