import axios from "axios"

interface UploadResponse {
    success: boolean
    message: string
    uploadedFiles?: string[]
}

interface UploadProgress {
    loaded: number
    total: number
}

const uploadPhotos = async (
    photos: File[],
    url: string,
    onProgressUpdate?: (progress: number) => void
): Promise<UploadResponse> => {
    try {
        const formData = new FormData()
        photos.forEach((photo, index) => {
            formData.append(`photo-${index}`, photo)
        })

        const response = await axios.post<UploadResponse>(url, formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
            onUploadProgress: (progressEvent: UploadProgress) => {
                if (onProgressUpdate && progressEvent.total) {
                    const percentCompleted = Math.round(
                        (progressEvent.loaded * 100) / progressEvent.total
                    )
                    onProgressUpdate(percentCompleted)
                }
            },
        })

        return response.data
    } catch (error) {
        if (axios.isAxiosError(error)) {
            console.error(
                "Upload failed:",
                error.response?.data || error.message
            )
            return {
                success: false,
                message:
                    error.response?.data?.message ||
                    "Upload failed due to a network error",
            }
        } else {
            console.error("Upload failed:", error)
            return {
                success: false,
                message: "Upload failed due to an unknown error",
            }
        }
    }
}

export default uploadPhotos
