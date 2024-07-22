from dotenv import load_dotenv
import os
import requests


load_dotenv()  # Load variables from .env file

api_key = os.getenv("GPT_API_KEY")

if api_key:
    print("API key successfully retrieved")
else:
    print("API key not found")

words_to_learn = [""]

def tell_GPT_to_work(words):
    prompt = f"""
        Generate a 200 word story that uses the words:

        {words}

        you can skip a few words
        """
    story = get_story_from_prompt(prompt)

    return story

def get_story_from_prompt(prompt):
    pass


def write_story_to_txt(story):
    pass