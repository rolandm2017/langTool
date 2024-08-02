import fs from "fs"
import { open } from "fs/promises"
import path from "path"

import { FrenchWord } from "./FrenchWord.js"
import { LineTransform } from "./lineTransform.js"

import DB from "./db.js"

const FILE_PATH = ".\\frLangDb\\kaikki.org-dictionary-French-words.jsonl"

let v = 0

const db = new DB()

const counts = {
    noun: 0,
    verb: 0,
    adj: 0,
    name: 0,
    nounWithGender: 0,
    genderlessNoun: 0,
}

const tagsFromGenderlessNouns = []

const others = {}

const inspector = []

let a = 0

function tallyWordType(word) {
    if (word.pos in counts) {
        counts[word.pos]++
        if (word.pos === "noun") {
            if (word.isFeminine || word.isMasculine) {
                counts.nounWithGender++
            } else {
                counts.genderlessNoun++
                inspector.push(word)
                a++
                if (a > 10) {
                    inspector.forEach((entry) => {
                        console.log(entry.word, entry.headTemplateArgs) // todo:
                    })
                    process.exit()
                }
            }
        }
    } else {
        // if (word.pos === 'name') {
        // console.log('is name: ', word.word)
        // }
        if (others[word.pos] === undefined) {
            others[word.pos] = 1
        } else {
            others[word.pos]++
        }
    }
}

let fakeDbProblems = 0

class FakeDB {
    constructor() {
        this.filePath = "./myFakeDb.txt"
        this.problemLinesPath = "./skippedEntries.jsonl"
        this.fileHandle = fs.openSync(this.filePath, "w")
        this.problemLinesHandle = fs.openSync(this.problemLinesPath, "w")
    }

    openProblemsFile() {
        this.problemFileHandle = fs.openSync(this.problemLinesPath, "w")
    }

    openFile() {
        this.fileHandle = fs.openSync(this.filePath, "w")
    }

    createNoun(word, gender, originalWordObject) {
        console.log(`writing ${word} with ${gender}`)
        if (gender === undefined) {
            console.log(originalWordObject, "92rm")
            this.writeSrcLine(originalWordObject.srcLine)
            // throw new Error("Undefined gender")
        }
        this.write(`${word}: ${gender}\n`)
    }

    writeSrcLine(srcLine) {
        fakeDbProblems++
        if (srcLine == undefined) {
            console.log(srcLine, "110rm")
            throw new Error("src line == undefined")
        }
        if (!this.problemLinesHandle) {
            throw new Error(
                "Problems File is not opened. Call openProblemsFile() first."
            )
        }
        const content = JSON.stringify(srcLine) + "\n"
        fs.writeSync(this.problemLinesHandle, content)
    }

    write(content) {
        if (!this.fileHandle) {
            throw new Error("File is not opened. Call open() first.")
        }
        fs.writeSync(this.fileHandle, content)
    }

    closeFile() {
        if (this.fileHandle !== null) {
            fs.closeSync(this.fileHandle)
            this.fileHandle = null
        }
    }

    closeProblemsFile() {
        if (this.problemLinesHandle !== null) {
            fs.closeSync(this.problemLinesHandle)
            this.problemLinesHandle = null
        }
    }
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

function addWordToDatabase(word) {
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

function main() {
    fileStream.pipe(lineTransform)

    lineTransform.on("data", (line) => {
        const jsonObject = JSON.parse(line)
        // console.log(jsonObject)
        // process.exit()
        if (jsonObject.lang_code === "fr") {
            v = v + 1
            // Process the JSON object
            // console.log(jsonObject);
            const frenchWord = new FrenchWord(jsonObject)
            // console.log("setting line ", jsonObject)
            // process.exit()
            frenchWord.srcLine = jsonObject
            // console.log(frenchWord.srcLine, "198rm")
            // process.exit()
            // console.log(`======= ${v} ====== ${v} ${v} === `)
            // console.log(frenchWord)
            // if (v === 10) process.exit()
            // process.exit()
            // console.log(v, frenchWord.pos)
            // console.log(frenchWord)
            // process.exit()
            // tallyWordType(frenchWord)
            addWordToDatabase(frenchWord)
            if (fakeDbProblems === 250) {
                fakeDb.closeFile()
                fakeDb.closeProblemsFile()
                console.log("Reached 200 problem lines")
                process.exit()
            }
            // if (v == 100000) {
            //     console.log("Tags: ", frenchWord.forms.map(form => form.tags.join(", ")))
            // console.log(counts, others)
            // process.exit()
            // }
        }
    })

    function writeGenderlessNounTagsToFile() {
        const cwd = process.cwd()
        console.log("Current working directory:", cwd)

        // Create the file path in the current working directory
        const filePath = path.join(cwd, "tagsOut.txt")

        // Create a string with each entry separated by a newline
        const data = tagsFromGenderlessNouns.join("\n")

        // Write the data to the file
        fs.writeFile(filePath, data, "utf8", (err) => {
            if (err) {
                console.error("Error writing to file", err)
            } else {
                console.log("File written successfully at", filePath)
            }
        })
    }

    lineTransform.on("end", () => {
        console.log(counts, others)
        // process.exit()
        // writeGenderlessNounTagsToFile()
        fakeDb.closeFile()
        fakeDb.closeProblemsFile()
        console.log("Finished reading the file.")
    })
}

main()
