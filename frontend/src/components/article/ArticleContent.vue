<script setup>

import ArticleComments from '@/components/article/ArticleComments.vue';

const props = defineProps({
    article: {
        type: Object,
        required: true,
    }
})

</script>

<template>
    <div class="section__inner">

        <p class="articleDetail__content-full">
            {{ article.content }}
        </p>

        <div class="articleDetail__summary">
            <ul class="articleDetail__summary-categories">
                <li class="articleDetail__summary-category">
                    <a href="#" class="section__category-link">ADVENTURE</a>
                </li>
                <li class="articleDetail__summary-category">
                    <a href="#" class="section__category-link">PHOTO</a>
                </li>
                <li class="articleDetail__summary-category">
                    <a href="#" class="section__category-link">DESIGN</a>
                </li>
            </ul>
        
            <hr class="articleDetail__summary-divider">

            <div class="section__author">
                <div class="section__author-inner">
                    <img class="section__author-image" src="/images/public_article_author_image1.jpeg">
                    <div class="section__author-details">
                        <h4 class="section__author-name"> By {{ article.author?.name }}</h4>
                        <p class="section__author-slug">Software Developer</p>
                    </div>
                </div>
                <div class="section__author-socials">
                    <ul class="section__author-socials-links">
                        <li class="section__author-social-link">
                            <img src="/images/facebook.png">
                        </li>
                        <li class="section__author-social-link">
                            <img src="/images/twitter.png">
                        </li>
                        <li class="section__author-social-link">
                            <img src="/images/pinterest.png">
                        </li>
                        <li class="section__author-social-link">
                            <img src="/images/behance.png">
                        </li>
                    </ul>
                </div>
            </div>

            <hr class="articleDetail__summary-divider">
        </div>

       <ArticleComments :article="article"/>
        
    </div>
</template>