<script setup>
import { defineProps, computed } from 'vue';

const props = defineProps({
    article: {
        type: Object,
        requires: true,
    }
})

const shortContent = computed(() => {
    if (props.article.content.length < 100) {
        return props.article.content;
    } else {
        return props.article.content.slice(0, 100) + "...";
    }
})


</script>

<template>
    <div class="article">
        <div class="article__image-container">
            <router-link :to="`/articles/${article.id}`">
                <img class="article__image" src="/images/public_article_image1.jpeg">
                <div class="article__categories">
                    <span class="article__category" v-for="category in article.categories">{{ category }}</span>
                </div>
            </router-link>
        </div>
        <div class="article__content">
            <div class="article__content-inner">
                <time class="article__time">08.02.2021</time>
                <img class="article__premium-icon" src="/images/premium_icon2.png" v-if="article.isPremium">
            </div>
            <h3 class="article__heading">
                <router-link class="article__heading-link" :to="`/articles/${article.id}`">
                    {{ article.title }}
                </router-link>
            </h3>
            <p class="article__text">
                {{ shortContent }}
            </p>
            <p class="article__content-divider"></p>
            <div class="article__content-author">
                <img class="article__content-author-image" src="/images/public_article_author_image1.jpeg">
                <div class="article__content-author-text">
                    <h4 class="article__content-author-name"> By {{ article.author.name }}</h4>
                    <p class="article__content-author-title">Software Developer</p>
                </div>
            </div>
        </div>
    </div>
</template>