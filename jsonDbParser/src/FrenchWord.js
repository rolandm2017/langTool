import {
    parseRawTagsData,
    parseHeadTemplateArgs,
    getGenderFromHeadTemplate,
} from "./frenchWordUtil"

class FrenchWord {
    constructor(data) {
        //   this.sounds = data.sounds.map(sound => new Sound(sound)); // don't care
        //   this.etymologyText = data.etymology_text; // don't care
        //   this.etymologyTemplates = data.etymology_templates.map(template => new EtymologyTemplate(template)); // don't care

        let rawTagsData
        if (data.forms) {
            rawTagsData = parseRawTagsData(data.forms) // data.forms.map((form) => form.tags).flat()
        }
        let headTemplateArgs = parseHeadTemplateArgs(data.head_templates)
        this.pos = data.pos
        this.headTemplates = undefined
        if (data.head_templates) {
            this.headTemplates = data.head_templates.map(
                (template) => new HeadTemplate(template)
            )
        }
        this.forms = undefined
        if (data.forms) {
            this.forms = data.forms.map((form) => new Form(form))
        }

        this.word = data.word
        this.lang = data.lang
        this.langCode = data.lang_code
        this.senses = data.senses.map((sense) => new Sense(sense))

        this.tags = rawTagsData ? rawTagsData.tagsArray : undefined
        this.tagsString = rawTagsData ? rawTagsData.tagsString : undefined
        this.headTemplateArgs = headTemplateArgs
        this.isFeminine = undefined
        this.isMasculine = undefined

        let genderWasSet
        if (headTemplateArgs) {
            const genderFromArgs = getGenderFromHeadTemplate(headTemplateArgs)
            this.isFeminine = genderFromArgs === "f"
            this.isMasculine = genderFromArgs === "m"
            genderWasSet = genderFromArgs === "f" || genderFromArgs === "m"
        }
        if (rawTagsData && !genderWasSet) {
            this.isFeminine = rawTagsData.tagsArray.includes("feminine")
            this.isMasculine = rawTagsData.tagsArray.includes("masculine")
        }
    }
}

class HeadTemplate {
    constructor(data) {
        // console.log(data.args, '27rm') // does data.args have m,f? masculine,feminine as m or f?
        this.name = data.name
        this.args = data.args
        this.expansion = data.expansion
    }
}

class Form {
    constructor(data) {
        this.form = data.form
        this.tags = data.tags
    }
}

class Sound {
    constructor(data) {
        this.ipa = data.ipa
        this.audio = data.audio
        this.oggUrl = data.ogg_url
        this.mp3Url = data.mp3_url
    }
}

class EtymologyTemplate {
    constructor(data) {
        this.name = data.name
        this.args = data.args
        this.expansion = data.expansion
    }
}

class Sense {
    constructor(data) {
        this.links = data.links
        this.glosses = data.glosses
        this.tags = data.tags
        this.id = data.id
        this.categories = data.categories
            ? data.categories.map((category) => new Category(category))
            : undefined
        this.related = data.related
            ? data.related.map((related) => new Related(related))
            : undefined
    }
}

class Category {
    constructor(data) {
        this.name = data.name
        this.kind = data.kind
        this.parents = data.parents
        this.source = data.source
    }
}

class Related {
    constructor(data) {
        this.word = data.word
    }
}

export { FrenchWord }
