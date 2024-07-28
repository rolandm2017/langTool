#
# Run this to convert a bunch of png files into text & csv files.
# The script uses Google's Cloud Vision API.
# The API response is saved to a .txt and .csv to be nice to google's servers; no need to use it >1x.
#

# TODO: If the preceding word is un, une, le, les, au, aux, keep the article.
# TODO: so you want to keep the gender associated.

from typing import List
import traceback
import requests
import re
import os

from fastapi import FastAPI, HTTPException
import os
import httpx
from PIL import Image
from google.cloud import vision


from dotenv import load_dotenv

load_dotenv('.env')
api_key = os.getenv('GOOGLE_CLOUD_API_KEY')

TARGET_SERVER_URL = "http://localhost:8080/words/batch"



def analyze_image_using_google(image_file):
    client = vision.ImageAnnotatorClient()

    content = image_file.read()
    image = vision.Image(content=content)

    response = client.text_detection(image=image)
    texts = response.text_annotations

    if response.error.message:
        raise Exception(f"{response.error.message}")
    
    return texts[0].description if texts else None


def process_page_into_array(src: str) -> List[str]: 
    print("foo")
    return ["a", "b"]


def convert_to_string_arr(text: str) -> List[str]:
    return re.split(r'\s+', text)


def handle_generic_exception(e):
    print(f"Exception occurred: {str(e)}")
    stack_trace = ''.join(traceback.format_exception(type(e), e, e.__traceback__))
    print("Stack trace:")
    print(stack_trace)
    raise HTTPException(status_code=500, detail=str(e))


def save_img_text_as_text_db(file_name, file_text):
    associated_text_file = "./textDb/" + file_name + "_as_text.txt"
    print("saving into text file -> ", associated_text_file)
    with open(associated_text_file, "w", encoding="utf-8") as f:
        f.write(file_text)


def save_img_text_as_csv(file_name, text_arr):
    associated_text_file = "./textDb/" + file_name + "_as_text.csv"
    print("saving into csv -> ", associated_text_file)
    with open(associated_text_file, "w", encoding="utf-8") as f:
        for word in text_arr:
            f.write(word + "\n")


articles = ["le", "la", "les", "un", "une", "des", "au", "aux", "du"]

def preserve_article(text_arr):
    out = []
    for i in range(0, len(text_arr)):
        if text_arr[i] in articles:
            word_with_article = text_arr[i] + " " + text_arr[i + 1]
            out.append(word_with_article)
            i = i + 1  # skip the word we just joined together


def read_text_from_file(file_name):
    print(f"Received fileName: {file_name}")

    # Read the file content
    try:
        print("Attempting to open the file...")
        with open("./lesChats/" + file_name, 'rb') as file:
            print(f"File {file_name} opened successfully.")
            file_text = analyze_image_using_google(file)
            print(f"File text read successfully: {file_text[:100]}...")  # Print first 100 chars for brevity

            text_arr = convert_to_string_arr(file_text)
    except FileNotFoundError:
        print(f"FileNotFoundError: File {file_name} not found.")
        raise HTTPException(status_code=404, detail="File not found")
    except Exception as e:
        handle_generic_exception(e)

    text_arr = preserve_article(text_arr)

    save_img_text_as_text_db(file_name, file_text)
    save_img_text_as_csv(file_name, text_arr)

    # Send the file text to the target server
    print("Sending file text to the target server...")
    try:
        print("sending text of length " + str(len(file_text)))
        print(file_text)
        print("=====")
        response = requests.post(
            TARGET_SERVER_URL,
            json={"words": text_arr}
        )
        print(f"Received response from target server: Status Code {response.status_code}")
        response_json = response.json()
        print(f"Response JSON: {response_json}")
    except Exception as e:
        print(f"Exception occurred while sending request: {str(e)}")
        raise HTTPException(status_code=500, detail="Error communicating with target server")




TARGET_FILE = "./lesChats/lesChats - 1.png"

def get_file_name_heic_ver(number):
    return f"lesChats ({number}).HEIC"

def get_file_name_png_ver(number):
    return f"lesChats - {number}.png"


for i in range(1,32):
    read_text_from_file(get_file_name_png_ver(i))

# read_text_from_file(TARGET_FILE)