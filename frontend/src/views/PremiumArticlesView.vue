<script setup>
import { useUserStore } from '@/stores/user.js'
import { ref, computed } from 'vue';

import PremiumHeader from '@/components/premium/PremiumHeader.vue';

import ArticlesList from '@/components/article/ArticlesList.vue';
import Article from '@/requests/Article.js';

const userStore = useUserStore()

const links = [
    {
        text: "Edit Profile",
        to: { name: 'edit-profile' }
    }
];



const articles = ref([])
const loading = ref(true)

Article.index().then((res) => {
    articles.value = res.data._embedded.items;
    loading.value = false
})

</script>

<template>
    <PremiumHeader />

    <section class="section__popularTopics">
        <div class="section__inner">

            <h2 class="section__heading">Premium</h2>

            <ArticlesList v-if="articles.length" :articleList="articles" :showAllArticles="false" />

        </div>

    </section>
</template>
