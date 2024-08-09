import React from "react"
import { Link } from "react-router-dom"

interface NavigationEntryProps {
    toUrl: string
    children: React.ReactNode
}

const NavigationEntry: React.FC<NavigationEntryProps> = ({
    toUrl,
    children,
}) => {
    return (
        <li className="mr-4 border-2 border-black">
            <Link to={toUrl}>{children}</Link>
        </li>
    )
}

export default NavigationEntry
