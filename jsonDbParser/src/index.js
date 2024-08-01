import fs from "fs"
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
        // console.log('v: ', word.word)
        counts[word.pos]++
        if (word.pos === "noun") {
            // console.log(word, '56rm')
            // console.log(counts, 'v2 56rm')
            if (word.isFeminine || word.isMasculine) {
                counts.nounWithGender++
            } else {
                counts.genderlessNoun++
                console.log(word, word.headTemplates[0].args, "65rm")
                inspector.push(word)
                a++
                if (a > 10) {
                    inspector.forEach((entry) => {
                        console.log(entry.word, entry.headTemplateArgs) // todo:
                        // these are the genders fo the words, 9/10x
                        //                         abime [ { '1': 'm' } ]
                        // aberrance [ { '1': 'f' } ]
                        // ablactation [ { '1': 'f' } ]
                        // abjuration [ { '1': 'f' } ]
                        // abjection [ { '1': 'f' } ]
                        // abraxas [ { '1': 'm' } ]
                        // abreuvoir [ { '1': 'm' } ]
                        // abbatial [ { '1': 'm' } ]
                        // abord [ { '1': 'm' } ]
                        // abscission [ { '1': 'f' } ]
                        // patronage [ { '1': 'm' } ]
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

function addWordToDatabase(word) {
    const wordType = word.pos
    if (wordType === "noun") {
        // db.createNoun(word.word, word.)
    } else if (wordType === "verb") {
        //
    } else if (wordType === "adj") {
        //
    } else if (wordType === "adv") {
    } else if (wordType === "name") {
        //
    } else {
        //
    }
}

const fileStream = fs.createReadStream(FILE_PATH)
const lineTransform = new LineTransform()

fileStream.pipe(lineTransform)

lineTransform.on("data", (line) => {
    try {
        const jsonObject = JSON.parse(line)
        // console.log(jsonObject)
        // process.exit()
        if (jsonObject.lang_code === "fr") {
            v = v + 1
            // Process the JSON object
            // console.log(jsonObject);
            const frenchWord = new FrenchWord(jsonObject)
            // console.log(`======= ${v} ====== ${v} ${v} === `)
            // console.log(frenchWord)
            // if (v === 10) process.exit()
            // process.exit()
            // console.log(v, frenchWord.pos)
            // console.log(frenchWord)
            // process.exit()
            tallyWordType(frenchWord)
            // addWordToDatabase(frenchWord)
            // if (v == 100000) {
            //     console.log("Tags: ", frenchWord.forms.map(form => form.tags.join(", ")))
            // console.log(counts, others)
            // process.exit()
            // }
        }
    } catch (err) {
        console.error("Error parsing JSON:", err)
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
    writeGenderlessNounTagsToFile()

    console.log("Finished reading the file.")
})
