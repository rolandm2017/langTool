import fs from "fs"

import { it, describe, test, expect } from "vitest"

import { LineTransform } from "../src/lineTransform"
import { FrenchWord } from "../src/FrenchWord"

const FILE_PATH = ".\\tests\\testMaterial.jsonl"

test("basic addition", () => {
    expect(1 + 1).toBe(2)
})

// prettier-ignore
const x = {"pos": "noun", "head_templates": [{"name": "fr-noun", "args": {"1": "m"}, "expansion": "abime m (plural abimes)"}], "forms": [{"form": "abimes", "tags": ["plural"]}], "sounds": [{"ipa": "/a.bim/"}, {"audio": "Fr-Abime.oga", "ogg_url": "https://upload.wikimedia.org/wikipedia/commons/4/4b/Fr-Abime.oga", "mp3_url": "https://upload.wikimedia.org/wikipedia/commons/transcoded/4/4b/Fr-Abime.oga/Fr-Abime.oga.mp3"}, {"audio": "LL-Q150 (fra)-DSwissK-abime.wav", "ogg_url": "https://upload.wikimedia.org/wikipedia/commons/transcoded/c/c8/LL-Q150_%28fra%29-DSwissK-abime.wav/LL-Q150_%28fra%29-DSwissK-abime.wav.ogg", "mp3_url": "https://upload.wikimedia.org/wikipedia/commons/transcoded/c/c8/LL-Q150_%28fra%29-DSwissK-abime.wav/LL-Q150_%28fra%29-DSwissK-abime.wav.mp3"}], "etymology_text": "Inherited from Middle French abime, from Old French abisme from Vulgar Latin *abyssimus, a superlative of abyssus (“bottomless pit”), from Ancient Greek ἄβυσσος (ábussos).", "etymology_templates": [{"name": "glossary", "args": {"1": "Inherited"}, "expansion": "Inherited"}, {"name": "inh", "args": {"1": "fr", "2": "frm", "3": "abime", "4": "", "5": "", "lit": "", "pos": "", "tr": "", "ts": "", "id": "", "sc": "", "g": "", "g2": "", "g3": "", "nocat": "", "sort": ""}, "expansion": "Middle French abime"}, {"name": "inh+", "args": {"1": "fr", "2": "frm", "3": "abime"}, "expansion": "Inherited from Middle French abime"}, {"name": "der", "args": {"1": "fr", "2": "fro", "3": "abisme"}, "expansion": "Old French abisme"}, {"name": "der", "args": {"1": "fr", "2": "VL.", "3": "*abyssimus"}, "expansion": "Vulgar Latin *abyssimus"}, {"name": "der", "args": {"1": "fr", "2": "grc", "3": "ἄβυσσος"}, "expansion": "Ancient Greek ἄβυσσος (ábussos)"}], "word": "abime", "lang": "French", "lang_code": "fr", "senses": [{"links": [["abîme", "abîme#French"]], "glosses": ["post-1990 spelling of abîme"], "tags": ["masculine"], "id": "en-abime-fr-noun-ozZ~Thra", "categories": [{"name": "French alternative spellings", "kind": "other", "parents": [], "source": "w"}, {"name": "French entries with incorrect language header", "kind": "other", "parents": ["Entries with incorrect language header", "Entry maintenance"], "source": "w"}, {"name": "French post-1990 spellings", "kind": "other", "parents": [], "source": "w"}], "related": [{"word": "abimer"}]}]}
// prettier-ignore
const y = {"pos": "noun", "head_templates": [{"name": "fr-noun", "args": {"1": "f"}, "expansion": "aberrance f (plural aberrances)"}], "forms": [{"form": "aberrances", "tags": ["plural"]}], "word": "aberrance", "lang": "French", "lang_code": "fr", "sounds": [{"ipa": "/a.bɛ.ʁɑ̃s/"}, {"ipa": "/a.be.ʁɑ̃s/"}, {"audio": "LL-Q150 (fra)-Ltrlg-aberrance.wav", "ogg_url": "https://upload.wikimedia.org/wikipedia/commons/transcoded/b/bf/LL-Q150_%28fra%29-Ltrlg-aberrance.wav/LL-Q150_%28fra%29-Ltrlg-aberrance.wav.ogg", "mp3_url": "https://upload.wikimedia.org/wikipedia/commons/transcoded/b/bf/LL-Q150_%28fra%29-Ltrlg-aberrance.wav/LL-Q150_%28fra%29-Ltrlg-aberrance.wav.mp3"}], "senses": [{"links": [["statistics", "statistics"], ["aberrant", "aberrant"]], "raw_glosses": ["(statistics) character of what is aberrant"], "topics": ["mathematics", "sciences", "statistics"], "glosses": ["character of what is aberrant"], "tags": ["feminine"], "id": "en-aberrance-fr-noun-JNOUPNuV", "categories": [{"name": "Statistics", "kind": "topical", "parents": ["Formal sciences", "Mathematics", "Sciences", "All topics", "Fundamental"], "source": "w", "orig": "fr:Statistics", "langcode": "fr"}, {"name": "French entries with incorrect language header", "kind": "other", "parents": ["Entries with incorrect language header", "Entry maintenance"], "source": "w+disamb", "_dis": "47 53"}]}, {"links": [["aberration", "aberration"], ["anomaly", "anomaly"]], "raw_glosses": ["(uncommon) an aberration or anomaly"], "glosses": ["an aberration or anomaly"], "tags": ["feminine", "uncommon"], "id": "en-aberrance-fr-noun-JZPJfoGB", "categories": [{"name": "French entries with incorrect language header", "kind": "other", "parents": ["Entries with incorrect language header", "Entry maintenance"], "source": "w+disamb", "_dis": "47 53"}]}]}

describe("word type detector", () => {
    it("can parse some words I pulled from the file", () => {
        const frenchWord = new FrenchWord(x)
        expect(frenchWord.word).toBe("abime")
        const frenchWordV2 = new FrenchWord(y)
        expect(frenchWordV2.word).toBe("aberrance")
    })
    it("can read some lines from the example file", () => {
        const fileStream = fs.createReadStream(FILE_PATH)

        const lineTransform = new LineTransform()

        fileStream.pipe(lineTransform)

        let v = 0
        lineTransform.on("data", (line) => {
            const jsonObject = JSON.parse(line)
            if (jsonObject.lang_code === "fr") {
                v = v + 1
                const frenchWord = new FrenchWord(jsonObject)
                expect(frenchWord.word).toBeTruthy()
            }
            if (v === 10) {
                fileStream.destroy() // after 10 lines
            }
        })
    })
})
