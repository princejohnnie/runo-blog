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
//import Auth from '@/requests/Auth.js';

const userStore = useUserStore()
const modalStore = useModalStore();
const isProcessing = ref(false)

const data = ref({
    email: '',
})

const submitForm = async () => {
    console.log("teste")
    try {
        const response = await Auth.forgotPassword(data.value);
        console.log(response)
        Swal.fire({
            title: "Success!",
            text: response.message,
            icon: "success"
        });
    } catch (error) {
        console.log(error)
        Swal.fire({
            title: "Error!",
            text: error.message,
            icon: "error"
        });
    }

}

const showSuccessAlert = () => {
    Swal.fire({
        title: 'Success!',
        text: 'You have logged in successfully!',
        icon: 'success'
    })
}
</script>

<template>
    <Modal>
        <div class="modal__form">
            <h2 class="modal__heading">Forgot password?</h2>

            <Form :handleLogic="submitForm" v-model:isProcessing="isProcessing">
                <Input type="text" name="email" label="Email:" placeholder="johndoe@gmail.com" v-model:value="data.email" />

                <div class="modal__inputWrapper">
                    <Button type="submit" class="modal__inputButton" :isProcessing="isProcessing">
                        Send email
                    </Button>
                </div>
            </Form>
        </div>
    </Modal>
</template>
