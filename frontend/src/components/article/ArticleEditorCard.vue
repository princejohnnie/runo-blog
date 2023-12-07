<script setup>

import { computed } from 'vue';
import dateFormatter from '@/utils/date.js'

const props = defineProps({
    article: {
        type: Object,
        required: true,
    }
})

const shortTitle = computed(() => {
    if (props.article.title?.length < 100) {
        return props.article.title;
    } else {
        return props.article.title?.slice(0, 100) + "...";
    }
})

const shortContent = computed(() => {
    if (props.article.content?.length < 250) {
        return props.article.content;
    } else {
        return props.article.content?.slice(0, 250) + "...";
    }
})

const formattedDate = computed(() => {
    return dateFormatter.formatDate(props.article.updatedAt)
})

</script>

<template>
    <div class="editorArticle">
        <img class="editorArticle__image" src="/images/editor_article_bg1.jpeg">
        <div class="editorArticle__category">"FASHION"</div>
        <div class="editorArticle__inner">
            <img src="/images/premium-icon.png" class="editorArticle__premium-icon">
            <time class="editorArticle__time">{{ formattedDate }}</time>
            <p class="editorArticle__heading">{{ shortTitle }}</p>
            <p class="editorArticle__text">{{ shortContent }}</p>
        </div>
    </div>
</template>