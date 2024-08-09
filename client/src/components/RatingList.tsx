import React, { useState } from "react"

import RatingButton from "./RatingButton"

const RatingList: React.FC = () => {
    const [selectedRating, setSelectedRating] = React.useState<string | null>(
        null
    )

    const handleClick = (rating: string) => {
        setSelectedRating(rating)
        console.log(`Selected rating: ${rating}`)
    }

    const ratings = ["1 - Impossible", "2 - Hard", "3 - Medium", "4 - Easy"]

    return (
        <ul className="flex flex-row justify-center items-center">
            {ratings.map((rating) => (
                <RatingButton
                    key={rating}
                    text={rating}
                    onClick={() => handleClick(rating)}
                    isPressed={selectedRating === rating}
                />
            ))}
        </ul>
    )
}

export default RatingList
