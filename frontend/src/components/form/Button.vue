<script setup>

import { defineProps } from 'vue'

defineProps({
    type: {
        type: String,
        default: 'button',
    },
    isProcessing: {
        type: Boolean,
        default: false,
    },
})


</script>

<template>
    <button class="modal__inputButton" :type="type" :disabled="isProcessing">
        <span v-if="isProcessing">Processing...</span>
        <slot v-else></slot>
    </button>
</template>