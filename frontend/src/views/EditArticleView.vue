<script setup>

import EditArticleHeader from '@/components/profile/EditArticleHeader.vue';
import EditArticleContent from '@/components/profile/EditArticleContent.vue';

import Article from '@/requests/Article.js';

import { ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute()

const article = ref(null);

const links = [
    {
        text: "Back to Profile",
        to: { name: 'my-profile' }
    }
];

Article.show(route.params.id).then((res) => {
    article.value = res.data
})


</script>

<template>

    <EditArticleHeader v-if="article" :title="article.title" :links="links"/>

    <EditArticleContent :isNew="false" v-if="article" :article="article"></EditArticleContent>

</template>