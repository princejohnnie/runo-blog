<script setup>

import ArticleComment from '@/components/article/ArticleComment.vue';

const props = defineProps({
    article: {
        type: Object,
        required: true,
    }
})

</script>

<template>
     <div class="section__comments">
            <h2 class="section__comments-heading">Comments:</h2>
            
            <div class="section__comments-inner">

                <div class="section__comments-edit">
                    <div class="section__author-image-container">
                        <img class="section__author-image" src="/images/public_article_author_image1.jpeg">
                    </div>
                    <div class="section__comments-inputWrapper">
                        <textarea class="section__comments-input" type="text" placeholder="Write a comment..."></textarea>
                        <button class="section__comments-send">Send</button>
                    </div>
                </div>

                <ArticleComment v-for="comment in article.comments" :key="comment.id" :comment="comment" />

            </div>

        </div>
</template>