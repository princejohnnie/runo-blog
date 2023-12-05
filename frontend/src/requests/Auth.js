import Http from './Http.js';

function login(data) {
    return Http.post(`/login`, data)
}

function register(data) {
    return Http.post(`/register`, data)
}

export default { login, register};