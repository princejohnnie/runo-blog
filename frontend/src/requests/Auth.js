import Http from './Http.js';

function login(data) {
    return Http.post(`/login`, data)
}

function register(data) {
    return Http.post(`/register`, data)
}

function me() {
    return Http.get(`/me`)
}

function editProfile(id, data) {
    return Http.put(`/users/${id}`, data)
}

export default { 
    login,
    register,
    me,
    editProfile,
};