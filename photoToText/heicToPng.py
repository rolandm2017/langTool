from heic2png import HEIC2PNG

def convert_heic_to_png(number):
    pass

def get_file_name(number):
    return f"lesChats ({number}).HEIC"

if __name__ == '__main__':
    for i in range(1,32):
        heic_img = HEIC2PNG(get_file_name(i), quality=90)  # Specify the quality of the converted image
        heic_img.save()  # The converted image will be saved as `test.png`