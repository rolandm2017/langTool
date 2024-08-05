# Open all the CSVs
# Tally up the words
# Count which ones occur how many times

import os

big_word_dict = {}

WORD_TALLY_CSV_PATH = "./les_chats_word_tally_csv.csv"
LES_CHATS_WORDS_ONLY_PATH = "./les_chats_words.csv"

def is_numbers_and_commas(string):
    return all(char.isdigit() or char == ',' for char in string)


def open_csv(number):
    associated_text_file = "./textDb/" + f"lesChats - {number}.png_as_text.csv"
    print("opening csv -> ", associated_text_file)
    with open(associated_text_file, "r", encoding="utf-8") as f:
        v = f.readlines()
    return v


# def clean_up(txt_arr):
#     out = []
#     for txt in txt_arr:
#         if txt.isdigit():
#             continue  # don't add page nums
#         if len(txt) <= 3:
#             continue  # skip the really short ones.
#         sanitized = sanitize(txt)
#         # print(sanitized, sanitized.isnumeric())
#         if sanitized.isnumeric():
#             continue
#         out.append(sanitized)

#     return out


def sanitize(t):
    return ''.join(char for char in t if char not in '.,?:;&')

skipped = []

for i in range(1, 32):
    bunch_of_words = open_csv(i)
    for word in bunch_of_words:
        word = sanitize(word.strip().lower())  # Remove whitespace and convert to lowercase
        # if len(word) <= 3 or word.isnumeric():
            # skipped.append(word)
            # continue
        if word:  # Check if the word is not empty
            big_word_dict[word] = big_word_dict.get(word, 0) + 1

# words_arr = clean_up(big_word_dict.keys())

v = 0

def sort_dict_by_value(word_dict, reverse=True):
    sorted_items = sorted(word_dict.items(), key=lambda x: x[1], reverse=reverse)
    return sorted_items

def convert_dict_to_csv_word_only(word_list):
    lines = []
    for word, count in word_list:
        if len(word) <= 3:
            continue
        lines.append(f"{word}\n")
    return "".join(lines)


def convert_dict_to_csv(word_list):
    lines = []
    boo = []
    for word, count in word_list:
        if len(word) <= 3:
            if word.isnumeric():
                continue
            boo.append(f"{word},{count}\n")
        lines.append(f"{word},{count}\n")
    return "".join(lines), "".join(boo)

# csv, boo = convert_dict_to_csv(sort_dict_by_value(big_word_dict))
csv = convert_dict_to_csv_word_only(sort_dict_by_value(big_word_dict))

print(f"writing {WORD_TALLY_CSV_PATH}")

# with open("boo.csv", "w", encoding="utf-8") as fff:
    # fff.write(boo)


with open(LES_CHATS_WORDS_ONLY_PATH, "w", encoding="utf-8") as f:
    f.write(csv)

# result = translator.translate_text("Hello, world!", source_lang="FR", target_lang="EN")
# print(result.text)  # "Bonjour, le monde !"

