// be able to:
// 1. select from your uploaded collections. be able to group them.
// 2. see a chart showing the # of new words & how many times they're mentioned per set
// 3. see a list of new words + how many times they're mentioned + check, uncheck for epxort

import React, { useState } from "react"
import Button from "./Button"

const validStates = {
    collections: "collections",
    chart: "chart",
    list: "list",
}

const ThreeStateNavToggle = () => {
    const [activeState, setActiveState] = useState(validStates.collections)

    const navItems = [
        { id: "collections", label: "Collections" },
        { id: "chart", label: "Chart" },
        { id: "list", label: "List" },
    ]

    return (
        <nav className="flex space-x-2 p-4 bg-gray-100 rounded-lg">
            {navItems.map((item) => (
                <Button
                    key={item.id}
                    onClick={() => setActiveState(item.id)}
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
