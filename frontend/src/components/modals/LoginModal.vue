<script setup>
import Modal from '@/components/modals/Modal.vue';
import Input from '@/components//form/Input.vue';
import Form from '@/components/form/Form.vue';
import Button from '@/components/form/Button.vue';
import Auth from '@/requests/Auth.js';
import Swal from 'sweetalert2';

import { ref } from 'vue';
import { useUserStore } from '@/stores/user.js'


const userStore = useUserStore();

const isProcessing = ref(false)

const data = ref({
    email: '',
    password: '',
})

const submitForm = async () => {
    const response = await Auth.login(data.value)
    localStorage.setItem('token', response.data);

    await userStore.me()

    showSuccessAlert()

}

const showSuccessAlert = () => {
    Swal.fire({
        title: "Success!",
        text: "You have logged in successfully!",
        icon: "success"
    });
}

</script>

<template>
    <Modal>
        <div class="modal__form">
            <h2 class="modal__heading">Login</h2>

            <Form :handleLogic="submitForm" v-model:isProcessing="isProcessing">

                <Input type="text" name="email" label="Email:" placeholder="johndoe@gmail.com" v-model:value="data.email"/>

                <Input type="password" name="password" label="Password:" placeholder="***********" v-model:value="data.password" />

                <div class="modal__inputWrapper">
                    <Button type="submit" class="modal__inputButton" :isProcessing="isProcessing">
                        Log in
                    </Button>
                </div>

            </Form>

        </div>
    </Modal>
</template>