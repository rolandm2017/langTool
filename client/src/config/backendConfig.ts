const apiUrl = import.meta.env.VITE_API_URL

function makeUrl(route: string): string {
    if (route[0] !== "/") {
        throw new Error("Forgotten leading slash error")
    }
    return apiUrl + "/api" + route
}

// upload
const photoUploadUrl = apiUrl + "/api/photos/upload"
// collections
const getCollectionsUrl = apiUrl + "/api/photocollections"
const getNextCollectionIdUrl = apiUrl + "/api/photocollections/next-id"
const updateCollectionLabelUrl = makeUrl("/photocollections/update-label")
// words
const getWordsListUrl = makeUrl("/words/all")

const backendConfig = {
    apiUrl,
    // words controller
    getWordsListUrl,
    // upload
    photoUploadUrl,
    // collections
    getCollectionsUrl,
    getNextCollectionIdUrl,
    updateCollectionLabelUrl,
}

export { backendConfig }
