<script setup>
import { RouterView } from "vue-router";
import { useModalStore } from '@/stores/modal'
import Header from "@/components/layout/Header.vue";
import Footer from "@/components/layout/Footer.vue";
import LoginModal from "@/components/modals/LoginModal.vue";
import RegisterModal from "@/components/modals/RegisterModal.vue";
import SubscribeModal from "@/components/modals/SubscribeModal.vue";

const modalStore = useModalStore();
</script>

<template>
    <Header />

    <main>
        <RouterView />
    </main>

    <Footer />
    <LoginModal v-if="modalStore.modal === 'login'" @close-modal="openLoginModel = false" />
    <RegisterModal v-if="modalStore.modal === 'register'" @close-modal="openLoginModel = false" />
</template>

<style lang="scss">
@import "@/assets/main.scss";
</style>
