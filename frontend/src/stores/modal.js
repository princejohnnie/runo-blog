import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useModalStore = defineStore('modal', () => {
    const modal = ref(null)

    function openModal(value) {
        modal.value = value
    }

    function closeModal() {
        modal.value = null
    }

    const showLoginModal = computed(() => modal.value === 'login')
    const showRegisterModal = computed(() => modal.value === 'register')
    const showSubscribeModal = computed(() => modal.value === 'subscribe')

    return {
        modal,
        openModal,
        closeModal,
        showLoginModal,
        showRegisterModal,
        showSubscribeModal
    }
})
