<script setup>
defineProps({
  categories: {
    type: Object,
    required: true
  },
  outlined: {
    type: Boolean,
    required: false,
    default: false
  },
  feature: {
    type: Boolean,
    required: false,
    default: false
  }
})
</script>
<template>
  <ul v-if="outlined" class="articleDetail__summary-categories">
    <li v-for="cat in categories" class="articleDetail__summary-category">
      <a href="#" class="section__category-link">{{ cat?.name }}</a>
    </li>
  </ul>

  <ul v-else-if="feature" class="mainArticle__categories">
    <li v-for="cat in categories" class="mainArticle__category">
      <a href="#" class="mainArticle__category-link">{{ cat?.name }}</a>
    </li>
  </ul>
  <div v-else="feature" class="article__categories">
    <span v-for="cat in categories" class="article__category">{{ cat.name }}</span>
  </div>
</template>
