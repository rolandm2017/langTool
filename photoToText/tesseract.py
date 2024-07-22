from PIL import Image
import pytesseract

# Load an image
img = Image.open('TEST.png')

pytesseract.pytesseract.tesseract_cmd = f'C:\Program Files\Tesseract-OCR\\tesseract.exe'

# Extract text
text = pytesseract.image_to_string(img)
print(text)