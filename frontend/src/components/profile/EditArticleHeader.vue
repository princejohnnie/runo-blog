<script setup>

import { defineProps } from 'vue';

const props = defineProps({
    links: {
        type: Array,
        default: () => [],
    },
    title: String,
})

</script>

<template>

    <div class="profileHeader">
        <div class="profileHeader__inner">
            <h2 class="editArticleHeader__title">
                {{ props.title }}
            </h2>

            <div class="profileHeader__actions">
                <router-link v-for="link in links" :to="link.to" class="profileHeader__action" v-if="links.length">
                    {{ link.text }}
                </router-link>
            </div>
        </div>
    </div>

</template>