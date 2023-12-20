<script setup>

import { useUserStore } from '@/stores/user.js'
import { ref, computed, defineProps } from 'vue';
import PremiumIconLarge from '@/components/icons/PremiumIconLarge.vue';
import PremiumEclipseIcon from '@/components/icons/PremiumEclipseIcon.vue';

import myUpload from 'vue-image-crop-upload';

const userStore = useUserStore()

const props = defineProps({
    links: {
        type: Array,
        default: () => [],
    },
    editProfile: Boolean
})

</script>
<template>
    <div class="premiumHeader">
        <div class="premiumHeader__inner">
            <div class="premiumHeader__logoBackground">
                <PremiumIconLarge />
            </div>
            <h2 class="premiumHeader__title">
                Premium Articles
            </h2>
            <p class="premiumHeader__message">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas faucibus consectetur neque vitae sagittis.
                Integer a suscipit lectus. Phasellus fermentum malesuada bibendum.
            </p>
        </div>
    </div>
</template>
