# Open all the CSVs
# Tally up the words
# Add every word one time
# Translate every word into English
# Output en,fr word pair CSV
# Then import into Anki.


import requests

from dotenv import load_dotenv, dotenv_values
import os

import deepl

load_dotenv('.env')
deepL_api_key = os.getenv('DEEPL_API_KEY')
gpt_api_key = os.getenv("GPT_API_KEY")

OUT_ANKI_CSV_FILE_NAME = "new_anki_deck-07-28.csv"

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


if deepL_api_key is None:
    print("api key was None")
    print(os.getcwd())
auth_key = deepL_api_key
translator = deepl.Translator(auth_key)


def unwrap_gpt_content(gpt_response):
    return gpt_response["choices"][0]["message"]["content"][2:].split("\n- ")


def get_articles_and_infinitives_with_gpt(package):
    raise ValueError("GPT sucks, don't use it for this")
    payload_for_gpt = package
    # Create the prompt
    prompt = f"Here is a list of French nouns: {payload_for_gpt}. Please return each noun with its correct article (le or la) attached. For verbs, please respond with the infinitive of the verb. If the noun is a proper noun, you can mark it 'P.N.'. If the word could be either a verb or a noun, you can guess. For words which are neither nouns nor verbs, write ## before them so I know to identify their type."

    # Define the request payload
    payload = {
        "model": "gpt-3.5-turbo",
        "messages": [{"role": "user", "content": prompt}],
        "temperature": 0.3
    }

    # Define the request headers
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {gpt_api_key}"
    }

    # Make the request to the OpenAI API
    response = requests.post("https://api.openai.com/v1/chat/completions", headers=headers, json=payload)

    return unwrap_gpt_content(response.json())


class Pair:
    def __init__(self, en, fr):
        self.english = en
        self.french = fr
        self.pn = False
        self.retry = False
    

v = 0

for package in packaged_groups_of_fifty:
    translations = translator.translate_text(package, source_lang="FR", target_lang="EN-US")
    just_text_arr = [x.text for x in translations]
    # print(just_text_arr)
    # exit()
    print("len of package: ", len(package))
    print(package)
    words_with_articles_and_infinitives = get_articles_and_infinitives_with_gpt(package)
    print("words_with_articles_and_infinitives")
    print(words_with_articles_and_infinitives)
    print(len(words_with_articles_and_infinitives))
    exit()
    # exit()
    for i in range(0, 50):
        pairs[just_text_arr[i]] = words_with_articles_and_infinitives[i]
    with open(f"gpt_out_for_loop{v}.csv", "w") as f:
        f.write(words_with_articles_and_infinitives)
    v = v + 1
    if v == 2:
        break   

csv = ""

# TODO: Tell GPT to give you the french noun with the article. 
for key, val in pairs.items():
    print(f"key, val: {key}: {val}")
    csv = csv + key + "," + val + "\n"

print(f"writing {OUT_ANKI_CSV_FILE_NAME}")

with open(OUT_ANKI_CSV_FILE_NAME, "w", encoding="utf-8") as f:
    f.write(csv)

# result = translator.translate_text("Hello, world!", source_lang="FR", target_lang="EN")
# print(result.text)  # "Bonjour, le monde !"

