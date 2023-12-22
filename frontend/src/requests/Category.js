import Http from '@/requests//Http.js';

function store(data) {
    return Http.post(`/categories`, data)
}

function index() {
    return Http.get(`/categories`)
}

export default { store, index };