<script setup>
import { ref, computed } from 'vue';

import ArticleMain from '@/components/article/ArticleMain.vue';
import ArticlesList from '@/components/article/ArticlesList.vue';

import Article from '@/requests/Article.js';
import FeaturedArticle from '@/components/article/FeaturedArticle.vue';

const articles = ref([])

Article.index().then((res) => {
    articles.value = res.data;
})

const latestArticle = computed(() => {
    return articles.value[0]
})

</script>

<template>
    <FeaturedArticle v-if="articles.length" :article="latestArticle"/>

    <div class="section__inner">
        <ArticlesList v-if="articles.length" :articleList="articles" :showAllArticles="true"/>
    </div>

</template>