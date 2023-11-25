<script setup>
import Modal from '@/components/modals/Modal.vue';
import Auth from '@/requests/Auth.js';

import { ref } from 'vue';

const emit = defineEmits(['closeModal'])

const data = ref({
    email: '',
    password: '',
})

const onCloseModal = () => {
    emit('closeModal')
}

const submit = () => {
    Auth.login(data.value).then(response => {
        onCloseModal()
        console.log(response)
    }).catch(error => {
        console.log(error)
    })
}

</script>

<template>
    <Modal>
        <div class="modal__form">
            <h2 class="modal__heading">Login</h2>
            <form @submit.prevent="submit()">
                <div class="modal__inputWrapper">
                    <label class="modal__inputLabel">Email: </label>
                    <input class="modal__input" type="email" v-model="data.email" placeholder="johndoe@email.com">
                </div>
                <div class="modal__inputWrapper">
                    <label class="modal__inputLabel">Password: </label>
                    <input class="modal__input" type="password" v-model="data.password" placeholder="**********">
                </div>
                <div class="modal__inputWrapper">
                    <button class="modal__inputButton">Log in</button>
                </div>
            </form>
        </div>
    </Modal>
</template>