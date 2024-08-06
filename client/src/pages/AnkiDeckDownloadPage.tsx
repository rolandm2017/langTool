import React, { useState, useEffect } from "react"

interface WordFrequency {
    word: string
    count: number
}

const AnkiDeckDownloadPage: React.FC = () => {
    const [wordFrequencies, setWordFrequencies] = useState<WordFrequency[]>([])

    useEffect(() => {
        // Simulating fetching data from an API
        fetchWordFrequencies()
    }, [])

    const fetchWordFrequencies = async () => {
        // This would be replaced with an actual API call
        const mockData: WordFrequency[] = [
            { word: "the", count: 1000 },
            { word: "be", count: 800 },
            { word: "to", count: 750 },
            { word: "of", count: 700 },
            { word: "and", count: 650 },
            { word: "a", count: 600 },
            { word: "in", count: 550 },
            { word: "that", count: 500 },
            { word: "have", count: 450 },
            { word: "I", count: 400 },
            // ... more words ...
            { word: "zephyr", count: 5 },
            { word: "quixotic", count: 4 },
            { word: "ephemeral", count: 3 },
            { word: "serendipity", count: 2 },
            { word: "mellifluous", count: 1 },
        ]
        setWordFrequencies(mockData)
    }

    const getMostFrequent = () => wordFrequencies.slice(0, 10)
    const getLeastFrequent = () => wordFrequencies.slice(-10).reverse()

    const handleDownload = () => {
        // Implement the actual download logic here
        console.log("Downloading Anki deck...")
    }

    const WordList: React.FC<{ words: WordFrequency[]; title: string }> = ({
        words,
        title,
    }) => (
        <div style={styles.wordList}>
            <h3>{title}</h3>
            {words.map((item, index) => (
                <div key={index} style={styles.wordItem}>
                    <span>{item.word}</span>
                    <span style={styles.count}>{item.count}</span>
                </div>
            ))}
        </div>
    )

    return (
        <div style={styles.container}>
            <h1>Anki Deck Preview</h1>
            <button onClick={handleDownload} style={styles.button}>
                Download Anki Deck
            </button>
            <div style={styles.preview}>
                <WordList
                    words={getMostFrequent()}
                    title="10 Most Frequent Words"
                />
                <WordList
                    words={getLeastFrequent()}
                    title="10 Least Frequent Words"
                />
            </div>
        </div>
    )
}

const styles = {
    container: {
        maxWidth: "800px",
        margin: "0 auto",
        padding: "20px",
        fontFamily: "Arial, sans-serif",
    },
    button: {
        backgroundColor: "#4CAF50",
        border: "none",
        color: "white",
        padding: "15px 32px",
        textAlign: "center" as const,
        textDecoration: "none",
        display: "inline-block",
        fontSize: "16px",
        margin: "20px 0",
        cursor: "pointer",
        borderRadius: "4px",
    },
    preview: {
        display: "flex",
        justifyContent: "space-between",
    },
    wordList: {
        width: "45%",
    },
    wordItem: {
        display: "flex",
        justifyContent: "space-between",
        padding: "5px 0",
        borderBottom: "1px solid #eee",
    },
    count: {
        backgroundColor: "#007bff",
        color: "white",
        borderRadius: "50%",
        padding: "2px 8px",
        fontSize: "14px",
    },
}

export default AnkiDeckDownloadPage
