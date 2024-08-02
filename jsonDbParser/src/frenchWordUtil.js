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
    // /*
    //  * FIXME: There are many many ways for the gender to appear in the 'head template args' and I don't catch all of them
    //  */
    const firstPlaceToLook = headTemplateArgs[0]["1"]
    if (firstPlaceToLook === "m" || firstPlaceToLook === "f") {
        return firstPlaceToLook
    }
    const secondPlaceToLook = headTemplateArgs[0].g
    if (secondPlaceToLook === "m" || secondPlaceToLook === "f") {
        return secondPlaceToLook
    }
    // maybe it's "m-p" or "f-p":
    // console.log(headTemplateArgs, secondPlaceToLook, "31rm")
    if (secondPlaceToLook && secondPlaceToLook.includes("-")) {
        const splitByDash = secondPlaceToLook.split("-")
        const possiblyGender = splitByDash[0]
        if (possiblyGender === "m" || possiblyGender === "f") {
            return possiblyGender
        }
    }
    console.log(headTemplateArgs, "39rm")
    const propertyOneExists = headTemplateArgs[0]["1"] !== undefined
    const propertyOneValueHasThirdChar = headTemplateArgs[0]["1"].length
    if (
        propertyOneExists &&
        propertyOneValueHasThirdChar &&
        headTemplateArgs[0]["1"].slice(0, 2) === "mf"
    ) {
        console.log(headTemplateArgs[0]["1"].slice(0, 2), "41rm")
        return "both"
    }
    if (propertyOneExists && headTemplateArgs[0]["1"] === "mf") {
        console.log(headTemplateArgs[0]["1"], "41rm")
        return "both"
    }
    return "?"
}

function getGenderFromG(headTemplateArgs) {}

export { parseRawTagsData, parseHeadTemplateArgs, getGenderFromHeadTemplate }
