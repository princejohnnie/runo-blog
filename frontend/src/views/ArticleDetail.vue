<script setup>

import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';

import Article from '@/requests/Article.js';
import ArticleEditorCard from '@/components/article/ArticleEditorCard.vue';
import ArticleHero from '@/components/article/ArticleHero.vue';
import ArticleContent from '@/components/article/ArticleContent.vue';


const route = useRoute();
const articleDetail = ref({});
const articles = ref([])


Article.show(route.params.id).then((res) => {
    articleDetail.value = res.data
})

Article.index().then((res) => {
    articles.value = res.data;
})

const editorArticles = computed(() => {
    return articles.value.slice(0, 3)
})

</script>

<template>
    
    <ArticleHero :article="articleDetail"/>

    <div class="section">
        <ArticleContent :article="articleDetail"/>
    </div>

    <section>
        <div class="section__inner">
            <h2 class="relatedPosts__heading"> Related Posts</h2>
            <div class="section__relatedPosts">
                <ArticleEditorCard v-for="(editorArticle, index) in editorArticles" :key="index" :article="editorArticle" />
            </div>
        </div>
    </section>
</template>