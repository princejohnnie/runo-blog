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

                <slot name="articleMainInner" :shortContent="shortContent">

                </slot>

            </div>
        </div>
</template>