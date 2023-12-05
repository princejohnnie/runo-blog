<script setup>

import { computed } from 'vue';

const props = defineProps({
    article: {
        type: Object,
        required: true,
    }
})

const shortContent = computed(() => {
    if (props.article.content?.length < 200 ) {
        return props.article.content
    } else {
        return props.article.content?.slice(0, 200) + '...'
    }
})

</script>

<template>

    <div class="mainArticle mainArticle__header">
        <div class="mainArticle__inner mainArticle__header-inner">

            <h2 class="mainArticle__heading articleDetail__heading">
                {{ article.title }}
            </h2>

            <p class="mainArticle__content articleDetail__content-short">
                <span class="mainArticle__text">
                    {{ shortContent }}
                </span>
            </p>
            <p class="articleDetail__author">
                By {{ article.author?.name }}
            </p>

        </div>
    </div>

</template>