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

const counts = {
    noun: 0,
    verb: 0,
    adj: 0,
    name: 0,
    nounWithGender: 0,
    genderlessNoun: 0,
}

function setGender(word) {
    let gender
    if (word.isFeminine || word.isMasculine || word.isBoth) {
        if (word.isFeminine) {
            gender = "feminine"
        } else if (word.isMasculine) {
            gender = "masculine"
        } else if (word.isBoth) {
            gender = "both"
        } else {
            gender = null
        }
    }
    return gender
}

function addWordToDatabase(word, db) {
    const wordType = word.pos
    const text = word.word
    if (wordType === "noun") {
        let gender = setGender(word)
        if (gender) {
            db.createNoun(text, gender)
        } else {
            // handle situations where pos = noun but gender is ? -> it's probably a plural form
            // fakeDb.writeSrcLine(word.srcLine)
            // fakeDb.write(word.senses[0].glosses + "\n")
            if (
                word.senses === undefined ||
                word.senses.length === 0 ||
                word.senses[0].glosses === undefined ||
                word.senses[0].glosses.length === 0
            ) {
                fakeDb.writeSrcLine(word.srcLine)
                return // skip -- it will cause an error.
            }
            const glossesEntry = word.senses[0].glosses[0]
            if (glossesEntry && glossesEntry.startsWith("plural of ")) {
                const noun = glossesEntry.slice(9)
                db.createNounPluralForm(noun, word.senses[0].glosses)
            } else {
                fakeDb.writeSrcLine(word.srcLine)
            }
        }
    } else if (wordType === "verb") {
        db.createVerb(text)
    } else if (wordType === "adj") {
        db.createAdjective(text)
    } else if (wordType === "adv") {
        db.createAdverb(text)
    } else if (wordType === "name") {
        let gender = setGender(word)
        db.createName(text, gender)
    } else {
        db.createOther(text, wordType)
    }
}

const fileStream = fs.createReadStream(FILE_PATH)
const lineTransform = new LineTransform()

const fakeDb = new FakeDB()

fakeDb.openFile()
fakeDb.openProblemsFile()

function main(db) {
    fileStream.pipe(lineTransform)

    lineTransform.on("data", (line) => {
        const jsonObject = JSON.parse(line)
        if (jsonObject.lang_code === "fr") {
            v = v + 1
            const frenchWord = new FrenchWord(jsonObject)
            frenchWord.srcLine = jsonObject

            addWordToDatabase(frenchWord, db)
        }
    })

    lineTransform.on("end", () => {
        // console.log(counts, others)
        fakeDb.closeFile()
        fakeDb.closeProblemsFile()
        console.log("Finished reading the file.")
    })
}
