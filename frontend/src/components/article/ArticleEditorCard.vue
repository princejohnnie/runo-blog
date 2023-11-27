<script setup>


const props = defineProps({
    article: {
        type: Object,
        required: true,
    }
})

// const shortContent = computed(() => {
//     if (props.article.content?.length < 100) {
//         return props.article.content;
//     } else {
//         return props.article.content?.slice(0, 100) + "...";
//     }
// })

</script>

<template>
    <div class="editorArticle">
        <img class="editorArticle__image" src="/images/editor_article_bg1.jpeg">
        <div class="editorArticle__category">"FASHION"</div>
        <div class="editorArticle__inner">
            <img src="/images/premium-icon.png" class="editorArticle__premium-icon">
            <time class="editorArticle__time">08.08.2021</time>
            <p class="editorArticle__heading">{{ article.title }}</p>
            <p class="editorArticle__text">{{ article.content.slice(0, 200) }}</p>
        </div>
    </div>
</template>