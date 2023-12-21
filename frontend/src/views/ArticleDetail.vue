<script setup>

import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import { useUserStore } from '@/stores/user.js'

import Article from '@/requests/Article.js';
import ArticleCardBig from '../components/article/ArticleCardBig.vue';
import ArticleHero from '@/components/article/ArticleHero.vue';
import ArticleContent from '@/components/article/ArticleContent.vue';
import ArticleContentPremium from '@/components/article/ArticleContentPremium.vue';

const userStore = useUserStore();
const route = useRoute();
const articleDetail = ref(null);
const articles = ref([])
const loading = ref(true)

Article.show(route.params.id).then((res) => {
    articleDetail.value = res.data
    loading.value = false
})

Article.index().then((res) => {
    articles.value = res.data._embedded.items;
})

const editorArticles = computed(() => {
    return articles.value?.slice(0, 3)
})

const displayFullArticle = computed(() => {
    if (!articleDetail.value.isPremium || (userStore.user !== null && userStore.user.isPremium)) {
        return true;
    }
    return false;
});

</script>

<template>
    <div v-if="loading" class="loader">
        <div class="loader__inner"></div>
    </div>

    <div v-if="!loading">

        <ArticleHero v-if="articleDetail" :article="articleDetail" />

        <div class="section" v-if="displayFullArticle">
            <ArticleContent v-if="articleDetail" :article="articleDetail" />
        </div>

        <div class="section" v-else>
            <ArticleContentPremium v-if="articleDetail" :article="articleDetail" />
        </div>

        <section>
            <div class="section__inner">
                <h2 class="relatedPosts__heading"> Related Posts</h2>
                <div class="section__relatedPosts">
                    <ArticleCardBig v-for="(editorArticle, index) in editorArticles" :key="index" :article="editorArticle"
                        :showCreateIcon="false" />
                </div>
            </div>
        </section>
    </div>
</template>
