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

export default { 
    login,
    register,
    me,
};