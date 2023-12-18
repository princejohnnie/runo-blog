<script setup>
import CloseButtonIcon from '../icons/CloseButtonIcon.vue';
import { useModalStore } from '@/stores/modal'

const modalStore = useModalStore();

</script>

<template>
    <div class="modal">
        <div class="modal__backdrop" @click="modalStore.closeModal()"></div>
        <div class="modal__inner">
            <button class="modal__close" @click="modalStore.closeModal()">
                <CloseButtonIcon/>
            </button>
            <slot></slot>
        </div>
    </div>
</template>