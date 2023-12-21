<script setup>
import { ref } from 'vue'
import VueMultiSelect from 'vue-multiselect'

const props = defineProps({
  tags: Array
})

const emit = defineEmits(['update:tags'])

const options = [{ name: 'Adventure' }, { name: 'Travel' }, { name: 'Tech' }, { name: 'Fashion' }]

const tags = ref([])
</script>

<template>
  <div class="imageupload">
    <label class="modal__inputLabel">Tags</label>

    <VueMultiSelect
      v-model="tags"
      :taggable="true"
      :options="options"
      :multiple="true"
      tag-placeholder="Add this as a new tag"
      placeholder="Search or add a tag"
      label="name"
      track-by="name"
      :internal-search="true"
      :show-no-results="false"
      :show-labels="false"
    >
      <template #noOptions>
        <div class="multiselect__option">Start typing to see options</div>
      </template>
    </VueMultiSelect>
  </div>
</template>
<style lang="scss">
@import 'vue-multiselect/dist/vue-multiselect.css';
</style>
