import React, { useState, useEffect } from "react"

type Difficulty = "impossible" | "hard" | "medium" | "easy"

const WordRatingPage: React.FC = () => {
    const [word, setWord] = useState<string>("")
    const [rating, setRating] = useState<Difficulty | null>(null)

    // Simulated word list - replace with your actual word source
    const words = ["apple", "banana", "chromatic", "dexterity", "ephemeral"]

    useEffect(() => {
        // Load a random word when the component mounts
        loadNewWord()

        // Set up the keyboard event listener
        window.addEventListener("keydown", handleKeyPress)

        // Clean up the event listener when the component unmounts
        return () => {
            window.removeEventListener("keydown", handleKeyPress)
        }
    }, [])

    const loadNewWord = () => {
        const randomIndex = Math.floor(Math.random() * words.length)
        setWord(words[randomIndex])
        setRating(null)
    }

    const handleKeyPress = (event: KeyboardEvent) => {
        const key = event.key
        let newRating: Difficulty | null = null

        switch (key) {
            case "1":
                newRating = "impossible"
                break
            case "2":
                newRating = "hard"
                break
            case "3":
                newRating = "medium"
                break
            case "4":
                newRating = "easy"
                break
            default:
                return // Ignore other keys
        }

        setRating(newRating)

        // Load a new word after a short delay
        setTimeout(loadNewWord, 1000)
    }

    const getRatingColor = (rating: Difficulty): string => {
        switch (rating) {
            case "impossible":
                return "red"
            case "hard":
                return "orange"
            case "medium":
                return "yellow"
            case "easy":
                return "green"
        }
    }

    return (
        <div style={styles.container}>
            <h1 style={styles.title}>Word Rating</h1>
            <div style={styles.wordContainer}>
                <h2 style={styles.word}>{word}</h2>
            </div>
            <div style={styles.instructionsContainer}>
                <p style={styles.instructions}>
                    Rate this word using your keyboard:
                </p>
                <ul style={styles.ratingList}>
                    <li>1 - Impossible</li>
                    <li>2 - Hard</li>
                    <li>3 - Medium</li>
                    <li>4 - Easy</li>
                </ul>
            </div>
            {rating && (
                <div
                    style={{
                        ...styles.rating,
                        backgroundColor: getRatingColor(rating),
                    }}
                >
                    You rated this word as: {rating}
                </div>
            )}
        </div>
    )
}

const styles = {
    container: {
        display: "flex",
        flexDirection: "column" as const,
        alignItems: "center",
        justifyContent: "center",
        height: "100vh",
        fontFamily: "Arial, sans-serif",
        backgroundColor: "#f0f0f0",
    },
    title: {
        fontSize: "2.5em",
        marginBottom: "20px",
    },
    wordContainer: {
        backgroundColor: "white",
        padding: "20px 40px",
        borderRadius: "10px",
        boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
        marginBottom: "20px",
    },
    word: {
        fontSize: "3em",
        margin: 0,
    },
    instructionsContainer: {
        textAlign: "center" as const,
        marginBottom: "20px",
    },
    instructions: {
        fontSize: "1.2em",
        marginBottom: "10px",
    },
    ratingList: {
        listStyleType: "none",
        padding: 0,
        display: "flex",
        justifyContent: "space-around",
        width: "100%",
    },
    rating: {
        padding: "10px 20px",
        borderRadius: "5px",
        fontSize: "1.2em",
        fontWeight: "bold" as const,
        color: "white",
    },
}

export default WordRatingPage
