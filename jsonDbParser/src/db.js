/*
 * noun - gender
 * ID - should not be the word itself ? or should be
 * definition
 * infinitive - if verb
 * for nouns: "forms" i.e. chien -> chienne -> chiens, chiennes
 * tags?
 *
 */

import pg from "pg"
const { Client } = pg
const { Pool } = pg
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

class DB {
    constructor() {
        console.log(dbConfig)
        this.pool = new Pool(dbConfig)
    }

    async initialize() {
        try {
            await this.pool.query("SELECT NOW()")
            console.log("Connected to the database")
            await this.createTables()
            return this
        } catch (err) {
            console.error("Database connection error", err)
            throw err
        }
    }

    async createTables() {
        const client = await this.pool.connect()
        try {
            await client.query("BEGIN")
            for (let [key, value] of Object.entries(tables)) {
                await client.query(value)
                console.log(`Table '${key}' created successfully.`)
            }
            await client.query("COMMIT")
        } catch (error) {
            await client.query("ROLLBACK")
            console.error("Error creating tables:", error)
            throw error
        } finally {
            client.release()
        }
        // throw new Error("Pausing create teables")
    }

    async batchInsert(words) {
        const client = await this.pool.connect()
        try {
            await client.query("BEGIN")
            for (let word of words) {
                // Implement your insert logic here
                // Use parameterized queries to prevent SQL injection
            }
            await client.query("COMMIT")
        } catch (e) {
            await client.query("ROLLBACK")
            throw e
        } finally {
            client.release()
        }
    }

    // Create a noun
    async createNoun(wordText, gender) {
        const client = await this.pool.connect()
        try {
            await client.query(
                "INSERT INTO nouns (word, gender) VALUES ($1, $2) ON CONFLICT (word) DO NOTHING",
                [wordText, gender]
            )
            console.log(`Noun '${wordText}' processed successfully.`)
        } finally {
            client.release()
        }
    }

    async createNounPluralForm(wordText, pluralForm) {
        const client = await this.pool.connect()
        try {
            await client.query(
                "INSERT INTO nouns (word, plural_of) VALUES ($1, $2) ON CONFLICT (word) DO NOTHING",
                [wordText, pluralForm]
            )
            console.log(`Plural noun '${wordText}' processed successfully.`)
        } finally {
            client.release()
        }
    }

    // Create a verb
    async createVerb(word) {
        const client = await this.pool.connect()
        try {
            await client.query(
                "INSERT INTO verbs (word) VALUES ($1) ON CONFLICT (word) DO NOTHING",
                [word]
            )
            console.log(`Verb '${word}' processed successfully.`)
        } finally {
            client.release()
        }
    }

    // Create an adjective
    async createAdjective(word) {
        const client = await this.pool.connect()
        try {
            await client.query(
                "INSERT INTO adjectives (word) VALUES ($1) ON CONFLICT (word) DO NOTHING",
                [word]
            )
            console.log(`Adjective '${word}' processed successfully.`)
        } finally {
            client.release()
        }
    }

    async createAdverb(word) {
        const client = await this.pool.connect()
        try {
            await client.query(
                "INSERT INTO adverbs (word) VALUES ($1) ON CONFLICT (word) DO NOTHING",
                [word]
            )
            console.log(`Adverb '${word}' processed successfully.`)
        } finally {
            client.release()
        }
    }

    async createName(nameText, gender) {
        const client = await this.pool.connect()
        try {
            await client.query(
                "INSERT INTO names (name, gender) VALUES ($1, $2) ON CONFLICT (name) DO NOTHING",
                [nameText, gender]
            )
            console.log(`Name '${nameText}' processed successfully.`)
        } finally {
            client.release()
        }
    }

    async createOther(wordText, wordType) {
        const client = await this.pool.connect()
        try {
            await client.query(
                "INSERT INTO others (word, type) VALUES ($1, $2) ON CONFLICT (word) DO NOTHING",
                [wordText, wordType]
            )
            console.log(
                `Other word '${wordText}' of type '${wordType}' processed successfully.`
            )
        } finally {
            client.release()
        }
    }
    // Read all nouns
    async readNouns() {
        try {
            const client = await this.pool.connect()

            const result = await client.query("SELECT * FROM nouns")
            return result.rows
        } catch (err) {
            console.error("Error reading nouns", err)
        }
    }

    // Read all verbs
    async readVerbs() {
        try {
            const client = await this.pool.connect()

            const result = await client.query("SELECT * FROM verbs")
            return result.rows
        } catch (err) {
            console.error("Error reading verbs", err)
        }
    }

    // Read all adjectives
    async readAdjectives() {
        try {
            const client = await this.pool.connect()

            const result = await client.query("SELECT * FROM adjectives")
            return result.rows
        } catch (err) {
            console.error("Error reading adjectives", err)
        }
    }

    async readAdverbs() {
        try {
            const client = await this.pool.connect()

            const result = await client.query("SELECT * FROM adverbs")
            return result.rows
        } catch (err) {
            console.error("Error reading adverbs", err)
        }
    }

    async readNames() {
        try {
            const client = await this.pool.connect()

            const result = await client.query("SELECT * FROM names")
            return result.rows
        } catch (err) {
            console.error("Error reading names", err)
        }
    }

    async readNoun(word) {
        const client = await this.pool.connect()
        try {
            const result = await client.query(
                "SELECT * FROM nouns WHERE word = $1",
                [word]
            )
            return result.rows[0] || null
        } finally {
            client.release()
        }
    }

    async readVerb(word) {
        const client = await this.pool.connect()
        try {
            const result = await client.query(
                "SELECT * FROM verbs WHERE word = $1",
                [word]
            )
            return result.rows[0] || null
        } finally {
            client.release()
        }
    }

    async readAdjective(word) {
        const client = await this.pool.connect()
        try {
            const result = await client.query(
                "SELECT * FROM adjectives WHERE word = $1",
                [word]
            )
            return result.rows[0] || null
        } finally {
            client.release()
        }
    }

    async readAdverb(word) {
        const client = await this.pool.connect()
        try {
            const result = await client.query(
                "SELECT * FROM adverbs WHERE word = $1",
                [word]
            )
            return result.rows[0] || null
        } finally {
            client.release()
        }
    }

    async readName(word) {
        const client = await this.pool.connect()
        try {
            const result = await client.query(
                "SELECT * FROM names WHERE word = $1",
                [word]
            )
            return result.rows[0] || null
        } finally {
            client.release()
        }
    }

    async readOther(word) {
        const client = await this.pool.connect()
        try {
            const result = await client.query(
                "SELECT * FROM others WHERE word = $1",
                [word]
            )
            return result.rows[0] || null
        } finally {
            client.release()
        }
    }

    async readWord(word) {
        const tables = [
            "nouns",
            "verbs",
            "adjectives",
            "adverbs",
            "names",
            "others",
        ]
        const client = await this.pool.connect()
        try {
            for (let table of tables) {
                const result = await client.query(
                    `SELECT '${table}' AS type, * FROM ${table} WHERE word = $1`,
                    [word]
                )
                if (result.rows.length > 0) {
                    return result.rows[0]
                }
            }
            return null
        } finally {
            client.release()
        }
    }

    // Close the database connection
    async close() {
        try {
            await this.pool.end()
            console.log("Database connection closed")
        } catch (err) {
            console.error("Error closing database connection", err)
        }
    }
}

export default DB
