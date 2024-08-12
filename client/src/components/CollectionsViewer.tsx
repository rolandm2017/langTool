import React, { useState } from "react"
import { ChevronDown, ChevronUp, Edit2 } from "lucide-react"

import { Collection } from "@/interface/Collection.int"
import { Photo } from "@/interface/Photo.int"
import { Button } from "./ui/button"
import { Input } from "./ui/input"

interface PhotoCollectionProps {
    collection: Collection
    onRename: (id: string, newLabel: string) => void
}

interface PhotoCollectionsProps {
    collections: Collection[]
    onRenameCollection: (id: string, newLabel: string) => void
}

const PhotoCollection: React.FC<PhotoCollectionProps> = ({
    collection,
    onRename,
}) => {
    const [isExpanded, setIsExpanded] = useState<boolean>(false)
    const [isRenaming, setIsRenaming] = useState<boolean>(false)
    const [newLabel, setNewLabel] = useState<string>(collection.label)

    const handleRename = () => {
        onRename(collection.id, newLabel)
        setIsRenaming(false)
    }

    return (
        <div className="border rounded-lg p-4 mb-4">
            <div className="flex items-center justify-between mb-2">
                {isRenaming ? (
                    <div className="flex items-center space-x-2">
                        <Input
                            value={newLabel}
                            onChange={(
                                e: React.ChangeEvent<HTMLInputElement>
                            ) => setNewLabel(e.target.value)}
                            className="w-48"
                        />
                        <Button onClick={handleRename} size="sm">
                            Save
                        </Button>
                    </div>
                ) : (
                    <h3 className="text-lg font-semibold">
                        {collection.label}
                    </h3>
                )}
                <div className="flex items-center space-x-2">
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => setIsRenaming(!isRenaming)}
                    >
                        <Edit2 className="w-4 h-4" />
                    </Button>
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => setIsExpanded(!isExpanded)}
                    >
                        {isExpanded ? (
                            <ChevronUp className="w-4 h-4" />
                        ) : (
                            <ChevronDown className="w-4 h-4" />
                        )}
                    </Button>
                </div>
            </div>
            {isExpanded && (
                <ul className="list-disc pl-6">
                    {collection.photos.map((photo) => (
                        <li key={photo.id}>{photo.url}</li>
                    ))}
                </ul>
            )}
        </div>
    )
}

const PhotoCollections: React.FC<PhotoCollectionsProps> = ({
    collections,
    onRenameCollection,
}) => {
    return (
        <div className="space-y-4">
            <h2 className="text-2xl font-bold mb-4">Photo Collections</h2>
            {collections.map((collection) => (
                <PhotoCollection
                    key={collection.id}
                    collection={collection}
                    onRename={onRenameCollection}
                />
            ))}
        </div>
    )
}

export default PhotoCollections
