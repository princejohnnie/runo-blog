<script setup>
import ArticleMain from '@/components/article/ArticleMain.vue';
import Article from '@/requests/Article.js';

import { ref } from 'vue';
import { useRoute } from 'vue-router';


const route = useRoute();
const articleDetail = ref({});

Article.show(route.params.id).then((res) => {
    articleDetail.value = res.data
})

</script>

<template>
    <ArticleMain :showArticle="true" :article="articleDetail"/>
    <div class="section">
        <div class="section__inner">
            <h1>
                {{ articleDetail.title }}
            </h1>
            <p>
                {{ articleDetail.content }}
            </p>
        </div>
    </div>
</template>