<script setup>
import { ref, defineProps, computed } from 'vue';
import dateFormatter from '@/utils/date.js'
import PremiumIcon from '../icons/PremiumIcon.vue';

const props = defineProps({
    article: {
        type: Object,
        required: true,
    }
})

const defaultCover = ref('https://picsum.photos/400/400')

const firstName = props.article.author?.name.split(" ")[0];
const lastName = props.article.author?.name.split(" ")[1];

const defaultAvatar = computed(() => {
    return `https://eu.ui-avatars.com/api/?name=${firstName}+${lastName}&size=250`
})

function removeHtmlTags(htmlString) {
    var doc = new DOMParser().parseFromString(htmlString, 'text/html');
    return doc.body.textContent || "";
}

const shortContent = computed(() => {
    if (props.article.content.length < 200) {
        return removeHtmlTags(props.article.content);
    } else {
        return removeHtmlTags(props.article.content.slice(0, 200) + "...");
    }
})

const formattedDate = computed(() => {
    return dateFormatter.formatDate(props.article.updatedAt)
})


</script>

<template>
    <div class="article">
        <div class="article__image-container">
            <router-link :to="`/articles/${article.id}`">
                <img class="article__image" :src="article.coverUrl || defaultCover">
                <div class="article__categories">
                    <span class="article__category">FASHION</span>
                </div>
            </router-link>
        </div>
        <div class="article__content">
            <div class="article__content-inner">
                <time class="article__time">{{ formattedDate }}</time>
                <PremiumIcon class="article__premium" v-if="article.isPremium" />
            </div>
            <h3 class="article__heading">
                <router-link class="article__heading-link" :to="`/articles/${article.id}`">
                    {{ article.title }}
                </router-link>
            </h3>
            <p class="article__text">
                {{ shortContent }}
            </p>
            <p class="article__content-divider"></p>
            <div class="article__content-author-container">
                <img class="article__content-author-image" :src="article.author.avatarUrl || defaultAvatar">
                <div>
                    <h4 class="article__content-author-name"> By {{ article.author.name }}</h4>
                    <p class="article__content-author-title">{{ article.author?.description }}</p>
                </div>
            </div>
        </div>
    </div>
</template>
