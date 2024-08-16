import React, { useEffect, useState } from "react"
import { Word } from "@/interface/interfaces.int"
import { useGetWordsList } from "@/api/useGetWordsList.api"

const WordList: React.FC = () => {
    const [words, setWords] = useState<Word[]>([
        { word: "de", isKnown: true, count: 438 },
        { word: "la", isKnown: true, count: 372 },
        { word: "le", isKnown: true, count: 311 },
        { word: "il", count: 237 },
        { word: "les", count: 204 },
        { word: "et", isKnown: true, count: 196 },
        { word: "à", count: 167 },
        { word: "commissaire", count: 156 },
        { word: "un", isKnown: true, count: 155 },
        { word: "une", count: 145 },
    ])

    const { retrievedWords, isLoading } = useGetWordsList()

    useEffect(() => {
        const loaded = !isLoading
        if (loaded) {
            setWords(retrievedWords)
        }
    }, [isLoading])

    const [confirmDelete, setConfirmDelete] = useState<string | null>(null)

    const handleIgnore = (word: string) => {
        // In a real application, you might want to add this word to an "ignored" list
        console.log(`Ignored word: ${word}`)
    }

    const handleDelete = (word: string) => {
        setWords(words.filter((w) => w.word !== word))
        setConfirmDelete(null)
    }

    return (
        <div style={styles.container}>
            <h2>Word List</h2>
            <ul style={styles.list}>
                {words.map((word) => (
                    <li key={word.word} style={styles.listItem}>
                        <span style={styles.word}>{word.word}</span>
                        <span style={styles.count}>{word.count}</span>
                        <span style={styles.count}>
                            {word.isKnown ? "Known" : ""}
                            {""}
                        </span>
                        <button
                            style={styles.button}
                            onClick={() => handleIgnore(word.word)}
                        >
                            Ignore
                        </button>
                        <button
                            style={styles.button}
                            onClick={() => setConfirmDelete(word.word)}
                        >
                            Delete
                        </button>
                        {confirmDelete === word.word && (
                            <div style={styles.confirmDialog}>
                                Are you sure you want to delete "{word.word}"?
                                <button
                                    style={styles.confirmButton}
                                    onClick={() => handleDelete(word.word)}
                                >
                                    Yes
                                </button>
                                <button
                                    style={styles.confirmButton}
                                    onClick={() => setConfirmDelete(null)}
                                >
                                    No
                                </button>
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    )
}

const styles = {
    container: {
        fontFamily: "Arial, sans-serif",
        maxWidth: "600px",
        margin: "0 auto",
    },
    list: {
        listStyleType: "none",
        padding: 0,
    },
    listItem: {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        padding: "10px",
        borderBottom: "1px solid #eee",
    },
    word: {
        fontWeight: "bold" as const,
        width: "100px",
    },
    count: {
        width: "50px",
    },
    button: {
        marginLeft: "10px",
        padding: "5px 10px",
        cursor: "pointer",
    },
    confirmDialog: {
        position: "absolute" as const,
        right: "10px",
        backgroundColor: "#f0f0f0",
        padding: "10px",
        borderRadius: "5px",
        boxShadow: "0 2px 5px rgba(0,0,0,0.2)",
    },
    confirmButton: {
        marginLeft: "5px",
        padding: "3px 8px",
        cursor: "pointer",
    },
}

export default WordList
