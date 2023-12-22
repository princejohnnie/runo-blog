<script setup>

import EditArticleHeader from '@/components/profile/EditArticleHeader.vue';
import EditArticleContent from '@/components/profile/EditArticleContent.vue';

import Article from '@/requests/Article.js';

import { ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute()
const isProcessing = ref(false)


const links = [
    {
        text: "Back to homepage",
        to: { name: 'home' }
    }
];




const data = ref({
    email: '',
})

const submitForm = async () => {
    //const response = await Auth.editProfile(userStore.user.id, data.value)
    showSuccessAlert()
    router.push({ name: 'home' })
}

const showSuccessAlert = () => {
    Swal.fire({
        title: 'Success!',
        text: 'Verification email was sent',
        icon: 'success'
    })
}

</script>

<template>
    <EditArticleHeader :title="'Reset Password'" :links="links" />

    <Form class="column" :handleLogic="submitForm" v-model:isProcessing="isProcessing">

        <Input type="text" name="email" label="Email:" placeholder="johndoe@email.com" v-model:value="data.email" />

        <div class="editProfile__inputWrapper">
            <Button type="submit" class="editProfile__inputButton" :isProcessing="isProcessing">
                Update
            </Button>
        </div>
    </Form>
</template>
