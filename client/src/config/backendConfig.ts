const apiUrl = import.meta.env.VITE_API_URL

function makeUrl(route: string): string {
    if (route[0] !== "/") {
        throw new Error("Forgotten leading slash error")
    }
    return apiUrl + "/api" + route
}

const photoUploadUrl = apiUrl + "/api/photos/upload"
const getCollectionsUrl = apiUrl + "/api/photocollections"
const getNextCollectionIdUrl = apiUrl + "/api/photocollections/next-id"
const updateCollectionLabelUrl = makeUrl("/photocollections/update-label")

const backendConfig = {
    apiUrl,
    photoUploadUrl,
    getCollectionsUrl,
    getNextCollectionIdUrl,
    updateCollectionLabelUrl,
}

export { backendConfig }
