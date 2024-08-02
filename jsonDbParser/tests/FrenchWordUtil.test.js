import { it, describe, test, expect } from "vitest"

import {
    parseRawTagsData,
    parseHeadTemplateArgs,
    getGenderFromHeadTemplate,
} from "../src/frenchWordUtil"
import { atypicalM, atypicalN, atypicalO, atypicalP } from "./atypicalEntries"

import { a, b, c, d, e, f, gVerb, h, iAdj, j, k } from "./specificEntries"
import { twoGenderC, twoGenderD, twoGenderE, twoGenderF } from "./mfEntries"

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

        const validFinds = ["m", "f", "m-p", "f-p"] // m-p, f-p indicates "plural" i think
        expect(validFinds.includes(aOut[0]["1"])).toBeTruthy()
        expect(validFinds.includes(bOut[0]["1"])).toBeTruthy()
        expect(validFinds.includes(cOut[0]["1"])).toBeTruthy()
        expect(validFinds.includes(dOut[0]["1"])).toBeTruthy()
        console.log(dOut, "36rm")

        const mOut = parseHeadTemplateArgs(atypicalM.head_templates)
        const nOut = parseHeadTemplateArgs(atypicalN.head_templates)
        const oOut = parseHeadTemplateArgs(atypicalO.head_templates)
        const pOut = parseHeadTemplateArgs(atypicalP.head_templates)

        expect(mOut).toBeTruthy()
        expect(nOut).toBeTruthy()
        expect(oOut).toBeTruthy()
        expect(pOut).toBeTruthy()
        expect(mOut.length).toEqual(1)
        expect(nOut.length).toEqual(1)
        expect(oOut.length).toEqual(1)
        expect(pOut.length).toEqual(1)
        console.log(mOut[0].g)
        console.log(nOut[0].g)
        console.log(oOut[0].g)
        console.log(pOut[0].g, "68rm")
        expect(validFinds.includes(mOut[0].g)).toBeTruthy()
        expect(validFinds.includes(nOut[0].g)).toBeTruthy()
        expect(validFinds.includes(oOut[0].g)).toBeTruthy()
        expect(validFinds.includes(pOut[0].g)).toBeTruthy()
    })
    it("gets the gender from the head template", () => {
        const aOut = parseHeadTemplateArgs(a.head_templates)
        const bOut = parseHeadTemplateArgs(b.head_templates)
        const cOut = parseHeadTemplateArgs(c.head_templates)
        const dOut = parseHeadTemplateArgs(d.head_templates)
        const mOut = parseHeadTemplateArgs(atypicalM.head_templates)
        const nOut = parseHeadTemplateArgs(atypicalN.head_templates)
        const oOut = parseHeadTemplateArgs(atypicalO.head_templates)
        const pOut = parseHeadTemplateArgs(atypicalP.head_templates)

        const aGender = getGenderFromHeadTemplate(aOut)
        const bGender = getGenderFromHeadTemplate(bOut)
        const cGender = getGenderFromHeadTemplate(cOut)
        const dGender = getGenderFromHeadTemplate(dOut)

        const mGender = getGenderFromHeadTemplate(mOut)
        const nGender = getGenderFromHeadTemplate(nOut)
        const oGender = getGenderFromHeadTemplate(oOut)
        const pGender = getGenderFromHeadTemplate(pOut)

        const validFinds = ["m", "f"]
        expect(validFinds.includes(aGender)).toBeTruthy()
        expect(validFinds.includes(bGender)).toBeTruthy()
        expect(validFinds.includes(cGender)).toBeTruthy()
        expect(validFinds.includes(dGender)).toBeTruthy()
        // the 'g' category
        expect(mGender).not.toBe("?")
        expect(nGender).not.toBe("?")
        expect(oGender).not.toBe("?")
        expect(pGender).not.toBe("?")
        expect(validFinds.includes(mGender)).toBeTruthy()
        expect(validFinds.includes(nGender)).toBeTruthy()
        expect(validFinds.includes(oGender)).toBeTruthy()
        expect(validFinds.includes(pGender)).toBeTruthy()
    })
    it("handles nouns with both m and f indicated", () => {
        const cOut = parseHeadTemplateArgs(twoGenderC.head_templates)
        const dOut = parseHeadTemplateArgs(twoGenderD.head_templates)
        const eOut = parseHeadTemplateArgs(twoGenderE.head_templates)
        const fOut = parseHeadTemplateArgs(twoGenderF.head_templates)

        const cGender = getGenderFromHeadTemplate(cOut)
        const dGender = getGenderFromHeadTemplate(dOut)
        const eGender = getGenderFromHeadTemplate(eOut)
        const fGender = getGenderFromHeadTemplate(fOut)

        expect(cGender).toBe("both")
        expect(dGender).toBe("both")
        expect(eGender).toBe("both")
        expect(fGender).toBe("both")
    })
})
