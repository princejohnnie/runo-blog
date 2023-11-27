<script setup>

import { ref } from 'vue';
import { useRoute } from 'vue-router';

import Article from '@/requests/Article.js';
import ArticleMain from '@/components/article/ArticleMain.vue';
import ArticleEditorCard from '../components/article/ArticleEditorCard.vue';
import ArticleComment from '../components/article/ArticleComment.vue';


const route = useRoute();
const articleDetail = ref({});
const articles = ref([])


Article.show(route.params.id).then((res) => {
    articleDetail.value = res.data
})

Article.index().then((res) => {
    articles.value = res.data;
})

</script>

<template>
    <ArticleMain :showArticle="true" :article="articleDetail">
        
        <template #articleMainInner="shortContent">

            <h2 class="mainArticle__heading articleDetail__heading">
                {{ articleDetail.title }}
            </h2>

            <p class="mainArticle__content articleDetail__content-short">
                <span class="mainArticle__text">
                    {{ shortContent.shortContent}}
                </span>
            </p>
            <p class="articleDetail__author">
                By {{ articleDetail.author?.name }}
            </p>
        </template>
                
    </ArticleMain>

    <div class="section">
        <div class="section__inner">

            <p class="articleDetail__content-full">
                {{ articleDetail.content }}
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
                            <h4 class="section__author-name"> By Jennifer Loper</h4>
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

                    <ArticleComment v-for="comment in articleDetail.comments" :key="comment.id" :comment="comment" />

                </div>

            </div>
        </div>
    </div>

    <section>
        <div class="section__inner">
            <h2 class="relatedPosts__heading"> Related Posts</h2>
            <div class="section__relatedPosts">
                <ArticleEditorCard v-for="(editorArticle, index) in articles.slice(0, 3)" :key="index" :article="editorArticle" />
            </div>
        </div>
    </section>
</template>