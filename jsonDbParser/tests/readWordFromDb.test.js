import { it, describe, test, expect, beforeAll, afterAll } from "vitest"

import DB from "../src/db"
import { wordsToCheck } from "./wordsToCheck"

let dbConn

beforeAll(async () => {
    try {
        const db = new DB()
        dbConn = await db.initialize()
    } catch (err) {
        console.error("Failed to initialize DB", err)
    }
})

afterAll(async () => {
    if (dbConn) {
        await dbConn.close()
    }
})

describe("the words are found in the database with their correct gender", () => {
    it(`finds ${wordsToCheck.length} words with the correct gender`, async () => {
        for (const word of wordsToCheck) {
            const foundFromDb = await dbConn.readNoun(word.word)
            if (foundFromDb) {
                const foundGender = foundFromDb.gender
                expect(foundGender).toEqual(word.gender)
            } else {
                fail(`Word '${word.word}' not found in the database`)
            }
        }
    })
})
