import React, { useState, useEffect, useCallback } from "react"

interface Word {
    id: number
    text: string
}

const WordKnowledgeTest: React.FC = () => {
    const [words, setWords] = useState<Word[]>([
        { id: 1, text: "bonjour" },
        { id: 2, text: "merci" },
        { id: 3, text: "au revoir" },
        { id: 4, text: "s'il vous plaît" },
        { id: 5, text: "excusez-moi" },
        // Add more words as needed
    ])

    const [currentWordIndex, setCurrentWordIndex] = useState(0)
    const [result, setResult] = useState<string | null>(null)

    const handleKeyPress = useCallback((event: KeyboardEvent) => {
        if (event.key === "ArrowLeft") {
            // Left arrow: Not known
            setResult("Not Known")
            moveToNextWord()
        } else if (event.key === "ArrowRight") {
            // Right arrow: Known
            setResult("Known")
            moveToNextWord()
        }
    }, [])

    const moveToNextWord = () => {
        setTimeout(() => {
            setCurrentWordIndex((prevIndex) => (prevIndex + 1) % words.length)
            setResult(null)
        }, 1000) // Wait for 1 second before moving to the next word
    }

    useEffect(() => {
        window.addEventListener("keydown", handleKeyPress)

        return () => {
            window.removeEventListener("keydown", handleKeyPress)
        }
    }, [handleKeyPress])

    return (
        <div style={styles.container}>
            <h1 style={styles.title}>Word Knowledge Test</h1>
            <div style={styles.wordContainer}>
                <h2 style={styles.word}>{words[currentWordIndex].text}</h2>
            </div>
            <div style={styles.instructionsContainer}>
                <p style={styles.instructions}>
                    Press ← for "Not Known" or → for "Known"
                </p>
            </div>
            {result && (
                <div
                    style={{
                        ...styles.result,
                        backgroundColor:
                            result === "Known" ? "#4CAF50" : "#f44336",
                    }}
                >
                    {result}
                </div>
            )}
            <div style={styles.progress}>
                Word {currentWordIndex + 1} of {words.length}
            </div>
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
    result: {
        padding: "10px 20px",
        borderRadius: "5px",
        fontSize: "1.2em",
        fontWeight: "bold" as const,
        color: "white",
        marginBottom: "20px",
    },
    progress: {
        fontSize: "1em",
        color: "#666",
    },
}

export default WordKnowledgeTest
