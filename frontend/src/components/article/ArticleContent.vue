<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/stores/user.js'
import ArticleComments from '@/components/article/ArticleComments.vue'
import Categories from '@/components/Categories.vue'

const userStore = useUserStore()

const props = defineProps({
  article: {
    type: Object,
    required: true
  }
})

const firstName = userStore.user?.name.split(' ')[0]
const lastName = userStore.user?.name.split(' ')[1]

const defaultAvatar = computed(() => {
  return `https://eu.ui-avatars.com/api/?name=${firstName}+${lastName}&size=250`
})
</script>

<template>
  <div class="section__inner">
    <div v-html="article.content"></div>

    <div class="articleDetail__summary">
      <Categories
        :categories="article.categories"
        v-if="article?.categories?.length > 0"
        :outlined="true"
      />

      <hr class="articleDetail__summary-divider" />

      <div class="section__author">
        <div class="section__author-inner">
          <img class="section__author-image" :src="article.author.avatarUrl || defaultAvatar" />
          <div class="section__author-details">
            <h4 class="section__author-name">By {{ article.author?.name }}</h4>
            <p class="section__author-slug">{{ article.author?.description }}</p>
          </div>
        </div>
        <div class="section__author-socials">
          <ul class="section__author-socials-links">
            <li class="section__author-social-link">
              <img src="/images/facebook.png" />
            </li>
            <li class="section__author-social-link">
              <img src="/images/twitter.png" />
            </li>
            <li class="section__author-social-link">
              <img src="/images/pinterest.png" />
            </li>
            <li class="section__author-social-link">
              <img src="/images/behance.png" />
            </li>
          </ul>
        </div>
      </div>

      <hr class="articleDetail__summary-divider" />
    </div>

    <ArticleComments :article="article" />
  </div>
</template>
