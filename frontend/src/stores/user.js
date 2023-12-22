import Swal from 'sweetalert2';

import Auth from '@/requests/Auth.js';

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
    const user = ref(null)

    const isLoggedIn = computed(() => user.value !== null)
    const isGuest = computed(() => user.value === null )
    const isPremium = computed(() => user.value !== null && user.value.isPremium )

    async function me() {
        try {
            const response = await Auth.me()
            console.log(response);
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

    async function subscribe(data) {
        try {
            const response = await Auth.subscribe(data)
            console.log("Got subscription: ", response.data)

            Swal.fire({
                title: "Success!",
                text: "You have subscribed successfully!",
                icon: "success"
            });
        } catch(error) {
            console.log("Subscribe error: ", error)
            Swal.fire({
                title: error.message,
                text: error.response?.data,
                icon: "error"
            });
        }
    }


    async function cancelSubscription(id) {
        try {
            const response = await Auth.cancelSubscription(id);
            console.log(response)
            Swal.fire({
                title: "Success!",
                text: "You have cancel your subscription!",
                icon: "success"
            });
        } catch(error) {
            Swal.fire({
                title: "Error!",
                text: error.message,
                icon: "error"
            });
        }
    }



    async function subscriptions() {
        try {
            const response = await Auth.subscriptions()
            console.log("Got subscriptions: ", response.data)
            return response;
        } catch(error) {
            console.log("Subscriptions error: ", error)

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
        isPremium,
        me,
        login,
        register,
        logout,
        premium,
        wantsPremium,
        successNotification,
        notificationClosed,
        subscribe,
        subscriptions,
        cancelSubscription
    }
})
