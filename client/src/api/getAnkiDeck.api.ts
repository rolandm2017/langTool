import axios from "axios"

interface AnkiDeckResponse {
    success: boolean
    message: string
    deckFile?: Blob
}

const getAnkiDeck = async (
    idType: "ankiDeckId" | "photoCollectionId",
    id: string
): Promise<File | null> => {
    try {
        const response = await axios.get<AnkiDeckResponse>("/api/anki-deck", {
            params: { [idType]: id },
            responseType: "blob", // Important: This tells Axios to treat the response as binary data
        })

        if (response.data.success && response.data.deckFile) {
            // Convert the Blob to a File object
            const fileName = response.headers["content-disposition"]
                ? response.headers["content-disposition"]
                      .split("filename=")[1]
                      .replace(/"/g, "")
                : "anki-deck.apkg"
            return new File([response.data.deckFile], fileName, {
                type: "application/octet-stream",
            })
        } else {
            console.error("Failed to fetch Anki deck:", response.data.message)
            return null
        }
    } catch (error) {
        if (axios.isAxiosError(error)) {
            console.error(
                "Error fetching Anki deck:",
                error.response?.data || error.message
            )
        } else {
            console.error("An unexpected error occurred:", error)
        }
        return null
    }
}

export default getAnkiDeck
