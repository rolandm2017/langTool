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

function writeGenderlessNounTagsToFile() {
    const cwd = process.cwd()
    console.log("Current working directory:", cwd)

    const filePath = path.join(cwd, "tagsOut.txt") // Create the file path in the current working directory

    const data = tagsFromGenderlessNouns.join("\n") // Create a string with each entry separated by a newline

    // Write the data to the file
    fs.writeFile(filePath, data, "utf8", (err) => {
        if (err) {
            console.error("Error writing to file", err)
        } else {
            console.log("File written successfully at", filePath)
        }
    })
}
