function parseHeadTemplateArgs(dataDotHeadTemplates) {
    return dataDotHeadTemplates
        ? dataDotHeadTemplates.map((template) => template.args) // data.head_templates[0].args['1'] may have the gender info.
        : undefined
}

function parseRawTagsData(dataDotForms) {
    if (dataDotForms === undefined) {
        return { tagsArray: undefined, tagsString: undefined }
    }
    const rawTagsData = dataDotForms.map((form) => form.tags).flat()
    return {
        tagsArray: rawTagsData,
        tagsString: rawTagsData ? rawTagsData.join(", ") : undefined,
    }
}

function getGenderFromHeadTemplate(headTemplateArgs) {
    const firstPlaceToLook = headTemplateArgs[0]["1"]
    const isSingleCharGender =
        firstPlaceToLook === "m" || firstPlaceToLook === "f"
    if (isSingleCharGender) {
        return firstPlaceToLook
    }
    const isGenderDenotedPlural =
        firstPlaceToLook &&
        firstPlaceToLook.length === 3 &&
        firstPlaceToLook.includes("-")
    if (isGenderDenotedPlural) {
        const splitByDash = firstPlaceToLook.split("-")
        const possiblyGender = splitByDash[0]
        if (possiblyGender === "m" || possiblyGender === "f") {
            return possiblyGender
        }
    }
    const secondPlaceToLook = headTemplateArgs[0].g
    if (secondPlaceToLook === "m" || secondPlaceToLook === "f") {
        return secondPlaceToLook
    }
    if (secondPlaceToLook && secondPlaceToLook.includes("-")) {
        const splitByDash = secondPlaceToLook.split("-")
        const possiblyGender = splitByDash[0]
        if (possiblyGender === "m" || possiblyGender === "f") {
            return possiblyGender
        }
    }
    const propertyOneExists = headTemplateArgs[0]["1"] !== undefined
    if (propertyOneExists) {
        if (
            headTemplateArgs[0]["1"] === "mf" ||
            headTemplateArgs[0]["1"] === "mfbysense"
        )
            return "both"
    }
    return null
}

function getGenderFromG(headTemplateArgs) {}

export { parseRawTagsData, parseHeadTemplateArgs, getGenderFromHeadTemplate }
