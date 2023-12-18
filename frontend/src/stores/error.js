import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useErrorStore = defineStore('error', () => {
    const errors = ref({})

    function setErrors(newErrors) {
        errors.value = newErrors
    }

    function clearErrors() {
        errors.value = {}
    }

    function getError(key) {
        return errors.value[key] ? errors.value[key] : null
    }

    function hasErrors() {
        return Object.keys(errors.value).length > 0
    }

    function deleteError(key) {
        delete errors.value[key]
    }

    return {
        errors,
        setErrors,
        getError,
        clearErrors,
        hasErrors,
        deleteError,
    }
})