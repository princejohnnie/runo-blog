<script setup>
import ArticleCard from '@/components/article/ArticleCard.vue';
import HomeCategories from '@/components/homepage/HomeCategories.vue';

import { defineProps, computed } from 'vue';


const props = defineProps({
    articleList: {
        type: Array,
        required: true,
        default: () => [],
    },
    showAllArticles: {
        type: Boolean,
        required: false,
        default: false,
    }
})

const articlesToShow = computed(() => {
    if (props.showAllArticles) {
        return props.articleList
    } else {
        return props.articleList.slice(0, 8);
    }
})

</script>

<template>

    <h2 v-if="!showAllArticles" class="section__heading">Popular topics</h2>

    <HomeCategories :showingArticlesList="showAllArticles" />

    <div class="articles">
        <ArticleCard v-for="article in articlesToShow" :key="article.id" :article="article" />
    </div>

</template>