import Http from './Http.js';

function index() {
    return Http.get(`/articles`)
}

function show(id) {
    return Http.get(`/articles/${id}`)
}

function comments(id) {
    return Http.get(`/articles/${id}/comments`)
}

export default { index, show, comments }