import pg from "pg"
const { Client } = pg
import tables from "./createTable.sql.js"

import dotenv from "dotenv"

// Load environment variables from .env file
dotenv.config()

// Access environment variables
const dbConfig = {
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_DATABASE,
}

/*
 * noun - gender
 * ID - should not be the word itself ? or should be
 * definition
 * infinitive - if verb
 * for nouns: "forms" i.e. chien -> chienne -> chiens, chiennes
 * tags?
 *
 */

class DB {
    constructor() {
        this.client = new Client(dbConfig)

        this.client
            .connect()
            .then(() => {
                console.log("Connected to the database")
                return this.createTables()
            })
            .catch((err) => console.error("Database connection error", err))
    }

    async createTables() {
        const tableDefinitions = [...tables]
        for (const table of tableDefinitions) {
            try {
                await this.client.query(table)
                console.log(`Table '${table.name}' created successfully.`)
            } catch (err) {
                console.error(`Error creating table '${table.name}'`, err)
            }
        }
    }

    // Create a noun
    async createNoun(wordText, gender) {
        try {
            await this.client.query(
                "INSERT INTO nouns (word, gender) VALUES ($1, $2)",
                [wordText, gender]
            )
            console.log(`Noun '${wordText}' created successfully.`)
        } catch (err) {
            console.error("Error creating noun", err)
            throw err
        }
    }

    async createNounPluralForm(wordText, pluralForm) {
        try {
            await this.client.query(
                "INSERT INTO nouns (word, plural_of) VALUES ($1, $2)",
                [wordText, pluralForm]
            )
            console.log(`Plural noun '${wordText}' created successfully.`)
        } catch (err) {
            console.error("Error creating plural noun", err)
            throw err
        }
    }

    // Create a verb
    async createVerb(word) {
        try {
            await this.client.query("INSERT INTO verbs (word) VALUES ($1)", [
                word,
            ])
            console.log(`Verb '${word}' created successfully.`)
        } catch (err) {
            console.error("Error creating verb", err)
            throw err
        }
    }

    // Create an adjective
    async createAdjective(word) {
        try {
            await this.client.query(
                "INSERT INTO adjectives (word) VALUES ($1, $2)",
                [word]
            )
            console.log(`Adjective '${word}' created successfully.`)
        } catch (err) {
            console.error("Error creating adjective", err)
            throw err
        }
    }

    async createAdverb(word) {
        try {
            await this.client.query(
                "INSERT INTO adverbs (word) VALUES ($1, $2)",
                [word]
            )
            console.log(`Adjective '${word}' created successfully.`)
        } catch (err) {
            console.error("Error creating adjective", err)
            throw err
        }
    }

    async createName(nameText, gender) {
        try {
            await this.client.query(
                "INSERT INTO names (word, gender) VALUES ($1, $2)",
                [nameText, gender]
            )
            console.log(`Name '${nameText}' created successfully.`)
        } catch (err) {
            console.error("Error creating name", err)
            throw err
        }
    }

    async createOther(wordText, wordType) {
        try {
            await this.client.query(
                "INSERT INTO others (word, type) VALUES ($1, $2)",
                [wordText, wordType]
            )
            console.log(
                `Other word '${wordText}' of type '${wordType}' created successfully.`
            )
        } catch (err) {
            console.error("Error creating name", err)
            throw err
        }
    }

    // Read all nouns
    async readNouns() {
        try {
            const result = await this.client.query("SELECT * FROM nouns")
            return result.rows
        } catch (err) {
            console.error("Error reading nouns", err)
        }
    }

    // Read all verbs
    async readVerbs() {
        try {
            const result = await this.client.query("SELECT * FROM verbs")
            return result.rows
        } catch (err) {
            console.error("Error reading verbs", err)
        }
    }

    // Read all adjectives
    async readAdjectives() {
        try {
            const result = await this.client.query("SELECT * FROM adjectives")
            return result.rows
        } catch (err) {
            console.error("Error reading adjectives", err)
        }
    }

    async readAdverbs() {
        try {
            const result = await this.client.query("SELECT * FROM adverbs")
            return result.rows
        } catch (err) {
            console.error("Error reading adverbs", err)
        }
    }

    async readNames() {
        try {
            const result = await this.client.query("SELECT * FROM names")
            return result.rows
        } catch (err) {
            console.error("Error reading names", err)
        }
    }

    // Close the database connection
    async close() {
        try {
            await this.client.end()
            console.log("Database connection closed")
        } catch (err) {
            console.error("Error closing database connection", err)
        }
    }
}

export default DB
