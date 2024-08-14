import { useState } from "react"
import axios from "axios"
import { backendConfig } from "@/config/backendConfig"

interface UpdateCollectionLabelParams {
    idForRenaming: string
    newLabel: string
}

export const useUpdateCollectionLabel = () => {
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    const updateCollectionLabel = async ({
        idForRenaming,
        newLabel,
    }: UpdateCollectionLabelParams): Promise<boolean> => {
        setIsLoading(true)
        setError(null)

        try {
            console.log(backendConfig.updateCollectionLabelUrl, "22rm")
            const response = await axios.patch(
                backendConfig.updateCollectionLabelUrl,
                {
                    idForRenaming,
                    newLabel,
                }
            )

            setIsLoading(false)
            return response.data.success
        } catch (err) {
            setIsLoading(false)
            setError(err instanceof Error ? err.message : "An error occurred")
            return false
        }
    }

    return { updateCollectionLabel, isLoading, error }
}
