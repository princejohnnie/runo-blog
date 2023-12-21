<script setup>
import LoginModal from "@/components/modals/LoginModal.vue";
import RegisterModal from "@/components/modals/RegisterModal.vue";
import SubscribeModal from "@/components/modals/SubscribeModal.vue";
import { useModalStore } from '@/stores/modal'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router';
import { ref, watch, watchEffect, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const modalStore = useModalStore();
const userStore = useUserStore();
const router = useRouter();
const menuOpen = ref(false);
const menuToggleRef = ref(false);
const route = useRoute();

const openLoginModal = () => {
    modalStore.openModal('login')
}

const openRegisterModal = () => {
    modalStore.openModal('register')
}

const openSubscribeModal = () => {
    modalStore.openModal('subscribe')
}

const logoutUser = () => {
    userStore.logout()
    router.push({ name: "home" })
}

watch(() => [userStore.premium, userStore.successNotification], function () {
    if (userStore.wantsPremium && userStore.isLoggedIn && userStore.notificationClosed) {
        openSubscribeModal();
    }
});

watch('route.name', () => {
    console.debug(`MyCoolComponent - watch route.name changed to ${route.name}`);
})

onMounted(() => {
    // Assign the ref after the component is mounted
    menuToggleRef.value = document.getElementById('menu-toggle');
});

watchEffect(() => {
    menuOpen.value = false;
});
</script>
<template>
    <header class="mainHeader">
        <div class="mainHeader__inner">
            <h1 class="mainHeader__logo">
                <router-link class="mainHeader__logo-link" to="/">
                    Levelup Blog
                </router-link>
            </h1>
            <nav class="mainHeader__nav">
                <input id="menu-toggle" type="checkbox" v-model="menuOpen" />
                <label class='menu-button-container' for="menu-toggle">
                    <div class='menu-button'></div>
                </label>
                <ul class="mainHeader__nav-list menu">
                    <li class="mainHeader__nav-item">
                        <router-link to="/" class="mainHeader__nav-link">
                            Home
                        </router-link>
                    </li>
                    <li class="mainHeader__nav-item" v-if="userStore.isPremium">
                        <router-link to="/premium-articles" class="mainHeader__nav-link">
                            Premium
                        </router-link>
                    </li>
                    <li class="mainHeader__nav-item" v-else>
                        <a href="#" class="mainHeader__nav-link">About</a>
                    </li>
                    <li class="mainHeader__nav-item">
                        <router-link to="/articles" class="mainHeader__nav-link">
                            Articles
                        </router-link>
                    </li>
                    <li v-if="userStore.isLoggedIn" class="mainHeader__nav-item">
                        <router-link :to="{ name: 'my-profile' }" class="mainHeader__nav-link">
                            My Profile
                        </router-link>
                    </li>
                    <li v-if="userStore.isGuest" class="mainHeader__nav-item">
                        <a href="#" @click.prevent="openLoginModal()" class="mainHeader__nav-link">Login</a>
                    </li>
                    <li v-if="userStore.isGuest" class="mainHeader__nav-item">
                        <a href="#" @click.prevent="openRegisterModal()" class="mainHeader__nav-link">Register</a>
                    </li>
                    <li v-if="userStore.isLoggedIn" class="mainHeader__nav-item">
                        <a href="#" @click.prevent="logoutUser()" class="mainHeader__nav-link">Logout</a>
                    </li>
                </ul>
            </nav>
        </div>
    </header>
</template>
