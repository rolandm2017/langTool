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
        <li className="mr-4 px-3 py-1 bg-blue-100 rounded-sm shadow-sm">
            <Link className="text-black" to={toUrl}>
                {children}
            </Link>
        </li>
    )
}

export default NavigationEntry
