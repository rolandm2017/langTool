// open les chats words csv
// for each word, check if it's in the nouns table.
// if in nouns table, get gender. pair with gender.
// then, try cramming every one of the 1,900 words into a GPT prompt for translation -> en.

import fs from "fs"
import path from "path"

import dotenv from "dotenv"

// Load environment variables from .env file
dotenv.config()

import DB from "./db.js"

let db

async function openDbConn() {
    try {
        const dbInstance = new DB()
        return await dbInstance.initialize()
    } catch (err) {
        console.error("Failed to initialize DB", err)
        throw err
    }
}

async function closeDbConn() {
    if (db) {
        await db.close()
    }
}

async function lookupWord(word) {
    const foundFromDb = await db.readNoun(word)
    console.log(foundFromDb, "36rm")
    if (foundFromDb && foundFromDb.part_of_speech === "noun") {
        return { isNoun: true, gender: foundFromDb.gender, id: foundFromDb.id }
    }
    return { isNoun: false, gender: null }
}

function formatWord(obj) {
    let withId = false
    if (obj.gender === "m" || obj.gender === "masculine") {
        return withId ? `le ${obj.word},${obj.id}` : `le ${obj.word}`
    } else if (obj.gender === "f" || obj.gender === "feminine") {
        return withId ? `la ${obj.word},${obj.id}` : `la ${obj.word}`
    } else {
        return obj.word
    }
}

const LES_CHATS_WORDS_CSV_PATH = "les_chats_words.csv"
const LES_CHATS_PROCESSED_PATH = "les_chats_processed.csv"

async function processWords() {
    const filePath = path.join("", LES_CHATS_WORDS_CSV_PATH)
    const outputPath = path.join("", LES_CHATS_PROCESSED_PATH)
    const virtue = []

    try {
        // Open DB connection
        db = await openDbConn()

        // Read file
        const data = fs.readFileSync(filePath, "utf8")
        const words = data.split("\r\n") // carriage return
        // console.log(words, "67rm")
        // process.exit()

        // console.log(`Total words: ${words.length}`)
        // console.log("First 10 words:", words.slice(0, 10))
        // console.log("Last 10 words:", words.slice(-10))

        // Process words
        for (const word of words) {
            // console.log(word, "74rm")
            // console.log(word, word.length, `'${word}'`, "74rm")
            // process.exit()
            const dbLookupResult = await lookupWord(word)
            // console.log(word, dbLookupResult, "75rm")
            if (dbLookupResult.isNoun) {
                virtue.push({
                    word,
                    gender: dbLookupResult.gender,
                    type: "noun",
                    id: dbLookupResult.id,
                })
            } else {
                virtue.push({ word, gender: undefined })
            }
        }

        // Write output
        const writeStream = fs.createWriteStream(outputPath)
        let v = 0
        console.log(virtue, "87rm")
        for (const obj of virtue) {
            const formattedWord = formatWord(obj)
            v++
            console.log(formattedWord, "88rm")
            writeStream.write(formattedWord + "\n")
            if (v === 500) {
                break
            }
        }
        writeStream.end()

        console.log(`CSV file has been written to ${outputPath}`)
    } catch (error) {
        console.error("An error occurred:", error)
    } finally {
        await closeDbConn()
    }
}

// Run the main process
processWords().catch(console.error)
