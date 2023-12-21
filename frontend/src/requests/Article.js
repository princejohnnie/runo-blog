import Http from './Http.js';

function index() {
    return Http.get(`/articles`)
}

function show(id) {
    return Http.get(`/articles/${id}`)
}

function userArticles(id) {
    return Http.get(`/users/${id}/articles`)
}

function store(data) {
    const formData = new FormData();

    Object.keys(data).forEach((key) => {
        formData.append(key, data[key]);
    })
    console.log(formData);

    return Http.post(`/articles`, formData)
}

function update(data, id) {
    const formData = new FormData();

    Object.keys(data).forEach((key) => {
        formData.append(key, data[key]);
    })

    return Http.put(`/articles/${id}`, formData)
}

function comments(id) {
    return Http.get(`/articles/${id}/comments`)
}

//TODO correct endpoint after backend makes ir available
function premiumIndex() {
    return Http.get(`articles-premium`)
}

export default { index, show, comments, store, userArticles, update, premiumIndex }
