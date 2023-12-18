<script setup>
import { ref, computed } from 'vue';
import { useUserStore } from '@/stores/user.js'
import ArticleComment from '@/components/article/ArticleComment.vue';

import Comment from '@/requests/Comment.js';
import Article from '@/requests/Article.js';

const userStore = useUserStore();

const props = defineProps({
    article: {
        type: Object,
        required: true,
    }
})

const data = ref({
    content: '',
    articleId: props.article.id,
})

const articleComments = ref([])

const firstName = userStore.user?.name.split(" ")[0];
const lastName = userStore.user?.name.split(" ")[1];

const defaultAvatar = computed(() => {
    return `https://eu.ui-avatars.com/api/?name=${firstName}+${lastName}&size=250`
})

Article.comments(props.article.id).then((res) => {
    articleComments.value = res.data?.items
});

const postComment = async () => {
    const response = await Comment.post(data.value)

    articleComments.value.unshift(response.data)

    data.value.content = '';
}

const disableButton = computed(() => {
    return data.value.content === ''
})


</script>

<template>
     <div class="section__comments">
            <h2 class="section__comments-heading">Comments:</h2>
            
            <div class="section__comments-inner">

                <div v-if="userStore.isLoggedIn" class="section__comments-edit">
                    <div class="section__author-image-container">
                        <img class="section__author-image" :src="userStore.user.avatarUrl || defaultAvatar">
                    </div>
                    <div class="section__comments-inputWrapper">
                        <textarea class="section__comments-input" v-model="data.content" type="text" placeholder="Write a comment..."></textarea>
                        <button class="section__comments-send" @click.prevent="postComment()" :disabled="disableButton">Send</button>
                    </div>
                </div>

                <ArticleComment v-for="comment in articleComments" :key="comment.id" :comment="comment" />

            </div>

        </div>
</template>