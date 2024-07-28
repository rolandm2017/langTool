from typing import List
import traceback

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import os
import httpx
from PIL import Image
from google.cloud import vision

import re

from dotenv import load_dotenv
import os

load_dotenv('.env')
api_key = os.getenv('GOOGLE_CLOUD_API_KEY')

# pytesseract.pytesseract.tesseract_cmd = f'C:\Program Files\Tesseract-OCR\\tesseract.exe'

app = FastAPI()

# Define the URL of the server to which you want to send the text
TARGET_SERVER_URL = "http://localhost:8080/words/batch"


# NOTE:
# PyTesseract sucked! So I'm going to use Google Cloud Vision API, which owns.


class FileRequest(BaseModel):
    fileName: str

class FileResponse(BaseModel):
    fileText: List[str]

def analyze_image_using_google(image_file):
    client = vision.ImageAnnotatorClient()

    content = image_file.read()
    image = vision.Image(content=content)

    response = client.text_detection(image=image)
    texts = response.text_annotations

    if response.error.message:
        raise Exception(f"{response.error.message}")
    
    return texts[0].description if texts else None

@app.post("/read-image-text", response_model=FileResponse)
async def read_image_text(request: FileRequest):
    print("HERE")
    file_path = os.path.join(os.getcwd(), request.fileName)
    
    if not os.path.exists(file_path):
        raise HTTPException(status_code=404, detail="File not found")

    print("reading file path ")
    file_text = analyze_image_using_google(file_path)
    print(file_text)
    no_newline = file_text.split("\n")
    # print("num of pieces: ", str(len(with_splits)))

    big_long_para = " ".join(no_newline)
    
    return {"fileText": no_newline, "bigPara": big_long_para}



@app.post("/process-and-save")
async def upload_file(request: FileRequest):
    file_name = request.fileName
    print(f"Received fileName: {file_name}")

    # Read the file content
    try:
        print("Attempting to open the file...")
        with open(file_name, 'rb') as file:
            print(f"File {file_name} opened successfully.")
            file_text = analyze_image_using_google(file)
            print(f"File text read successfully: {file_text[:100]}...")  # Print first 100 chars for brevity

            text_arr = convert_to_string_arr(file_text)
    except FileNotFoundError:
        print(f"FileNotFoundError: File {file_name} not found.")
        raise HTTPException(status_code=404, detail="File not found")
    except Exception as e:
        print(f"Exception occurred: {str(e)}")
        stack_trace = ''.join(traceback.format_exception(type(e), e, e.__traceback__))
        print("Stack trace:")
        print(stack_trace)
        raise HTTPException(status_code=500, detail=str(e))

    # Send the file text to the target server
    print("Sending file text to the target server...")
    async with httpx.AsyncClient() as client:
        try:
            print("sending text of length " + str(len(file_text)))
            print(file_text)
            print("=====")
            response = await client.post(
                TARGET_SERVER_URL,
                json={"words": text_arr}
            )
            print(f"Received response from target server: Status Code {response.status_code}")
            response_json = response.json()
            print(f"Response JSON: {response_json}")
        except Exception as e:
            print(f"Exception occurred while sending request: {str(e)}")
            raise HTTPException(status_code=500, detail="Error communicating with target server")

    # Return the response from the target server
    return {
        "status_code": response.status_code,
        "response": response_json,
        "words_sent": len(text_arr)
    }

def process_page_into_array(src: str) -> List[str]: 
    print("foo")
    return ["a", "b"]


def convert_to_string_arr(text: str) -> List[str]:
    return re.split(r'\s+', text)
