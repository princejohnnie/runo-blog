<script setup>
import { ref, computed } from 'vue';
import SubscribeModal from "@/components/modals/SubscribeModal.vue";
import CancelSubscriptionModal from "@/components/modals/CancelSubscriptionModal.vue";
import PremiumIconLarge from '@/components/icons/PremiumIconLarge.vue';
import dateFormatter from '@/utils/date.js'
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



const subscriptionStatus = computed(() => {
    let subType = userStore.user.currentSubscription.subscriptionType;
    return subType.charAt(0).toUpperCase() + subType.slice(1);
});

const formattedStartDate = computed(() => {
    return dateFormatter.formatDate(userStore.user.currentSubscription.startDate)
})

const formattedEndDate = computed(() => {
    return dateFormatter.formatDate(userStore.user.currentSubscription.endDate)
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
                    <h3 class="subscription__ActiveType">{{ subscriptionStatus }} Premium
                    </h3>
                    <div class="subscription__activeDates">
                        <p class="subscription__activeDate">Start date: </p>
                        <p class="subscription__activeDateValue">{{ formattedStartDate }}</p>
                    </div>
                    <div class="subscription__activeDates">
                        <p class="subscription__activeDate">End date: </p>
                        <p class="subscription__activeDateValue">{{ formattedEndDate }}</p>
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

    </section>
</template>
<style scoped></style>
