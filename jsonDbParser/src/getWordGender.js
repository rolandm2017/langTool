import fs from "fs"
import { open } from "fs/promises"
import path from "path"

import { FrenchWord } from "./FrenchWord.js"
import { LineTransform } from "./lineTransform.js"
import { FakeDB } from "./FakeDB.js"

import DB from "./db.js"

const FILE_PATH = ".\\frLangDb\\kaikki.org-dictionary-French-words.jsonl"

let v = 0

const dbPromise = new DB().initialize()

dbPromise
    .then((db) => {
        // do the writes in here
        main(db)
    })
    .catch((err) => {
        console.error("Failed to initialize DB", err)
    })

function getWordGenderFromDb(word) {
    //
}

function writeWordWithGender(word, gender) {
    console.log(`Word "${word}" has gender ${gender}.`)
}

const args = process.argv.slice(2)
let word = null

for (let i = 0; i < args.length; i++) {
    if (args[i].startsWith("--word=")) {
        word = args[i].split("=")[1]
        break
    }
}

if (!word) {
    console.error('Please provide a word using --word="yourword"')
    process.exit(1)
}

console.log(`The word you passed is: ${word}`)

// Add your processing logic here
// For example, let's reverse the word
const reversedWord = word.split("").reverse().join("")
console.log(`The reversed word is: ${reversedWord}`)

export { getWordGenderFromDb }
