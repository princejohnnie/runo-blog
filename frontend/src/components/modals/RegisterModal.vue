<script setup>
import Modal from '@/components/modals/Modal.vue'
import Swal from 'sweetalert2'
import Input from '@/components//form/Input.vue'
import Form from '@/components/form/Form.vue'
import Button from '@/components/form/Button.vue'
import Auth from '@/requests/Auth.js'
import GoogleAuth from '@/components/GoogleAuth.vue'

import { ref } from 'vue'
import { useUserStore } from '@/stores/user.js'

const userStore = useUserStore()

const emit = defineEmits(['closeModal'])

const isProcessing = ref(false)

const data = ref({
  email: '',
  name: '',
  slug: '',
  password: '',
  premium: false
})

//TODO confirm after premium is accepted in backend
const submitForm = async () => {
  userStore.premium = data.value.premium
  userStore.successNotification = true
  const response = await Auth.register(data.value)
  localStorage.setItem('token', response.data)

  await userStore.me()

  showSuccessAlert()
}

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
          name="email"
          label="Email:"
          placeholder="johndoe@email.com"
          v-model:value="data.email"
        />

        <Input
          type="text"
          name="name"
          label="Name:"
          placeholder="Your Name"
          v-model:value="data.name"
        />

        <Input type="text" name="slug" label="Slug:" placeholder="Slug" v-model:value="data.slug" />

        <Input
          type="password"
          name="password"
          label="Password:"
          placeholder="***********"
          v-model:value="data.password"
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
@success="showSuccessAlert"v</template>
