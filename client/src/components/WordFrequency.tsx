import React from "react"

interface WordFrequencyProps {
    word: string
    count: number
}

const WordFrequency: React.FC<WordFrequencyProps> = ({ word, count }) => {
    return (
        <div style={styles.container}>
            <span style={styles.word}>{word}</span>
            <span style={styles.count}>{count}</span>
        </div>
    )
}

const styles = {
    container: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        padding: "8px 16px",
        backgroundColor: "#f0f0f0",
        borderRadius: "4px",
        margin: "4px 0",
    },
    word: {
        fontWeight: "bold",
        fontSize: "16px",
    },
    count: {
        backgroundColor: "#007bff",
        color: "white",
        borderRadius: "50%",
        padding: "4px 8px",
        fontSize: "14px",
    },
}

export default WordFrequency
