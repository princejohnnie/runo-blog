<script setup>
import PremiumIconWhite from '@/components/icons/PremiumIconWhite.vue';

const props = defineProps({
    isPremium: {
        type: Boolean,
        required: false,
        default: false
    }
})

</script>
<template>
    <div class="premiumSymbol__background" v-if="isPremium">
        <PremiumIconWhite />
    </div>
</template>
