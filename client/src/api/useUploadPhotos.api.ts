import { useState, useCallback } from "react"
import axios, { AxiosProgressEvent } from "axios"
import { backendConfig } from "../config/backendConfig"

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

const CHUNK_SIZE = 1024 * 1024 // 1MB chunks

const usePhotoUpload = (): UsePhotoUploadResult => {
    const [progress, setProgress] = useState<number>(0)
    const [isUploading, setIsUploading] = useState<boolean>(false)
    const [error, setError] = useState<string | null>(null)

    const uploadChunk = async (
        chunk: Blob,
        fileName: string,
        chunkNumber: number,
        totalChunks: number
    ): Promise<void> => {
        const formData = new FormData()
        formData.append("file", chunk, fileName)
        formData.append("fileName", fileName)
        formData.append("chunkNumber", chunkNumber.toString())
        formData.append("totalChunks", totalChunks.toString())

        await axios.post(`${backendConfig.photoUploadUrl}/chunk`, formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        })
    }

    const uploadPhotos = useCallback(
        async (photos: File[]): Promise<UploadResponse> => {
            setIsUploading(true)
            setProgress(0)
            setError(null)

            try {
                let totalUploaded = 0
                const totalSize = photos.reduce(
                    (acc, photo) => acc + photo.size,
                    0
                )

                for (const photo of photos) {
                    const totalChunks = Math.ceil(photo.size / CHUNK_SIZE)

                    for (
                        let chunkNumber = 0;
                        chunkNumber < totalChunks;
                        chunkNumber++
                    ) {
                        const start = chunkNumber * CHUNK_SIZE
                        const end = Math.min(start + CHUNK_SIZE, photo.size)
                        const chunk = photo.slice(start, end)

                        await uploadChunk(
                            chunk,
                            photo.name,
                            chunkNumber,
                            totalChunks
                        )

                        totalUploaded += chunk.size
                        const percentCompleted = Math.round(
                            (totalUploaded * 100) / totalSize
                        )
                        setProgress(percentCompleted)
                    }
                }

                setIsUploading(false)
                return {
                    success: true,
                    message: "All photos uploaded successfully",
                    uploadedFiles: photos.map((photo) => photo.name),
                }
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
