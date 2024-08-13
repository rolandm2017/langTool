import { useState, useEffect } from "react"
import axios, { AxiosError } from "axios"

import { PhotoTextDto } from "@/interface/PhotoTextDto.int"
import { backendConfig } from "@/config/backendConfig"
import { Collection } from "@/interface/interfaces.int"

// Define the return type of our hook
interface UseFetchPhotoTextsResult {
    collections: Collection[]
    loading: boolean
    error: string | null
}

const useGetCollections = (): UseFetchPhotoTextsResult => {
    const [collections, setCollections] = useState<Collection[]>([])
    const [loading, setLoading] = useState<boolean>(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        const fetchPhotoTexts = async () => {
            try {
                setLoading(true)
                const response = await axios.get<Collection[]>(
                    backendConfig.getCollectionsUrl // assume # of entries will be hardcoded.
                )
                console.log(response.data, "29rm")
                setCollections(response.data) // todo: convert  PhotoTextDto -> Collection before it leaves the API
                setError(null)
            } catch (err) {
                const error = err as Error | AxiosError
                setError(
                    axios.isAxiosError(error)
                        ? error.response?.data?.message || error.message
                        : "An unexpected error occurred"
                )
            } finally {
                setLoading(false)
            }
        }

        fetchPhotoTexts()
    }, [])

    return { collections, loading, error }
}

export default useGetCollections
