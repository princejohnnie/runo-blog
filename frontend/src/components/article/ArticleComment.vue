<script setup>
import { computed, defineProps } from 'vue';
import { useUserStore } from '@/stores/user.js'
import dateFormatter from '@/utils/date.js'

const props = defineProps({
    comment: {
        type: Object,
        required: true,
    }
})

const userStore = useUserStore()

const firstName = userStore.user?.name.split(" ")[0];
const lastName = userStore.user?.name.split(" ")[1];

const defaultAvatar = computed(() => {
    return `https://eu.ui-avatars.com/api/?name=${firstName}+${lastName}&size=250`
})

const formattedDate = computed(() => {
    return dateFormatter.formatDate(props.comment.updatedAt)
})

const formattedTime = computed(() => {
    return dateFormatter.formatTime(props.comment.updatedAt)
})

</script>

<template>
    <div class="article__comment">
        <div class="article__content-author-image-container">
            <img class="article__content-author-image" :src="comment.author.avatarUrl || defaultAvatar">
        </div>
        <div class="article__comment-details">
            <h4 class="article__comment-author-name"> {{ comment.author?.name }}</h4>
            <p class="article__comment-date">
                <time>{{ formattedDate }}</time>
                <span> - </span>
                {{ formattedTime }}
            </p>
            <p class="article__comment-content"> {{ comment.content }} </p>
        </div>
    </div>
</template>