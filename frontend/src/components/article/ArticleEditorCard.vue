<template>
    <div class="editorArticle">
        <img class="editorArticle__image" :src=editorArticle.image>
        <div class="editorArticle__category" v-for="category in editorArticle.categories">{{ category }}</div>
        <div class="editorArticle__inner">
            <img src="/images/premium-icon.png" class="editorArticle__premium-icon" v-if="editorArticle.isPremium">
            <time class="editorArticle__time">{{ editorArticle.time }}</time>
            <p class="editorArticle__heading">{{ editorArticle.title }}</p>
            <p class="editorArticle__text">{{ editorArticle.content }}</p>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            editorArticle : {
                type: Object,
                required: true
            }
        }
    }
</script>