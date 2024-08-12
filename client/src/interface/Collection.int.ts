import { Photo } from "./Photo.int"

export interface Collection {
    id: string
    label: string
    photos: Photo[]
}
