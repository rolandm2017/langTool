import React, { useState } from "react"
import Button from "./Button"
import {
    photoCollectionPageStates,
    PhotoCollectionPageState,
} from "@/util/pageStates"

interface ThreeStateNavToggleProps {
    currentState: PhotoCollectionPageState
    onStateChange: (state: PhotoCollectionPageState) => void
}

const ThreeStateNavToggle: React.FC<ThreeStateNavToggleProps> = ({
    currentState,
    onStateChange,
}) => {
    console.log(currentState, "17rm")
    const [activeState, setActiveState] =
        useState<PhotoCollectionPageState>(currentState)

    const navItems = [
        { id: photoCollectionPageStates.collections, label: "Collections" },
        { id: photoCollectionPageStates.chart, label: "Chart" },
        { id: photoCollectionPageStates.list, label: "List" },
    ]

    const handleStateChange = (newState: PhotoCollectionPageState) => {
        setActiveState(newState)
        onStateChange(newState)
    }

    function decideButtonColor(
        navState: PhotoCollectionPageState,
        itemId: string
    ) {
        // itemId: collections, chart, list
        // navstate: collections, chart, list
        const result = navState === itemId ? "default" : "outline" // blue = "outline"
        return result
    }

    return (
        <nav className="flex space-x-2 p-4 bg-gray-100 rounded-lg">
            {navItems.map((item) => (
                <Button
                    key={item.id}
                    onClick={() => handleStateChange(item.id)}
                    // variant={activeState === item.id ? "default" : "outline"}
                    variant={decideButtonColor(currentState, item.id)}
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
