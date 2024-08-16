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

export interface Word {
    word: string
    count: number
    isKnown?: boolean
}
