import Http from '@/requests//Http.js';

function index() {
    return Http.get(`/comments`)
}

function post(data) {
    return Http.post(`/comments`, data)
}

export default { 
    index,
    post,
};