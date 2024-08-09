import axios, { AxiosResponse } from "axios"

interface UploadResponse {
    // Define the structure of your server's response here
    success: boolean
    message: string
    uploadedFiles?: string[]
}

export const uploadPhotos = async (
    photos: File[],
    serverUrl: string
): Promise<UploadResponse> => {
    try {
        const formData = new FormData()

        photos.forEach((photo, index) => {
            formData.append(`photo${index}`, photo)
        })

        const response: AxiosResponse<UploadResponse> = await axios.post(
            serverUrl,
            formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            }
        )

        return response.data
    } catch (error) {
        console.error("Error uploading photos:", error)
        throw error
    }
}
