import { useState } from "react"

const usePostDifficultyRating = () => {
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState(null)

    const postDifficultyRating = async (
        difficultyRating: number,
        cardId: number
    ) => {
        setIsLoading(true)
        setError(null)

        try {
            const response = await fetch("/api/difficulty-ratings", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    difficultyRating,
                    cardId,
                }),
            })

            if (!response.ok) {
                throw new Error("Failed to post difficulty rating")
            }

            const data = await response.json()
            setIsLoading(false)
            return data
        } catch (err) {
            setError(
                err instanceof Error ? err.message : "An unknown error occurred"
            )
            throw err
        } finally {
            setIsLoading(false)
        }
    }

    return { postDifficultyRating, isLoading, error }
}

export default usePostDifficultyRating
