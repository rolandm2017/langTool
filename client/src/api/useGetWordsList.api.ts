import { useState, useEffect } from "react"
import axios from "axios"
import { backendConfig } from "@/config/backendConfig"

export interface Word {
    word: string
    count: number
    isKnown?: boolean
}

export const useGetWordsList = () => {
    const [retrievedWords, setRetrievedWords] = useState<Word[]>([])
    const [isLoading, setIsLoading] = useState<boolean>(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        const fetchWords = async () => {
            try {
                setIsLoading(true)
                const response = await axios.get<Word[]>(
                    backendConfig.getWordsListUrl
                )
                setRetrievedWords(response.data)
                setError(null)
            } catch (err) {
                setError("Failed to fetch words list")
                console.error("Error fetching words list:", err)
            } finally {
                setIsLoading(false)
            }
        }

        fetchWords()
    }, [])

    return { retrievedWords, isLoading, error }
}
