export interface Photo {
    id: string
    fileName: string | undefined
    words: string[]
}

export interface Collection {
    id: string
    label: string
    photos: Photo[]
}
