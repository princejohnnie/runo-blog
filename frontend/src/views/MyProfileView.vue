<script setup>
import { useUserStore } from '@/stores/user.js'
import { ref } from 'vue';

import ProfileHeader from '@/components/profile/ProfileHeader.vue';

import Article from '@/requests/Article.js';
import ArticleCardBig from '@/components/article/ArticleCardBig.vue';
import CreateArticleIcon from '../components/icons/CreateArticleIcon.vue';
import MySubscription from '../components/profile/MySubscription.vue';

const userStore = useUserStore()

const links = [
    {
        text: "Edit Profile",
        to: { name: 'edit-profile' }
    }
];

const articles = ref([])

Article.userArticles(userStore.user.id).then((res) => {
    articles.value = res.data?.items;
})


</script>

<template>

    <ProfileHeader :links="links" :editProfile="false"/>

    <section class="myArticles">
        <div class="myArticles__inner">
            <h3 class="section__heading">
                    My Articles
            </h3>
            <div class="myArticles__articles">
                <RouterLink to="/create-article" class="createArticle">
                    <div class="createArticle__inner">
                        <CreateArticleIcon class="createArticle__icon"/>
                        <span class="createArticle__text">
                            Add new article
                        </span>
                    </div>
                </RouterLink>

                <ArticleCardBig v-for="(userArticle, index) in articles" :key="index" :article="userArticle" :showCreateIcon="true" class="createArticle__article--active"/>
            </div>
        </div>
    </section>

    <MySubscription></MySubscription>

</template>