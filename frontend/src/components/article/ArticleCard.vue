<template>
    <div class="article">
        <div class="article__image-container">
            <img class="article__image" :src="article.image">
            <div class="article__categories">
                <span class="article__category" v-for="category in article.categories">{{ category }}</span>
            </div>
        </div>
        <div class="article__content">
            <div class="article__content-inner">
                <time class="article__time">{{ article.time }}</time>
                <img class="article__premium-icon" src="/images/premium_icon2.png" v-if="article.isPremium">
            </div>
            <h3 class="article__heading">{{ article.title }}</h3>
            <p class="article__text">
                {{ article.content }}
            </p>
            <p class="article__content-divider"></p>
            <div class="article__content-author">
                <img class="article__content-author-image" :src="article.authorImage">
                <div class="article__content-author-text">
                    <h4 class="article__content-author-name"> By {{ article.name }}</h4>
                    <p class="article__content-author-title">{{ article.description }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            article : {
                type: Object,
                required: true
            }
        }
    }
</script>