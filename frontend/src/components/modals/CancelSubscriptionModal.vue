<script setup>
import Modal from '@/components/modals/Modal.vue';
import Input from '@/components//form/Input.vue';
import Form from '@/components/form/Form.vue';
import Button from '@/components/form/Button.vue';
import Auth from '@/requests/Auth.js';
import Swal from 'sweetalert2';
import { useModalStore } from '@/stores/modal'
import { ref } from 'vue';
import { useUserStore } from '@/stores/user.js'


const userStore = useUserStore();
const modalStore = useModalStore();
const isProcessing = ref(false)

const data = ref({
    email: '',
    password: '',
})

const submitForm = async () => {
    //const response = await Auth.login(data.value)
    //localStorage.setItem('token', response.data);

    //await userStore.me()

    showSuccessAlert();
    modalStore.closeModal();
}

const showSuccessAlert = () => {
    Swal.fire({
        title: "Success!",
        text: "You have cancelled your subscription!",
        icon: "success"
    });
}



</script>

<template>
    <Modal>

    </Modal>

    <div class="modal">
        <div class="modal__backdrop" @click="modalStore.closeModal()"></div>
        <div class="modal__inner">
            <div class="modal__form">
                <p class="cancelSub__text">Are you sure you want to cancel your subscription?</p>
                <div class="cancelSubscription__buttons">
                    <Button class="button cancelSubscription__buttonYes" @click.prevent="submitForm()"> Yes</Button>
                    <Button class="button cancelSubscription__buttonNo" @click.prevent="modalStore.closeModal()">
                        No</Button>
                </div>

            </div>
        </div>
    </div>
</template>
