
const slugify = (str) => {
    const slug = str
        .toLowerCase()
        .replace(/ /g, '-')
        .replace(/[^\w-]+/g, '')
    return slug
}

export default slugify;