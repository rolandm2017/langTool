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
        <li className="mr-4  bg-blue-100 rounded-sm shadow-sm">
            <Link className="px-3 py-1 text-black" to={toUrl}>
                <div>{children}</div>
            </Link>
        </li>
    )
}

export default NavigationEntry
