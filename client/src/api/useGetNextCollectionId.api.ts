import { useState, useEffect } from "react"
import axios from "axios"
import { backendConfig } from "@/config/backendConfig"

// Define the shape of the API response
interface ApiResponse {
    nextCollectionId: number
}

const useGetNextCollectionId = () => {
    const [nextCollectionId, setNextCollectionId] = useState<number | null>(
        null
    )
    const [loading, setLoading] = useState<boolean>(true)
    const [error, setError] = useState<Error | null>(null)

    useEffect(() => {
        const fetchNextCollectionId = async () => {
            try {
                setLoading(true)
                const response = await axios.get<ApiResponse>(
                    backendConfig.getNextCollectionIdUrl
                )
                setNextCollectionId(response.data.nextCollectionId)
                setError(null)
            } catch (err) {
                setError(
                    err instanceof Error
                        ? err
                        : new Error("An unknown error occurred")
                )
                setNextCollectionId(null)
            } finally {
                setLoading(false)
            }
        }

        fetchNextCollectionId()
    }, [])

    return { nextCollectionId, loading, error }
}

export default useGetNextCollectionId
