<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    comment: {
        type: Object,
        required: true,
    }
})
</script>

<template>
    <div class="article__comment">
        <div class="article__content-author-image-container">
            <img class="article__content-author-image" src="/images/public_article_author_image1.jpeg">
        </div>
        <div class="article__comment-details">
            <h4 class="article__comment-author-name"> {{ comment.author?.name }}</h4>
            <p class="article__comment-date">
                <time>08.08.2021</time>
                <span> - </span>
                4 min ago
            </p>
            <p class="article__comment-content"> {{ comment.content }} </p>
        </div>
    </div>
</template>