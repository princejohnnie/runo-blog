<script setup>
import { defineEmits, defineProps } from 'vue'
import { useErrorStore } from '@/stores/error'
import { useModalStore } from '@/stores/modal'
import handleError from '@/utils/handleError';

const errorStore = useErrorStore();
const modalStore = useModalStore();

const props = defineProps({
    handleLogic: Function,
    isProcessing: Boolean,
})

errorStore.clearErrors()

const emits = defineEmits(['update:isProcessing'])

const submitForm = async () => {
    emits('update:isProcessing', true)
    try {
        await props.handleLogic()
        modalStore.closeModal()
    } catch(errorResponse) {
        handleError(errorResponse, errorStore)
    } finally {
        emits('update:isProcessing', false)
    }
}

</script>

<template>
    <form @submit.prevent="submitForm">
        <slot></slot>
    </form>
</template>