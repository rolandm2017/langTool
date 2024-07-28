# Open all the CSVs
# Tally up the words
# Add every word one time
# Translate every word into English
# Output en,fr word pair CSV
# Then import into Anki.

import deepl

from dotenv import load_dotenv, dotenv_values
import os

load_dotenv('.env')
api_key = os.getenv('DEEPL_API_KEY')

big_word_dict = {}

path = "./textDb"


def open_csv(number):
    associated_text_file = "./textDb/" + f"lesChats - {number}.png_as_text.csv"
    print("opening csv -> ", associated_text_file)
    with open(associated_text_file, "r", encoding="utf-8") as f:
        v = f.readlines()
    return v



for i in range(1, 32):
    bunch_of_words = open_csv(i)
    for word in bunch_of_words:
        big_word_dict[word.strip()] = True


def clean_up(txt_arr):
    out = []
    for txt in txt_arr:
        if len(txt) <= 2:
            continue  # skip the really short ones.
        out.append(sanitize(txt))

    return out


def sanitize(t):
    return ''.join(char for char in t if char not in '...-.,?')


words_arr = clean_up(big_word_dict.keys())

# print(words_arr)
print(len(words_arr))
# exit()

def group_array(long_array, group_size=50):
    return [long_array[i:i+group_size] for i in range(0, len(long_array), group_size)]

packaged_groups_of_fifty = group_array(words_arr)


pairs = {}


config = dotenv_values(".env")

# Print all key-value pairs
for key, value in config.items():
    print(f'{key}: someVal')


if api_key is None:
    print("api key was None")
    print(os.getcwd())
auth_key = api_key
translator = deepl.Translator(auth_key)

class Pair:
    def __init__(self, en, fr):
        self.english = en
        self.french = fr

for package in packaged_groups_of_fifty:
    translations = translator.translate_text(package, source_lang="FR", target_lang="EN-US")
    just_text = [x.text for x in translations]
    for i in range(0, 50):
        pairs[just_text[i]] = package[i]
        # pairs
    break

csv = ""
for key, val in pairs.items():
    print(f"{key}: {val}")
    csv = csv + key + "," + val + "\n"

with open("new_anki_deck-07-28.csv", "w", encoding="utf-8") as f:
    f.write(csv)

# result = translator.translate_text("Hello, world!", source_lang="FR", target_lang="EN")
# print(result.text)  # "Bonjour, le monde !"

