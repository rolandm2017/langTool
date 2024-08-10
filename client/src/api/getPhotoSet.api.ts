import axios from "axios"

interface PhotoCollectionResponse {
    success: boolean
    message: string
    photoCollection?: string[][]
}

const fetchPhotoCollection = async (
    photoCollectionId: string
): Promise<string[][] | null> => {
    try {
        const response = await axios.get<PhotoCollectionResponse>(
            "/api/photo-sets",
            {
                params: { photoCollectionId },
            }
        )

        if (response.data.success && response.data.photoCollection) {
            return response.data.photoCollection
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

export default fetchPhotoCollection
