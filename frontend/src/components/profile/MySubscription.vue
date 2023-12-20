<script setup>
import { ref } from 'vue';
import SubscribeModal from "@/components/modals/SubscribeModal.vue";
import CancelSubscriptionModal from "@/components/modals/CancelSubscriptionModal.vue";
import PremiumIconLarge from '@/components/icons/PremiumIconLarge.vue';
import { useModalStore } from '@/stores/modal'
import { useUserStore } from '@/stores/user'

const modalStore = useModalStore();
const userStore = useUserStore();

const openSubscribeModal = () => {
    modalStore.openModal('subscribe')
}

const openCancelSubscriptionModal = () => {
    modalStore.openModal('cancelSub')
}

openCancelSubscriptionModal

const test = ref(false)

const history = ref([]);
userStore.subscriptions().then((res) => {
    console.log(res.data);
    history.value = res.data;
})
</script>

<template>
    <section class="subscription">

        <div class="subscription__inner" v-if="userStore.user.isPremium === false">
            <div class="subscription__addSub">
                <h3 class="section__heading">
                    My Subscription
                </h3>
                <router-link :to="{ name: 'edit-subscription' }">
                    <p class="subscription__add-text">
                        Add subscription
                    </p>
                </router-link>
            </div>

            <div class="subscription__active">
                <p class="subscription__active-text">
                    No active subscription.
                </p>
                <button class="subscription__premiumButton" @click.prevent="openSubscribeModal()">Go Premium</button>
            </div>
        </div>

        <div class="subscription__inner" v-else>
            <div class="subscription__addSub">
                <h3 class="section__heading">
                    My Subscription
                </h3>
                <router-link :to="{ name: 'edit-subscription' }">
                    <p class="subscription__add-text">
                        Manage subscription
                    </p>
                </router-link>
            </div>

            <div class="subscription__active">
                <div class="subscription__activeInfo">
                    <h3 class="subscription__ActiveType">Monthly Premium</h3>
                    <div class="subscription__activeDates">
                        <p class="subscription__activeDate">Start date: </p>
                        <p class="subscription__activeDateValue">10.11.2023</p>
                    </div>
                    <div class="subscription__activeDates">
                        <p class="subscription__activeDate">End date: </p>
                        <p class="subscription__activeDateValue">10.12.2023</p>
                    </div>
                    <router-link :to="{ name: 'edit-subscription' }">
                        <h3 class="subscription__editOption">Edit payment</h3>
                    </router-link>
                    <p class="subscription__active-text" @click.prevent="openCancelSubscriptionModal()">Cancel subscription
                    </p>
                </div>
                <div class="subscription__logoBackground">
                    <PremiumIconLarge />
                </div>
            </div>
        </div>


        <Teleport to="body">
            <SubscribeModal v-if="modalStore.showSubscribeModal" />
        </Teleport>
        <Teleport to="body">
            <CancelSubscriptionModal v-if="modalStore.showCancelSubscriptionModal" />
        </Teleport>

    </section>
</template>
<style scoped></style>
