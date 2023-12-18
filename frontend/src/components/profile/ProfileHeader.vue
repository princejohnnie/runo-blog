<script setup>

import { useUserStore } from '@/stores/user.js'
import { ref, computed, defineProps } from 'vue';

import myUpload from 'vue-image-crop-upload';

const userStore = useUserStore()

const props = defineProps({
    links: {
        type: Array,
        default: () => [],
    },
    editProfile: Boolean
})

const showUploadDialog = ref(false)
const avatarUrl = ref('')
const currentUser = ref({})

currentUser.value = userStore.user

const firstName = currentUser.value?.name.split(" ")[0];
const lastName = currentUser.value?.name.split(" ")[1];

if(currentUser.value.avatarUrl === null) {
    avatarUrl.value = `https://eu.ui-avatars.com/api/?name=${firstName}+${lastName}&size=250`
} else {
    avatarUrl.value = currentUser.value.avatarUrl
}

const token = localStorage.getItem('token')

const cropUploadSuccess = (jsonData) => {
    avatarUrl.value = jsonData.avatarUrl
    currentUser.value = userStore.me()
}

</script>

<template>

    <div class="profileHeader">
        <div class="profileHeader__inner">
            <img :src="avatarUrl" 
                :class="editProfile ? 'profileHeader__image profileHeader__image--edit' : 'profileHeader__image'" 
                @click="showUploadDialog=true">

                <my-upload
                    langType="en"
                    @crop-upload-success="cropUploadSuccess"
                    v-model="showUploadDialog"
                    :width="300"
                    :height="300"
                    field="avatar"
                    url="http://localhost:8080/user/upload-avatar"
                    :headers="{
                        Authorization: `Bearer ${token}`,
                        Accept: 'application/json'
                    }"
                    img-format="png">
                </my-upload>

            <h2 class="profileHeader__name">
                {{ userStore.user?.name }}
            </h2>
            <p class="profileHeader__email">
                {{ userStore.user?.email }}
            </p>

            <div class="profileHeader__actions">
                <router-link v-for="link in links" :to="link.to" class="profileHeader__action" v-if="links.length">
                    {{ link.text }}
                </router-link>
                <router-link to="/" class="profileHeader__action">Manage subscription</router-link>
            </div>
        </div>
    </div>

</template>