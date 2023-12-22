<script setup>
import { onMounted, ref, watch } from 'vue'
import VueMultiSelect from 'vue-multiselect'
import categoryRoutes from '@/requests/Category.js'

const props = defineProps({
  tags: Array
})

const emit = defineEmits(['OnTagsUpdate'])

const options = ref([])
const tags = ref([])

const updateTags = (tags) => {
  emit(
    'onTagsUpdate',
    tags.value.map((tag) => tag.id)
  )
}

const addTag = async (tag) => {
  const resp = await categoryRoutes.store({ name: tag })
  const tagToAdd = { name: resp.data.name, id: resp.data.id }
  tags.value.push(tagToAdd)
  options.value.push(tagToAdd)
  updateTags(tags)
}

onMounted(async () => {
  const resp = await categoryRoutes.index()
  options.value = resp.data
})

watch(tags, () => {
  updateTags(tags)
})
</script>

<template>
  <div class="multiselect_wrapper">
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
      :hide-selected="true"
      @tag="addTag"
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
