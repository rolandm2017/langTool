import { it, describe, test, expect } from "vitest"

import { FrenchWord } from "../src/FrenchWord"

import { a, b, c, d, e, f, gVerb, h, iAdj, j, k } from "./specificEntries"
import { atypicalM, atypicalN, atypicalO, atypicalP } from "./atypicalEntries"
import { twoGenderC, twoGenderD, twoGenderE } from "./mfEntries"

describe("the french word class parses data correctly", () => {
    // a, b, c, d, e, f, __, h, j, k
    it("handles the gender being found in the head template args", () => {
        // those are: A, B, C, D, E, F, ## H, J, K
        const expectedToPass = [
            a,
            b,
            c,
            d,
            e,
            f,
            h,
            j,
            k,
            atypicalM,
            atypicalN,
            atypicalO,
            atypicalP,
        ]
        const words = []
        for (const jsonLine of expectedToPass) {
            const word = new FrenchWord(jsonLine)
            words.push(word)
        }

        function genderWasSet(word) {
            return word.isFeminine || word.isMasculine
        }

        expect(words.every((word) => genderWasSet(word))).toBe(true)
    })
    // it("handles the gender being located in the tags", () => {
    //     // those are _, _, and _
    // })
    // it("handles the gender being in one of many head template entries", () => {
    //     // those are _, _, and _
    // })
    it("handles the word being a verb", () => {
        // verb
        const word = new FrenchWord(gVerb)
        expect(word.pos).toBe("verb")
    })
    it("handles the word being an adjective", () => {
        // adjective
        const word = new FrenchWord(iAdj)
        expect(word.pos).toBe("adj")
    })
    // it("handles the word being an adverb", () => {
    // adverb
    // })
    it("handles the word being both M and F", () => {
        const twoGenderV1 = new FrenchWord(twoGenderC)
        const twoGenderV2 = new FrenchWord(twoGenderD)
        const twoGenderV3 = new FrenchWord(twoGenderE)
        expect(twoGenderV1.isBoth).toBeTruthy()
        expect(twoGenderV2.isBoth).toBeTruthy()
        expect(twoGenderV3.isBoth).toBeTruthy()
    })
})
