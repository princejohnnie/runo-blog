<script setup>
import Modal from '@/components/modals/Modal.vue'
import Input from '@/components//form/Input.vue'
import Form from '@/components/form/Form.vue'
import Button from '@/components/form/Button.vue'
import Auth from '@/requests/Auth.js'
import Swal from 'sweetalert2'
import { ref } from 'vue'
import { useModalStore } from '@/stores/modal'
import { useUserStore } from '@/stores/user.js'
import GoogleAuth from '@/components/GoogleAuth.vue'

const userStore = useUserStore()
const modalStore = useModalStore();
const isProcessing = ref(false)

const data = ref({
    email: '',
    password: ''
})

const submitForm = async () => {
    const response = await Auth.login(data.value)
    localStorage.setItem('token', response.data)

    await userStore.me()

    showSuccessAlert()
}

const showSuccessAlert = () => {
    Swal.fire({
        title: 'Success!',
        text: 'You have logged in successfully!',
        icon: 'success'
    })
}

const passResetRequest = () => {
    modalStore.passResetModal = true;
}
</script>

<template>
    <Modal>
        <div class="modal__form">
            <h2 class="modal__heading">Login</h2>

            <Form :handleLogic="submitForm" v-model:isProcessing="isProcessing">
                <Input type="text" name="email" label="Email:" placeholder="johndoe@gmail.com" v-model:value="data.email" />

                <Input type="password" name="password" label="Password:" placeholder="***********"
                    v-model:value="data.password" />

                <p class="modal__forgotPassword" @click="passResetRequest">Forgot password?</p>

                <div class="modal__inputWrapper">
                    <Button type="submit" class="modal__inputButton" :isProcessing="isProcessing">
                        Log in
                    </Button>
                </div>
                <GoogleAuth @success="showSuccessAlert" />
            </Form>
        </div>
    </Modal>
</template>
