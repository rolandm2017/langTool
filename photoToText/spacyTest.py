import spacy

# Load the French model
nlp = spacy.load("fr_core_news_sm")

doc = nlp(u"voudrais non animaux yeux dors couvre.")
for token in doc:
    print(token, token.lemma_)

# TODO: Try Spacy for: (1) Detecting verbs (2) detecting nouns (3) detecting gender of nouns