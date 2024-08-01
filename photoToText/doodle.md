so

google cloud vision works well, but not perfectly.

1. import-ant is split across two lines. fine.
2. some jibberish comes back sometimes

hence, the py server will submit the words, and the java server will ask, "are these all valid french words?"
the invalid Fr words will uh, be discarded.

Hence I need a Fr dictionary db.

# doodle July 28

what has given me pause?
the problem is that ... "les prendre" for example.
fr lang puts direct objects before the verb! so you'll get: le prendre, l'apprendre, la constuire, la boire

current best solution: ignore the article, learn without gender

Another solution would be to query a dictionary API over and over, but that would be ~2,800 requests
for just my 60 page book.

-   I could use a Spellchecker like Hunspell. If "le voiture" has an underline, it must be "la voiture"

-   I could create my own database by ... nah -- it couldve been, "collate from sources" but, nah: same "les prendre" problem

-   The best solution would be to download a french dictionary database, like in Sqlite format, and use it.

-   OK so the best solution I have so far is to ask GPT to give me the noun returned to me with the article.

# doodle july 28 part Deux

-   I could: 1. Group the words, translate them into En via DeepL. ->

... smaller problems: a. how do I know which are nouns and verbs? In English or French. NLP could do it

# July 30

I can accept the JsonL file as a db for now, and slowly verify the gender of nouns over several weeks.

~90,000 nouns / 50 nouns per request = 1800 requests => 90 days @ 20 requests/day
