<script setup>
import Swal from 'sweetalert2'
import Input from '@/components//form/Input.vue'
import Form from '@/components/form/Form.vue'
import Button from '@/components/form/Button.vue'
import Article from '@/requests/Article.js'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import dateFormatter from '@/utils/date.js'
import slugify from '@/utils/slugify'
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { QuillEditor } from '@vueup/vue-quill'
import ImageUpload from '../form/ImageUpload.vue'
import MultiSelectTags from '@/components/form/MultiSelectTags.vue'

const isProcessing = ref(false)

const router = useRouter()

const props = defineProps({
  article: {
    type: Object,
    required: false
  },
  isNew: Boolean
})

const data = ref({
  title: props?.article?.title,
  slug: props?.article?.slug,
  content: props?.article?.content,
  cover: null,
  isPremium: false
})

const currentDate = computed(() => {
  if (props.isNew) {
    return dateFormatter.formatDate(new Date())
  } else {
    return dateFormatter.formatDate(props.article.updatedAt)
  }
})

const createArticle = async () => {
  const response = await Article.store(data.value)

  showSuccessAlert()

  router.push({ name: 'my-profile' })
}

const updateArticle = async () => {
  const response = await Article.update(data.value, props.article.id)

  showSuccessAlert()

  router.push({ name: 'my-profile' })
}

const setArticleCover = (cover) => {
  data.value.cover = cover
}

const showSuccessAlert = () => {
  Swal.fire({
    title: 'Success!',
    text: 'Article created successfully!',
    icon: 'success'
  })
}

watch(
  () => data.value.title,
  (newVal) => {
    data.value.slug = slugify(newVal)
  }
)
</script>

<template>
  <div class="editArticle__form">
    <div class="editArticle__form-inner">
      <h2 class="editArticle__heading">{{ isNew ? 'Add content' : 'Edit content' }}</h2>

      <Form
        :handleLogic="isNew ? createArticle : updateArticle"
        v-model:isProcessing="isProcessing"
      >
        <div class="editArticle__formContent">
          <Input
            type="text"
            name="title"
            label="Title"
            placeholder="Set Title"
            v-model:value="data.title"
          />
          <MultiSelectTags />
          <Input
            :disabled="true"
            class="editArticle__input"
            type="text"
            name="slug"
            label="Slug"
            placeholder="Set slug"
            v-model:value="data.slug"
          />
          <Input
            class="editArticles__premium"
            type="checkbox"
            label="Make premium"
            v-model="data.isPremium"
          />
          <div class="modal__inputWrapper">
            <label class="modal__inputLabel"> Content </label>
            <div class="modal__quillEditor">
              <QuillEditor theme="snow" v-model:content="data.content" contentType="html" />
            </div>
          </div>
          <div class="group__class">
            <div class="modal__inputWrapper">
              <ImageUpload
                label="Article Image"
                name="Cover"
                :url="article?.coverUrl"
                @setCover="(cover) => setArticleCover(cover)"
              />
            </div>
          </div>
          <div class="editArticle__inputWrapper">
            <Button type="submit" class="editProfile__inputButton" :isProcessing="isProcessing">
              {{ isNew ? 'Add new' : 'Update' }}
            </Button>
          </div>
        </div>
      </Form>
    </div>
  </div>
</template>
