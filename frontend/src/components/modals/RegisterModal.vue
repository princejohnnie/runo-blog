<script setup>
import Modal from '@/components/modals/Modal.vue'
import Swal from 'sweetalert2'
import Input from '@/components//form/Input.vue'
import Form from '@/components/form/Form.vue'
import Button from '@/components/form/Button.vue'
import Auth from '@/requests/Auth.js'
import GoogleAuth from '@/components/GoogleAuth.vue'
import slugify from '@/utils/slugify'
import { ref, watch } from 'vue'
import { useUserStore } from '@/stores/user.js'

const userStore = useUserStore()

const emit = defineEmits(['closeModal'])

const isProcessing = ref(false)

const data = ref({
  email: '',
  name: '',
  slug: '',
  description: '',
  password: '',
  premium: false
})

//TODO confirm after premium is accepted in backend
const submitForm = async () => {
  userStore.premium = data.value.premium
  userStore.successNotification = true

  const registrationData = {
    name: data.value.name,
    email: data.value.email,
    password: data.value.password,
    slug: slugify(data.value.name),
    premium: data.value.premium
  }
  const response = await Auth.register(registrationData)
  localStorage.setItem('token', response.data)

  await userStore.me()

  showSuccessAlert()
}

watch(
  () => data.value.name,
  (newVal) => {
    data.value.slug = slugify(newVal)
  }
)
const showSuccessAlert = () => {
  Swal.fire({
    title: 'Success!',
    text: 'You have been registered successfully!',
    icon: 'success',
    willClose: onCloseNotification
  })
}

const onCloseNotification = () => {
  userStore.successNotification = false
}
</script>

<template>
  <Modal @closeModal="onCloseModal()">
    <div class="modal__form">
      <h2 class="modal__heading">Register</h2>

      <Form :handleLogic="submitForm" v-model:isProcessing="isProcessing">
        <Input
          type="text"
          name="name"
          label="Name:"
          placeholder="Your Name"
          v-model:value="data.name"
        />

        <Input
          type="email"
          name="email"
          label="Email:"
          placeholder="johndoe@email.com"
          v-model:value="data.email"
        />

        <Input
          :disabled="true"
          type="text"
          name="slug"
          label="Slug:"
          placeholder="Slug"
          v-model:value="data.slug"
        />

        <Input
          type="text"
          name="description"
          label="Description"
          placeholder="A short description of yourself"
          v-model:value="data.description"
        />

        <Input
          type="password"
          name="password"
          label="Password:"
          placeholder="***********"
          v-model:value="data.password"
        />

        <Input
          type="password"
          name="password_confirmation"
          label="Confirm Password:"
          placeholder="***********"
          v-model:value="data.password_confirmation"
        />

        <input class="modal__checkbox" v-model="data.premium" type="checkbox" />
        <label class="modal__checkLabel" for="checkbox">I want Premium </label>

        <div class="modal__inputWrapper">
          <Button type="submit" class="modal__inputButton" :isProcessing="isProcessing">
            Register
          </Button>
        </div>
        <GoogleAuth @success="showSuccessAlert" />
      </Form>
    </div>
  </Modal>
  @success="showSuccessAlert"v
</template>
