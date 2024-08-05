import React from "react"
import WordFrequency from "./WordFrequency"

interface WordFrequencies {
    [word: string]: number
}

const PhotoWordFrequencies: React.FC = () => {
    // This would typically come from your photo analysis logic
    const wordFrequencies: WordFrequencies = {
        cat: 5,
        dog: 3,
        tree: 7,
        car: 2,
    }

    return (
        <div>
            <h2>Word Frequencies in Photos</h2>
            {Object.entries(wordFrequencies).map(([word, count]) => (
                <WordFrequency key={word} word={word} count={count} />
            ))}
        </div>
    )
}

export default PhotoWordFrequencies
