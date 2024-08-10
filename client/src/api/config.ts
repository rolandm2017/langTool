const apiUrl = import.meta.env.VITE_API_URL

const photoUploadRoute = "/temp/upload/photos"

const config = { apiUrl, photoUploadUrl: apiUrl + photoUploadRoute }

export { config }
