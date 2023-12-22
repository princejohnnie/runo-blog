<script setup>

import EditArticleHeader from '@/components/profile/EditArticleHeader.vue';
import Input from '@/components//form/Input.vue'
import Form from '@/components/form/Form.vue'
import Button from '@/components/form/Button.vue'
import Auth from '@/requests/Auth.js';
import Swal from 'sweetalert2'
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()
const isProcessing = ref(false)


const links = [
    {
        text: "Back to homepage",
        to: { name: 'home' }
    }
];

const data = ref({
    token: route.query.token,
    password: '',
})


const submitForm = async () => {
    console.log("teste")
    try {
        const response = await Auth.resetPassword(data.value);
        console.log(response)
        Swal.fire({
            title: "Success!",
            text: response.data,
            icon: "success"
        });
        router.push({ name: 'home' })
    } catch (error) {
        console.log(error)
        Swal.fire({
            title: "Error!",
            text: error.response.data,
            icon: "error"
        });
    }

}


console.log(route.query.token)
</script>

<template>
    <EditArticleHeader :title="'Reset Password'" :links="links" />

    <div class="editProfile__form">
        <div class="editProfile__form-inner">
            <h2 class="editProfile__heading">Reset password</h2>

            <Form class="column" :handleLogic="submitForm" v-model:isProcessing="isProcessing">



                <Input type="password" name="password" label="New Password:" placeholder="***********"
                    v-model:value="data.password" />

                <div class="editProfile__inputWrapper">
                    <Button type="submit" class="editProfile__inputButton" :isProcessing="isProcessing">
                        Proceed
                    </Button>
                </div>
            </Form>
        </div>
    </div>
</template>
