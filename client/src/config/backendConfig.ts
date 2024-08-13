const apiUrl = import.meta.env.VITE_API_URL

const photoUploadUrl = apiUrl + "/api/photos/upload"
const getCollectionsUrl = apiUrl + "/api/photocollections"
const getNextCollectionIdUrl = apiUrl + "/api/photocollections/next-id"

const backendConfig = {
    apiUrl,
    photoUploadUrl,
    getCollectionsUrl,
    getNextCollectionIdUrl,
}

export { backendConfig }
