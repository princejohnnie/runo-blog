<script setup>
import EditArticleHeader from '@/components/profile/EditArticleHeader.vue'
import EditArticleContent from '@/components/profile/EditArticleContent.vue'

const links = [
  {
    text: 'Back to Profile',
    to: { name: 'my-profile' }
  }
]
</script>

<template>
  <EditArticleHeader title="Article editor" :links="links" />

  <EditArticleContent :isNew="true" editButtonText="Add new"></EditArticleContent>
</template>
