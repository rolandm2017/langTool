import React from "react"

interface RatingButtonProps {
    text: string
    onClick?: () => void
    isPressed: boolean
}

const RatingButton: React.FC<RatingButtonProps> = ({
    text,
    onClick,
    isPressed,
}) => {
    return (
        <li>
            <button
                onClick={onClick}
                className={`mr-2 px-4 py-2 rounded transition-colors ${
                    isPressed
                        ? "bg-blue-700 text-white"
                        : "bg-blue-500 text-white hover:bg-blue-600"
                }`}
            >
                {text}
            </button>
        </li>
    )
}

export default RatingButton
