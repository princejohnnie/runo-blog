<script setup>
import { defineProps, watch } from 'vue'
import { useErrorStore } from '@/stores/error'

const errorStore = useErrorStore()

const props = defineProps({
    name: String,
    label: String,
    type: String,
    placeholder: String,
    value: {
        required: true,
    },
    required: {
        type: Boolean,
        default: false,
    }

})

watch(() => props.value, () => {
    errorStore.deleteError(props.name)
})


</script>

<template>
    <div class="modal__inputWrapper" :class="{ 'modal__inputWrapper--error': errorStore.getError(name)}">
        <label class="modal__inputLabel"> {{ label }} </label>
        <input class="modal__input" :name="name" :type="type" :placeholder="placeholder" :required="required" :value="value"
                @input=" $emit('update:value', $event.target.value)"  />

        <span class="modal__inputError" v-if="errorStore.getError(name)">
            {{ errorStore.getError(name) }}
        </span>
    </div>

</template>