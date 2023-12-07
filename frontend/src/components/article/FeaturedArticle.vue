<script setup>

import { computed } from 'vue';
import dateFormatter from '@/utils/date.js'

const props = defineProps({
    article: {
        type: Object,
        required: true,
    }
})


const shortContent = computed(() => {
    if (props.article.content.length < 100 ) {
        return props.article.content
    } else {
        return props.article.content.slice(0, 199) + '...'
    }
})

const formattedDate = computed(() => {
    return dateFormatter.formatDate(props.article.updatedAt)
})

</script>


<template>

    <div class="mainArticle">
        <div class="mainArticle__inner">

            <ul class="mainArticle__categories">
                <li class="mainArticle__category">
                    <a href="#" class="mainArticle__category-link">ADVENTURE</a>
                </li>
                <li class="mainArticle__category">
                    <a href="#" class="mainArticle__category-link">TRAVEL</a>
                </li>
            </ul>
            <h2 class="mainArticle__heading">
                {{ article.title }}
            </h2>
            <p class="mainArticle__content">
                <time class="mainArticle__time">{{ formattedDate }}</time>
                <span class="mainArticle__divider"></span>
                <span class="mainArticle__text">
                    {{ shortContent }}
                </span>
            </p>
    
            <div class="mainArticle__pagination">
                <svg xmlns="http://www.w3.org/2000/svg" width="80" height="15" viewBox="0 0 44 8" fill="none">
                    <circle cx="4" cy="4" r="4" fill="white"/>
                    <circle cx="22" cy="4" r="4" fill="white" fill-opacity="0.2"/>
                    <circle cx="40" cy="4" r="4" fill="white" fill-opacity="0.2"/>
                </svg>
            </div>

        </div>
    </div>

</template>