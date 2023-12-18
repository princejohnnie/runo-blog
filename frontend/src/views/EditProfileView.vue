<script setup>

import ProfileHeader from '@/components/profile/ProfileHeader.vue';

import Swal from 'sweetalert2';
import Input from '@/components//form/Input.vue';
import Form from '@/components/form/Form.vue';
import Button from '@/components/form/Button.vue';
import Auth from '@/requests/Auth.js';

import { ref } from 'vue';
import { useUserStore } from '@/stores/user.js'

const userStore = useUserStore()

const isProcessing = ref(false)

const links = [
    {
        text: "Back to Profile",
        to: { name: 'my-profile' }
    }
];


const user = ref(userStore.user)

const data = ref({
    email: user.value.email ?? '',
    name: user.value.name ?? '',
    slug: user.value.slug ?? '',
    password: '',
})

const submitForm = async () => {
    const response = await Auth.editProfile(userStore.user.id, data.value)

    user.value = await userStore.me()
    
    showSuccessAlert()
}

const showSuccessAlert = () => {
    Swal.fire({
        title: "Success!",
        text: "User profile updated successfully!",
        icon: "success"
    });
}

</script>

<template>

    <ProfileHeader :links="links" :editProfile="true"/>

    <div class="editProfile__form">
        <div class="editProfile__form-inner">

            <h2 class="editProfile__heading">Edit profile</h2>

            <Form class="column" :handleLogic="submitForm" v-model:isProcessing="isProcessing">

                <Input type="text" name="email" label="Email:" placeholder="johndoe@email.com" v-model:value="data.email" />

                <Input type="text" name="name" label="Name:" placeholder="Your Name" v-model:value="data.name"/>

                <Input type="text" name="slug" label="Slug:" placeholder="Slug" v-model:value="data.slug"/>

                <Input type="password" name="password" label="New Password:" placeholder="***********" v-model:value="data.password" />

                <div class="editProfile__inputWrapper">
                    <Button type="submit" class="editProfile__inputButton" :isProcessing="isProcessing">
                        Update
                    </Button>
                </div>

            </Form>

        </div>

    </div>

</template>