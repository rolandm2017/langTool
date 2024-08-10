import React, { CSSProperties } from "react"

const LandingPage: React.FC = () => {
    return (
        <div style={styles.container}>
            <div>
                <h1 style={styles.mainTitle}>Photo to Anki Deck Converter</h1>
            </div>
            <div style={styles.contentContainer}>
                <div>
                    <h2 style={styles.subtitle}>
                        Hand crafting decks is a chore
                    </h2>
                </div>
                <div>
                    <h2 style={styles.subtitle}>
                        Stop wasting hours and hours
                    </h2>
                </div>
                <div>
                    <h2 style={styles.subtitle}>
                        Let the software do it for you
                    </h2>
                </div>
                <div>
                    <h2 style={styles.subtitle}>
                        Generate decks of only unknown words
                    </h2>
                </div>
                <div>
                    <button style={styles.button}>Sign up today</button>
                </div>
            </div>
        </div>
    )
}

const styles: { [key: string]: CSSProperties } = {
    container: {
        margin: "40px 0 0 0",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "start",
        minHeight: "100vh",
        padding: "20px",
        textAlign: "center",
        backgroundColor: "#f0f0f0",
        fontFamily: "Arial, sans-serif",
    },
    mainTitle: {
        fontSize: "2.5em",
        marginBottom: "40px",
        color: "#333",
    },
    contentContainer: {
        maxWidth: "600px",
    },
    subtitle: {
        fontSize: "1.5em",
        margin: "20px 0",
        color: "#555",
    },
    button: {
        backgroundColor: "#4CAF50",
        border: "none",
        color: "white",
        padding: "15px 32px",
        textDecoration: "none",
        display: "inline-block",
        fontSize: "16px",
        margin: "20px 2px",
        cursor: "pointer",
        borderRadius: "4px",
        transition: "background-color 0.3s",
    },
}

export default LandingPage
