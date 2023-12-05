<script setup>
import CloseButtonIcon from '../icons/CloseButtonIcon.vue';

const emit = defineEmits(['closeModal'])

const onCloseModal = () => {
    emit('closeModal')
}

</script>

<template>
    <div class="modal">
        <div class="modal__backdrop" @click="onCloseModal"></div>
        <div class="modal__inner">
            <button class="modal__close" @click="emit('closeModal')">
                <CloseButtonIcon/>
            </button>
            <slot @closeModal="onCloseModal()"></slot>
        </div>
    </div>
</template>