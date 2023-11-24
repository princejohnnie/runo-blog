<script setup>
import { ref } from 'vue';

import ArticleMain from '@/components/article/ArticleMain.vue';
import ArticleCard from '@/components/article/ArticleCard.vue';
import ArticleEditorCard from '@/components/article/ArticleEditorCard.vue';
import HomeCategories from '@/components/homepage/HomeCategories.vue';

import axios from 'axios';

const articles = ref([])

axios.get("http://localhost:8080/articles").then((res) => {
    articles.value = res.data;
})

</script>

<template>
    <main>
        <ArticleMain v-if="articles.length" :article="articles[0]"/>

        <section class="section section__popularTopics">
        <div class="section__inner">

            <HomeCategories />

            <div class="articles">
                <ArticleCard v-for="article in articles" :key="article.id" :article="article" />
                </div>
            </div>

        </section>

    <section class="section section__editorPicks">
        <div class="section__inner">
            <h2 class="section__heading">Editor's Pick</h2>
            <div class="editorArticles">
                <ArticleEditorCard v-for="(editorArticle, index) in editorArticles" :key="index" :editorArticle="editorArticle" />
                </div>
            </div>
        </section>

    </main>
</template>

<script>
export default {
    data() {
        return {
            editorArticles: [
                {
                    image: "/images/editor_article_bg1.jpeg",
                    categories: [
                        "FASHION"
                    ],
                    isPremium: true,
                    time: "08.08.2021",
                    title: "Richird Norton photorealistic rendering as real photos",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data."
                },
                {
                    image: "/images/editor_article_bg2.jpeg",
                    categories: [
                        "FASHION"
                    ],
                    isPremium: false,
                    time: "08.08.2021",
                    title: "Richird Norton photorealistic rendering as real photos",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data."
                },
                {
                    image: "/images/editor_article_bg3.jpeg",
                    categories: [
                        "FASHION"
                    ],
                    isPremium: false,
                    time: "08.08.2021",
                    title: "Richird Norton photorealistic rendering as real photos",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data."
                }
            ]
        }
    },
}
</script>
