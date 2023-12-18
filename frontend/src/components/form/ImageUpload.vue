<script setup>

import { ref } from 'vue';
import ImageIcon from '../icons/ImageIcon.vue';
import CancelIcon from '../icons/CancelIcon.vue';

const props = defineProps({
    name: String,
    label: String,
    url: String,
})

const emit = defineEmits(['setCover'])

const uploadFieldRef = ref(null)
const imageUploadPreview = ref(props.url)


const onUploadImage = () => {
    uploadFieldRef.value.click()

}

const handleInput = (e) => {
    if (e.target.files.length === 0) {
        return
    }

    emit('setCover', e.target.files[0])

    const previewURL = URL.createObjectURL(e.target.files[0])
    imageUploadPreview.value = previewURL;
}

const clearImage = () => {
    emit('setCover', null)

    imageUploadPreview.value = null
}

</script>

<template>
    <div class="imageupload">
        <label class="modal__inputLabel">{{ label ? label : name }}</label>

        <div v-if="!imageUploadPreview" class="imageupload__inner" @click="onUploadImage">
            <ImageIcon class="imageupload__icon"></ImageIcon>
            <h4 class="imageupload__text">Upload an Image</h4>
            <p>Click on the icon to upload an image</p>
        </div>

        <div v-else class="imageupload__inner">
            <CancelIcon class="imageupload__cancel" @click="clearImage"/>
            <img class="imageupload__preview" alt="cover image" :src="imageUploadPreview">
        </div>

        <input ref="uploadFieldRef" type="file" accepts="images/*" hidden @input="handleInput" />

        <p class="imageupload__error"></p>
    </div>
</template>