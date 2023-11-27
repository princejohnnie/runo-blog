<script setup>
import Modal from '@/components/modals/Modal.vue';
import Auth from '@/requests/Auth.js';

import { ref } from 'vue';
import Swal from 'sweetalert2';

const emit = defineEmits(['closeModal'])

const data = ref({
    email: '',
    name: '',
    slug: '',
    password: '',
    premium: false,
})

const onCloseModal = () => {
    emit('closeModal')
}

const submit = () => {
    showAlert()
    onCloseModal()
}

const showAlert = () => {
    Swal.fire({
    title: "Success!",
    text: "You have been registered successfully!",
    icon: "success"
    });
}

</script>

<template>
    <Modal @closeModal="onCloseModal()">
        <div class="modal__form">
            <h2 class="modal__heading">Register</h2>
            <form @submit.prevent="submit()">
                <div class="modal__inputWrapper">
                    <label class="modal__inputLabel">Email: </label>
                    <input class="modal__input" type="email" v-model="data.email" placeholder="johndoe@email.com">
                </div>
                <div class="modal__inputWrapper">
                    <label class="modal__inputLabel">Name: </label>
                    <input class="modal__input" type="text" v-model="data.name" placeholder="Your Name">
                </div>
                <div class="modal__inputWrapper">
                    <label class="modal__inputLabel">Slug: </label>
                    <input class="modal__input" type="text" v-model="data.slug" placeholder="Slug">
                </div>
                <div class="modal__inputWrapper">
                    <label class="modal__inputLabel">Password: </label>
                    <input class="modal__input" type="password" v-model="data.password" placeholder="**********">
                </div>
                <div class="modal__inputWrapper">
                    <label class="modal__inputLabel">Repeat password: </label>
                    <input class="modal__input" type="password" placeholder="**********">
                </div>
                <input class="modal__checkbox" v-model="data.premium" type="checkbox">
                <label class="modal__checkLabel" for="checkbox">I want Premium </label>
                <div class="modal__inputWrapper">
                    <button class="modal__inputButton">Register</button>
                </div>
            </form>
        </div>
    </Modal>
</template>