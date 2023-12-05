import Http from './Http.js';

function index() {
    return Http.get(`/articles`)
}

function show(id) {
    return Http.get(`/articles/${id}`)
}

export default { index, show }