import axios from "axios"

interface PhotoSetResponse {
    success: boolean
    message: string
    photoSet?: string[][]
}

const fetchPhotoSet = async (
    photoSetId: string
): Promise<string[][] | null> => {
    try {
        const response = await axios.get<PhotoSetResponse>("/api/photo-sets", {
            params: { photoSetId },
        })

        if (response.data.success && response.data.photoSet) {
            return response.data.photoSet
        } else {
            console.error("Failed to fetch photo set:", response.data.message)
            return null
        }
    } catch (error) {
        if (axios.isAxiosError(error)) {
            console.error(
                "Error fetching photo set:",
                error.response?.data || error.message
            )
        } else {
            console.error("An unexpected error occurred:", error)
        }
        return null
    }
}

export default fetchPhotoSet
