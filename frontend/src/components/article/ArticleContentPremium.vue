<script setup>

import { ref, computed } from 'vue';
import { useUserStore } from '@/stores/user.js'
import ArticleComments from '@/components/article/ArticleComments.vue';
import PremiumIcon from '@/components/icons/PremiumIcon.vue';
import PremiumIconWhite from '@/components/icons/PremiumIconWhite.vue';
import Button from '@/components/form/Button.vue';
import LoginModal from "@/components/modals/LoginModal.vue";
import RegisterModal from "@/components/modals/RegisterModal.vue";
import SubscribeModal from "@/components/modals/SubscribeModal.vue";
import { useModalStore } from '@/stores/modal'
import PremiumIconWithBackground from '@/components/icons/PremiumIconWithBackground.vue';

const userStore = useUserStore();
const modalStore = useModalStore();
const props = defineProps({
    article: {
        type: Object,
        required: true,
    }
})

const firstName = userStore.user?.name.split(" ")[0];
const lastName = userStore.user?.name.split(" ")[1];

const defaultAvatar = computed(() => {
    return `https://eu.ui-avatars.com/api/?name=${firstName}+${lastName}&size=250`
})

const openLoginModal = () => {
    modalStore.openModal('login')
}

const openRegisterModal = () => {
    modalStore.openModal('register')
}

const openSubscribeModal = () => {
    modalStore.openModal('subscribe')
}


const test = ref(false);

const displayPremiumMessages = computed(() => {
    if (userStore.user !== null && userStore.isLoggedIn && !userStore.user.isPremium) {
        return true;
    }
    return false;
});

</script>

<template>
    <div class="section__inner">

        <div v-html="article.content"></div>
        <div class="premiumContent__window">
            <div class="premiumContent__windowInner">
                <PremiumIconWithBackground :isPremium="true" />
                <div class="premiumSymbol__boundary" v-if="displayPremiumMessages">
                    <h2 class="premiumContent_windowHeading">Premium article</h2>
                    <p class="premiumContent__message">
                        This article is available to premium members only.
                        You can start reading without restrictions by becoming a premium user today.
                    </p>
                    <div class="premiumContent__buttonsSubscribe">
                        <Button class="premiumContent__button" @click.prevent="openSubscribeModal()">Go Premium</Button>
                    </div>
                </div>
                <div class="premiumSymbol__boundary" v-else>
                    <h2 class="premiumContent_windowHeading">Premium article</h2>
                    <p class="premiumContent__message">
                        This article is available to premium members only.
                        If you are a premium member already, you can Log in to view the content.
                        If not, you can become a premium user today by registering.
                    </p>
                    <div class="premiumContent__buttons">
                        <Button class="premiumContent__buttonLogIn" @click.prevent="openLoginModal()">Log in</Button>
                        <Button class="premiumContent__button" @click.prevent="openRegisterModal()">Register</Button>
                    </div>
                </div>
            </div>
        </div>



        <div class="articleDetail__summary">
            <ul class="articleDetail__summary-categories">
                <li class="articleDetail__summary-category">
                    <a href="#" class="section__category-link">ADVENTURE</a>
                </li>
                <li class="articleDetail__summary-category">
                    <a href="#" class="section__category-link">PHOTO</a>
                </li>
                <li class="articleDetail__summary-category">
                    <a href="#" class="section__category-link">DESIGN</a>
                </li>
            </ul>

            <hr class="articleDetail__summary-divider">

            <div class="section__author">
                <div class="section__author-inner">
                    <img class="section__author-image" :src="article.author.avatarUrl || defaultAvatar">
                    <div class="section__author-details">
                        <h4 class="section__author-name"> By {{ article.author?.name }}</h4>
                        <p class="section__author-slug">Software Developer</p>
                    </div>
                </div>
                <div class="section__author-socials">
                    <ul class="section__author-socials-links">
                        <li class="section__author-social-link">
                            <img src="/images/facebook.png">
                        </li>
                        <li class="section__author-social-link">
                            <img src="/images/twitter.png">
                        </li>
                        <li class="section__author-social-link">
                            <img src="/images/pinterest.png">
                        </li>
                        <li class="section__author-social-link">
                            <img src="/images/behance.png">
                        </li>
                    </ul>
                </div>
            </div>

            <hr class="articleDetail__summary-divider">
        </div>


    </div>
</template>
