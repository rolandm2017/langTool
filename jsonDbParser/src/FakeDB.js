import fs from "fs"
import { open } from "fs/promises"
import path from "path"

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

export { FakeDB }
