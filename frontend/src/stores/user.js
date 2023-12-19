import Swal from 'sweetalert2';

import Auth from '@/requests/Auth.js';

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
    const user = ref(null)

    const isLoggedIn = computed(() => user.value !== null)
    const isGuest = computed(() => user.value === null )

    async function me() {
        try {
            const response = await Auth.me()
            user.value = response.data
        } catch(error) {
            user.value = null;
        }
    }

    async function login(data) {
        try {
            const response = await Auth.login(data)
            localStorage.setItem('token', response.data);

            Swal.fire({
                title: "Success!",
                text: "You have logged in successfully!",
                icon: "success"
            });
        } catch(error) {
            user.value = null
        }
    }

    async function register(data) {
        try {
            const response = await Auth.register(data)
            console.log("Got token after register ", response.data)
            localStorage.setItem('token', response.data);

            Swal.fire({
                title: "Success!",
                text: "You have registered successfully!",
                icon: "success"
            });
        } catch(error) {
            user.value = null
        }

    }

    async function logout() {
        localStorage.removeItem('token')
        user.value = null
    }

    const premium = ref(false);
    const wantsPremium = computed(() => premium.value === true)

    const successNotification = ref(false);
    const notificationClosed = computed(() => successNotification.value === false)


    return {
        user,
        isLoggedIn,
        isGuest,
        me,
        login,
        register,
        logout,
        premium,
        wantsPremium,
        successNotification,
        notificationClosed

    }
})
