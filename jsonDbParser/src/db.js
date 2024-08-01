import pg from 'pg'
const { Client } = pg
import tables from './createTable.sql.js'

import dotenv from 'dotenv'

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
                console.log('Connected to the database')
                return this.createTables()
            })
            .catch((err) => console.error('Database connection error', err))
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
                'INSERT INTO nouns (word, gender) VALUES ($1, $2)',
                [wordText, gender]
            )
            console.log(`Noun '${wordText}' created successfully.`)
        } catch (err) {
            console.error('Error creating noun', err)
        }
    }

    // Create a verb
    async createVerb(word) {
        try {
            await this.client.query('INSERT INTO verbs (word) VALUES ($1)', [
                word,
            ])
            console.log(`Verb '${word}' created successfully.`)
        } catch (err) {
            console.error('Error creating verb', err)
        }
    }

    // Create an adjective
    async createAdjective(word, definition) {
        try {
            await this.client.query(
                'INSERT INTO adjectives (word, definition) VALUES ($1, $2)',
                [word, definition]
            )
            console.log(`Adjective '${word}' created successfully.`)
        } catch (err) {
            console.error('Error creating adjective', err)
        }
    }

    // Read all nouns
    async readNouns() {
        try {
            const result = await this.client.query('SELECT * FROM nouns')
            return result.rows
        } catch (err) {
            console.error('Error reading nouns', err)
        }
    }

    // Read all verbs
    async readVerbs() {
        try {
            const result = await this.client.query('SELECT * FROM verbs')
            return result.rows
        } catch (err) {
            console.error('Error reading verbs', err)
        }
    }

    // Read all adjectives
    async readAdjectives() {
        try {
            const result = await this.client.query('SELECT * FROM adjectives')
            return result.rows
        } catch (err) {
            console.error('Error reading adjectives', err)
        }
    }

    // Close the database connection
    async close() {
        try {
            await this.client.end()
            console.log('Database connection closed')
        } catch (err) {
            console.error('Error closing database connection', err)
        }
    }
}

export default DB
