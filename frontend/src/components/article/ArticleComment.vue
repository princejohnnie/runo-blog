<script setup>
import { computed, defineProps } from 'vue';
import dateFormatter from '@/utils/date.js'

const props = defineProps({
    comment: {
        type: Object,
        required: true,
    }
})

const formattedDate = computed(() => {
    return dateFormatter.formatDate(props.comment.updatedAt)
})

const formattedTime = computed(() => {
    return dateFormatter.formatTime(props.comment.updatedAt)
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
                <time>{{ formattedDate }}</time>
                <span> - </span>
                {{ formattedTime }}
            </p>
            <p class="article__comment-content"> {{ comment.content }} </p>
        </div>
    </div>
</template>