const apiUrl = import.meta.env.VITE_API_URL

const photoUploadRoute = "/api/photos/upload"

const backendConfig = { apiUrl, photoUploadUrl: apiUrl + photoUploadRoute }

export { backendConfig }
