import Http from '@/requests//Http.js';

function login(data) {
    return Http.post(`/login`, data)
}

function register(data) {
    return Http.post(`/register`, data)
}

function me() {
    return Http.get(`/me`)
}

function googleAuth(data) {
    return Http.post(`/google-auth`, data)
}

function editProfile(id, data) {
    return Http.put(`/users/${id}`, data)
}

function subscribe(data) {
    return Http.post(`/users/subscribe`, data)
}

function subscriptions() {
    return Http.get(`/user/subscriptions`)
}

function cancelSubscription(id) {
    return Http.post(`/user/cancel-subscription/${id}`)
}

export default {
    login,
    register,
    me,
    googleAuth,
    editProfile,
    subscribe,
    subscriptions,
    cancelSubscription
};
