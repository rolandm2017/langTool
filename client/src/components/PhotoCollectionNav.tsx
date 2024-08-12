import React, { useState } from "react"
import Button from "./Button"
import {
    photoCollectionPageStates,
    PhotoCollectionPageState,
} from "@/util/pageStates"

interface ThreeStateNavToggleProps {
    onStateChange: (state: PhotoCollectionPageState) => void
}

const ThreeStateNavToggle: React.FC<ThreeStateNavToggleProps> = ({
    onStateChange,
}) => {
    const [activeState, setActiveState] = useState<PhotoCollectionPageState>(
        photoCollectionPageStates.collections
    )

    const navItems = [
        { id: photoCollectionPageStates.collections, label: "Collections" },
        { id: photoCollectionPageStates.chart, label: "Chart" },
        { id: photoCollectionPageStates.list, label: "List" },
    ]

    const handleStateChange = (newState: PhotoCollectionPageState) => {
        setActiveState(newState)
        onStateChange(newState)
    }

    return (
        <nav className="flex space-x-2 p-4 bg-gray-100 rounded-lg">
            {navItems.map((item) => (
                <Button
                    key={item.id}
                    onClick={() => handleStateChange(item.id)}
                    variant={activeState === item.id ? "default" : "outline"}
                    className={`transition-all ${
                        activeState === item.id ? "shadow-md" : ""
                    }`}
                >
                    {item.label}
                </Button>
            ))}
        </nav>
    )
}

export default ThreeStateNavToggle
