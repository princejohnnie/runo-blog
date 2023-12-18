<script setup>

import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import dateFormatter from '@/utils/date.js'
import EditArticleIcon from '../icons/EditArticleIcon.vue';

const router = useRouter()

const props = defineProps({
    article: {
        type: Object,
        required: true,
    },
    showCreateIcon: {
        type: Boolean,
        required: true,
    }
})

const defaultCover = ref('https://picsum.photos/400/400')

function removeHtmlTags(htmlString) {
    var doc = new DOMParser().parseFromString(htmlString, 'text/html');
    return doc.body.textContent || "";
}

const shortTitle = computed(() => {
    if (props.article.title?.length < 100) {
        return props.article.title;
    } else {
        return props.article.title?.slice(0, 100) + "...";
    }
})

const shortContent = computed(() => {
    if (props.article.content?.length < 250) {
        return removeHtmlTags(props.article.content);
    } else {
        return removeHtmlTags(props.article.content?.slice(0, 250) + "...");
    }
})

const formattedDate = computed(() => {
    return dateFormatter.formatDate(props.article.updatedAt)
})

</script>

<template>
    <div class="editorArticle">
        <RouterLink :to="`/articles/${article.id}`">
            <img class="editorArticle__image" :src="article.coverUrl || defaultCover">
        </RouterLink>
        <RouterLink :to="`/articles/${article.id}/edit`">
            <EditArticleIcon v-if="showCreateIcon" class="editorArticle__edit-icon"/>
        </RouterLink>
        <div class="editorArticle__category">FASHION</div>
        <div class="editorArticle__inner">
            <img src="/images/premium-icon.png" v-if="props.article.isPremium" class="editorArticle__premium-icon">
            <time class="editorArticle__time">{{ formattedDate }}</time>
            <p class="editorArticle__heading">{{ shortTitle }}</p>
            <p class="editorArticle__text">{{ shortContent }}</p>
        </div>
    </div>
</template>