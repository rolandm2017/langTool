import React, { useState } from "react"
import { ChevronDown, ChevronUp, Edit2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"

// TODO: 1. use shadcn/ui for everything.
// 2. fix up this collectionsViewer thing: https://claude.ai/chat/24229e39-d95f-4e0f-b5ec-c230e5f95fff

const PhotoCollection = ({ collection, onRename }) => {
    const [isExpanded, setIsExpanded] = useState(false)
    const [isRenaming, setIsRenaming] = useState(false)
    const [newLabel, setNewLabel] = useState(collection.label)

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
                            onChange={(e) => setNewLabel(e.target.value)}
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
                    {collection.photos.map((photo, index) => (
                        <li key={index}>{photo}</li>
                    ))}
                </ul>
            )}
        </div>
    )
}

const PhotoCollections = ({ collections, onRenameCollection }) => {
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
