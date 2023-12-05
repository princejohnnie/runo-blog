<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    showingArticlesList: {
        type: Boolean,
        required: false,
        default: false,
    },
})
</script>

<template>
    <ul class="section__categories">
        <li class="section__category">
            <a href="#" class="section__category-link section__category-link--active">All</a>
        </li>
        <li class="section__category">
            <a href="#" class="section__category-link">Adventure</a>
        </li>
        <li class="section__category">
            <a href="#" class="section__category-link">Travel</a>
        </li>
        <li class="section__category">
            <a href="#" class="section__category-link">Fashion</a>
        </li>
        <li class="section__category">
            <a href="#" class="section__category-link">Technology</a>
        </li>
        <li class="section__category">
            <a href="#" class="section__category-link">Branding</a>
        </li>
        <li v-if="!showingArticlesList" class="section__category section__category-last">
            <router-link to="/articles" class="section__category-link">
                View All 
            </router-link>
        </li>
    </ul>
</template>