import photoToText.spacyTest as spacyTest
nlp = spacyTest.load('fr_core_news_md')

doc = nlp(u"voudrais non animaux yeux dors couvre.")
for token in doc:
    print(token, token.lemma_)