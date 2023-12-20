<script setup>
import { useRouter } from 'vue-router'
import Auth from '@/requests/Auth.js'
import { useUserStore } from '@/stores/user.js'
import { useModalStore } from '@/stores/modal.js'

const userStore = useUserStore()
const modalStore = useModalStore()

const router = useRouter()

const emit = defineEmits(['success'])

const callback = async (response) => {
  // Successful log in with Google
  console.log('Handle the response', response.credential)
  const data = {
    credential: response.credential
  }
  const res = await Auth.googleAuth(data)

  console.log('Response: ', res)
  localStorage.setItem('token', res.data)
  await userStore.me()
  modalStore.closeModal()
  emit('success')
}

const idConfig = {
  auto_select: true
}

const buttonConfig = {
  size: 'large',
  text: 'continue_with',
  width: 320
}
</script>
<template>
  <div class="loginWithGoogle">
    <GoogleLogin :callback="callback" :id-configuration="idConfig" :button-config="buttonConfig" />
  </div>
</template>
