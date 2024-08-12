const apiUrl = import.meta.env.VITE_API_URL

const photoUploadRoute = "/api/photos/upload"
const getCollectionsRoute = "/api/photocollections"

const backendConfig = {
    apiUrl,
    photoUploadUrl: apiUrl + photoUploadRoute,
    getCollectionsUrl: apiUrl + getCollectionsRoute,
}

export { backendConfig }
