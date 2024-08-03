# What is LangTool?

langtool is a repo of several disjointed tools that I believe will be useful for language learning after some work.

## Repositories

/jsonDbParser -> Parses a long .jsonl file that is a french dictionary into a Postgres db.
/client -> Some day it will house an Anki-esque GUI enabling users to rate a word's difficulty.
/server -> Will handle a variety of tasks, including

-   Keeping track of which words the user knows already.
-   Translating words using external services.
-   Generating .csv files of unknown words to convert into Anki decks.

/langLordsAnkiScript -> IDK what this is TBH.
/photoToText -> A program that enables you to send photos to Google's Cloud Vision API and get the photo's text returned.

-   Will later feed the photo's text into the /server server to bundle into an Anki deck.

/storyGenerator -> Will some day, maybe, become a GPT wrapper that turns lists of unknown words into stories.
/textToSpeechScript -> Uses ElevenLabs to convert text to high quality speech in a target language. To be used for shadowing exercises.
