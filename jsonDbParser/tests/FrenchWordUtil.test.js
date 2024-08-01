import { it, describe, test, expect } from "vitest"

import {
    parseRawTagsData,
    parseHeadTemplateArgs,
    getGenderFromHeadTemplate,
} from "../src/frenchWordUtil"

import { a, b, c, d, e, f, gVerb, h, iAdj, j, k } from "./specificEntries"

describe("the parsers work as intended", () => {
    it("extracts the raw tags data", () => {
        const aOut = parseRawTagsData(a.forms)
        const bOut = parseRawTagsData(b.forms)
        const cOut = parseRawTagsData(c.forms) //   args: { 1: "m", f: "+" },
        const dOut = parseRawTagsData(d.forms)

        expect(aOut.tagsArray.length).toEqual(1)
        expect(bOut.tagsArray.length).toEqual(1)
        expect(cOut.tagsArray.length).toEqual(2) // because it has 2 args.
        expect(aOut.tagsArray.length).toEqual(1)

        expect(aOut.tagsString).toEqual("plural")
        expect(bOut.tagsString).toEqual("plural")
        expect(cOut.tagsString).toEqual("plural, feminine")
        expect(dOut.tagsString).toEqual("plural")
    })
    it("parses the head template args", () => {
        const aOut = parseHeadTemplateArgs(a.head_templates)
        const bOut = parseHeadTemplateArgs(b.head_templates)
        const cOut = parseHeadTemplateArgs(c.head_templates)
        const dOut = parseHeadTemplateArgs(d.head_templates)

        expect(aOut).toBeTruthy()
        expect(bOut).toBeTruthy()
        expect(cOut).toBeTruthy()
        expect(dOut).toBeTruthy()

        expect(aOut.length).toEqual(1)
        expect(bOut.length).toEqual(1)
        expect(cOut.length).toEqual(1)
        expect(dOut.length).toEqual(1)

        const validFinds = ["m", "f"]
        expect(validFinds.includes(aOut[0]["1"])).toBeTruthy()
        expect(validFinds.includes(bOut[0]["1"])).toBeTruthy()
        expect(validFinds.includes(cOut[0]["1"])).toBeTruthy()
        expect(validFinds.includes(dOut[0]["1"])).toBeTruthy()
        console.log(dOut, "36rm")
    })
    it("gets the gender from the head template", () => {
        const aOut = parseHeadTemplateArgs(a.head_templates)
        const bOut = parseHeadTemplateArgs(b.head_templates)
        const cOut = parseHeadTemplateArgs(c.head_templates)
        const dOut = parseHeadTemplateArgs(d.head_templates)

        const aGender = getGenderFromHeadTemplate(aOut)
        const bGender = getGenderFromHeadTemplate(bOut)
        const cGender = getGenderFromHeadTemplate(cOut)
        const dGender = getGenderFromHeadTemplate(dOut)

        const validFinds = ["m", "f"]
        expect(validFinds.includes(aGender)).toBeTruthy()
        expect(validFinds.includes(bGender)).toBeTruthy()
        expect(validFinds.includes(cGender)).toBeTruthy()
        expect(validFinds.includes(dGender)).toBeTruthy()
    })
})
