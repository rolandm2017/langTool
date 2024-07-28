articles = ["le", "la", "les", "un", "une", "des", "au", "aux", "du"]

def preserve_article(text_arr):
    out = []
    for i in range(0, len(text_arr)):
        if text_arr[i] in articles:
            word_with_article = text_arr[i] + " " + text_arr[i + 1]
            out.append(word_with_article)
            i = i + 1  # skip the word we just joined together


# open all the .txt db files

# join up the articles with the words

# proceed thru csvTallyTool as normal