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
    return headTemplateArgs[0]["1"]
}

export { parseRawTagsData, parseHeadTemplateArgs, getGenderFromHeadTemplate }
