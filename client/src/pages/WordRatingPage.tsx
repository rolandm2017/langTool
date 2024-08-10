import React, { useState, useEffect, CSSProperties } from "react"
import RatingList from "../components/RatingList"

import usePostDifficultyRating from "../api/postDifficultyRating.api"

type Difficulty = "impossible" | "hard" | "medium" | "easy"

interface Word {
    text: string
    wordId: number
}

const WordRatingPage: React.FC = () => {
    const [word, setWord] = useState<Word>({ text: "", wordId: 0 })
    const [rating, setRating] = useState<Difficulty | null>(null)

    const words: Word[] = [
        { text: "apple", wordId: 1 },
        { text: "banana", wordId: 2 },
        { text: "chromatic", wordId: 3 },
        { text: "dexterity", wordId: 4 },
        { text: "ephemeral", wordId: 5 },
        { text: "fandango", wordId: 6 },
        { text: "galvanize", wordId: 7 },
        { text: "harmonious", wordId: 8 },
        { text: "ineffable", wordId: 9 },
        { text: "juxtapose", wordId: 10 },
        { text: "kaleidoscope", wordId: 11 },
        { text: "labyrinthine", wordId: 12 },
        { text: "mellifluous", wordId: 13 },
        { text: "nebulous", wordId: 14 },
        { text: "obfuscate", wordId: 15 },
    ]

    const { postDifficultyRating, isLoading, error } = usePostDifficultyRating()

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
        console.log("key: ", key)

        const keyAsInt = parseInt(key, 10)
        if (!keyAsInt) {
            throw new Error("Key not from valid set")
        }

        postDifficultyRating(word.wordId, keyAsInt)

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
                <h2 style={styles.word}>{word.text}</h2>
            </div>
            <div style={styles.instructionsContainer}>
                <p style={styles.instructions}>
                    Rate this word using your keyboard:
                </p>
                <RatingList />
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

const styles: { [key: string]: CSSProperties } = {
    container: {
        padding: "40px 0 0 0",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "start",
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
        textAlign: "center",
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
        fontWeight: "bold",
        color: "white",
    },
}

export default WordRatingPage
