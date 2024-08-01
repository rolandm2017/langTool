###
###
### COMMENTED OUT BECAUSE I CAN ASK gpt TO DO IT FOR ME IN THE OTHER SCRIPT.
###
###

# import re
# from typing import List


# articles = ["le", "la", "les", "un", "une", "des", "au", "aux", "du", "sa", "son", "ses"]



# def clean_up(txt_arr):
#     out = []
#     for txt in txt_arr:
#         if len(txt) <= 2:
#             continue  # skip the really short ones.
#         out.append(sanitize(txt))

#     return out


# def sanitize(t):
#     return ''.join(char for char in t if char not in '...-.,?:;')



# def convert_to_string_arr(text: str) -> List[str]:
#     return re.split(r'\s+', text)


# def preserve_article(text_arr):
#     out = []
#     for i in range(0, len(text_arr)):
#         if text_arr[i] in articles:
#             word_with_article = text_arr[i] + " " + text_arr[i + 1]
#             out.append(word_with_article)
#             i = i + 1  # skip the word we just joined together


# def open_text_db(number):
#     associated_text_file = f"./textDb/lesChats - {number}.png_as_text.txt"
#     print("opening text file -> ", associated_text_file)
#     with open(associated_text_file, "r", encoding="utf-8") as f:
#         txt = [x.strip() for x in f.readlines()]
#     return txt


# def convert_to_string_arr(text: str) -> List[str]:
#     return re.split(r'\s+', text)


# for i in range(1, 32):
#     read_from_db = open_text_db(i)
#     separate_words = [convert_to_string_arr(sentence) for sentence in read_from_db]
#     print(separate_words)
#     separate_words = preserve_article(separate_words)
#     print(separate_words)
#     exit()

# # open all the .txt db files

# # join up the articles with the words

# # proceed thru csvTallyTool as normal