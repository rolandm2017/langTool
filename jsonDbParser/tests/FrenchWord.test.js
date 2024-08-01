import { it, describe, test, expect } from "vitest"

import { FrenchWord } from "../src/FrenchWord"

import { a, b, c, d, e, f, gVerb, h, iAdj, j, k } from "./specificEntries"

describe("the french word class parses data correctly", () => {
    // a, b, c, d, e, f, __, h, j, k
    it("handles the gender being found in the head template args", () => {
        // those are: A, B, C, D, E, F, ## H, J, K
        const expectedToPass = [a, b, c, d, e, f, h, j, k]
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
})
