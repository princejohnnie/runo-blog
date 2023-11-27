<script setup>
import { ref } from 'vue';

import ArticleMain from '@/components/article/ArticleMain.vue';
import ArticlesList from '@/components/article/ArticlesList.vue';
import ArticleEditorCard from '@/components/article/ArticleEditorCard.vue';

import Article from '@/requests/Article.js';

const articles = ref([])

Article.index().then((res) => {
    articles.value = res.data;
})

</script>

<template>
    <main>
        <ArticleMain v-if="articles.length" :article="articles[0]">

            <template #articleMainInner="shortContent">
                <ul class="mainArticle__categories">
                    <li class="mainArticle__category">
                        <a href="#" class="mainArticle__category-link">ADVENTURE</a>
                    </li>
                    <li class="mainArticle__category">
                        <a href="#" class="mainArticle__category-link">TRAVEL</a>
                    </li>
                </ul>
                <h2 class="mainArticle__heading">
                    {{ articles[0].title }}
                </h2>
                <p class="mainArticle__content">
                    <time class="mainArticle__time">08.08.2021</time>
                    <span class="mainArticle__divider"></span>
                    <span class="mainArticle__text">
                        {{ shortContent.shortContent }}
                    </span>
                </p>
                
                <div class="mainArticle__pagination">
                    <svg xmlns="http://www.w3.org/2000/svg" width="80" height="15" viewBox="0 0 44 8" fill="none">
                        <circle cx="4" cy="4" r="4" fill="white"/>
                        <circle cx="22" cy="4" r="4" fill="white" fill-opacity="0.2"/>
                        <circle cx="40" cy="4" r="4" fill="white" fill-opacity="0.2"/>
                    </svg>
                </div>
            </template>
        </ArticleMain>

        <section class="section__popularTopics">
            <div class="section__inner">
                
                <ArticlesList v-if="articles.length" :articleList="articles" :showAllArticles="false"/>

            </div>

        </section>

        <section class="section__editorPicks">
            <div class="section__inner">
                
                <h2 class="section__heading">Editor's Pick</h2>
                <div class="editorArticles">
                    <ArticleEditorCard v-for="(editorArticle, index) in articles.slice(0, 3)" :key="index" :article="editorArticle" />
                </div>
            </div>
        </section>

    </main>
</template>
