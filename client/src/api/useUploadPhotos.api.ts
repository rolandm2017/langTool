import { useState, useCallback } from "react"
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

interface UsePhotoUploadResult {
    uploadPhotos: (photos: File[]) => Promise<UploadResponse>
    progress: number
    isUploading: boolean
    error: string | null
}

const usePhotoUpload = (): UsePhotoUploadResult => {
    const [progress, setProgress] = useState<number>(0)
    const [isUploading, setIsUploading] = useState<boolean>(false)
    const [error, setError] = useState<string | null>(null)

    const uploadPhotos = useCallback(
        async (photos: File[]): Promise<UploadResponse> => {
            setIsUploading(true)
            setProgress(0)
            setError(null)

            try {
                const formData = new FormData()
                photos.forEach((photo, index) => {
                    formData.append(`photo-${index}`, photo)
                })

                const response = await axios.post<UploadResponse>(
                    backendConfig.photoUploadUrl,
                    formData,
                    {
                        headers: {
                            "Content-Type": "multipart/form-data",
                        },
                        onUploadProgress: (progressEvent: UploadProgress) => {
                            if (progressEvent.total) {
                                const percentCompleted = Math.round(
                                    (progressEvent.loaded * 100) /
                                        progressEvent.total
                                )
                                setProgress(percentCompleted)
                            }
                        },
                    }
                )

                setIsUploading(false)
                return response.data
            } catch (error) {
                setIsUploading(false)
                if (axios.isAxiosError(error)) {
                    const errorMessage =
                        error.response?.data?.message ||
                        "Upload failed due to a network error"
                    setError(errorMessage)
                    console.error(
                        "Upload failed:",
                        error.response?.data || error.message
                    )
                    return {
                        success: false,
                        message: errorMessage,
                    }
                } else {
                    const errorMessage = "Upload failed due to an unknown error"
                    setError(errorMessage)
                    console.error("Upload failed:", error)
                    return {
                        success: false,
                        message: errorMessage,
                    }
                }
            }
        },
        []
    )

    return { uploadPhotos, progress, isUploading, error }
}

export default usePhotoUpload
