<script setup>
import { defineProps, computed } from 'vue';

const props = defineProps({
    article: {
        type: Object,
        required: true,
    },
    showArticle: {
        type: Boolean,
        required: false,
        default: false,
    }
})


const shortContent = computed(() => {
    if (props.article.content?.length < 100) {
        return props.article.content;
    } else {
        return props.article.content?.slice(0, 100) + "...";
    }
})

</script>

<template>
    <div :class="[showArticle ? 'mainArticle mainArticle--showArticle' : 'mainArticle']">
            <div :class="[showArticle ? 'mainArticle__inner mainArticle__inner--showArticle' : 'mainArticle__inner']">
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
                    <time class="mainArticle__time">08.08.2021</time>
                    <span class="mainArticle__divider"></span>
                    <span class="mainArticle__text">
                        {{ shortContent}}
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