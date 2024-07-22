from dotenv import load_dotenv
import os
import requests


load_dotenv()  # Load variables from .env file

api_key = os.getenv("ELEVEN_LABS_API_KEY")

if api_key:
    print("API key successfully retrieved")
else:
    print("API key not found")


# rachel_voice_id = "21m00Tcm4TlvDq8ikWAM"
martin_dupont_id = "FNOttooGMYDRXmqkQ0Fz"
tenko_id = "0bKGtCCpdKSI5NjGhU3z"

CHUNK_SIZE = 1024
url = f"https://api.elevenlabs.io/v1/text-to-speech/{tenko_id}"

print(url)

headers = {
  "Accept": "audio/mpeg",
  "Content-Type": "application/json",
  "xi-api-key": api_key
}

target_translation = "J'avais peur. Il avais raison: Les ennuis etaient tres grand pour les soldats. Je n'etais pas un chef. J'etais terrifiant."

data = {
  "text": target_translation,
  "model_id": "eleven_monolingual_v1",
  "voice_settings": {
    "stability": 0.5,
    "similarity_boost": 0.5
  }
}

response = requests.post(url, json=data, headers=headers)
print("response status: ", response.status_code)
# print(response.content)
print("==\n==")
# print(response.text)
with open('tenko_output.mp3', 'wb') as f:
    for chunk in response.iter_content(chunk_size=CHUNK_SIZE):
        if chunk:
            f.write(chunk)