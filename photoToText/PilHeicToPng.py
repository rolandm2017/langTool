from PIL import Image
import pillow_heif

def get_file_name(number):
    return f"lesChats ({number}).HEIC"

for i in range(1,32):

    heif_file = pillow_heif.read_heif(get_file_name(i))
    image = Image.frombytes(
        heif_file.mode,
        heif_file.size,
        heif_file.data,
        "raw",
    )

    out_file_name = f"./lesChats - {i}.png"

    image.save(out_file_name, format("png"))