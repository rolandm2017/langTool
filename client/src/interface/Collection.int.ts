import { Photo } from "./Photo.int"

export interface Collection {
    id: number
    label: string
    photos: Photo[]
}
