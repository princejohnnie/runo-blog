<script setup>
import { ref, computed } from 'vue';

import ArticlesList from '@/components/article/ArticlesList.vue';
import ArticleEditorCard from '@/components/article/ArticleEditorCard.vue';

import Article from '@/requests/Article.js';
import FeaturedArticle from '@/components/article/FeaturedArticle.vue';

const articles = ref([])
const loading = ref(true)

Article.index().then((res) => {
    articles.value = res.data._embedded.items;
    loading.value = false
})

const featuredArticle = computed(() => {
    return articles.value[0]
})

const editorArticles = computed(() => {
    return articles.value.slice(0, 3)
})

</script>

<template>
    <main>

        <div v-if="loading" class="loader">
            <div class="loader__inner">

            </div>
        </div>

        <FeaturedArticle v-if="articles.length" :article="featuredArticle"/>

        <section class="section__popularTopics">
            <div class="section__inner">

                <h2 class="section__heading">Popular topics</h2>
                
                <ArticlesList v-if="articles.length" :articleList="articles" :showAllArticles="false"/>

            </div>

        </section>

        <section class="section__editorPicks">
            <div class="section__inner">
                
                <h2 class="section__heading">Editor's Pick</h2>
                <div class="editorArticles">
                    <ArticleEditorCard v-for="(editorArticle, index) in editorArticles" :key="index" :article="editorArticle" />
                </div>
            </div>
        </section>

    </main>
</template>
