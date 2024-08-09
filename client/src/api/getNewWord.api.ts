import { useState, useCallback } from "react"

interface WordResponse {
    frenchText: string
}

interface UseFetchNewWordResult {
    word: string | null
    fetchNewWord: () => Promise<void>
    isLoading: boolean
    error: string | null
}

const useGetNewWord = (): UseFetchNewWordResult => {
    const [word, setWord] = useState<string | null>(null)
    const [isLoading, setIsLoading] = useState<boolean>(false)
    const [error, setError] = useState<string | null>(null)

    const fetchNewWord = useCallback(async (): Promise<void> => {
        setIsLoading(true)
        setError(null)

        try {
            const response = await fetch("/api/new-word", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            })

            if (!response.ok) {
                throw new Error("Failed to fetch new word")
            }

            const data: WordResponse = await response.json()
            setWord(data.frenchText)
        } catch (err) {
            setError(
                err instanceof Error ? err.message : "An unknown error occurred"
            )
        } finally {
            setIsLoading(false)
        }
    }, [])

    return { word, fetchNewWord, isLoading, error }
}

export default useGetNewWord
