import axios from 'axios';

const Http = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
});

Http.interceptors.request.use(function (config) {
    if (localStorage.getItem('token')) {
        config.headers.Authorization = `Bearer ${localStorage.getItem('token')}`;
    }
        return config;
    }, function (error) {
        return Promise.reject(error);
    });

export default Http;
