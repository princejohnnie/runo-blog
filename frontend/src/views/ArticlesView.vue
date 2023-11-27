<script setup>
import { ref } from 'vue';

import ArticleMain from '@/components/article/ArticleMain.vue';
import ArticlesList from '@/components/article/ArticlesList.vue';

import Article from '@/requests/Article.js';

const articles = ref([])

Article.index().then((res) => {
    articles.value = res.data;
})

</script>

<template>
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
                
            </template>
    </ArticleMain>

    <div class="section__inner">
        <ArticlesList v-if="articles.length" :articleList="articles" :showAllArticles="true"/>
    </div>

</template>