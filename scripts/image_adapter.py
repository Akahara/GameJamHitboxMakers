from PIL import Image, ImageDraw, ImageFont, ImageFilter
import os
import re
from math import ceil, floor

dir = "C:\\Users\\Albin\\Dev\\eclipse\\GGJ\\res\\textures\\sources"
out_dir = "C:\\Users\\Albin\\Dev\\eclipse\\GGJ\\res\\textures\\adapted"
CODE = (246, 0, 255, 255)

spritemap_image_width = 160

Infinity = 100000

for image_file in os.listdir(dir):
    if not image_file.endswith('.png'):
        continue
    image = Image.open(dir + "\\" + image_file)
    image = image.convert('RGBA')
    if image.size[0] % spritemap_image_width != 0:
        print("Image width not divisible by " + str(spritemap_image_width))
        continue
    for i in range(image.size[0]//spritemap_image_width):
        minx, miny, maxx, maxy = Infinity, Infinity, -Infinity, -Infinity
        for x in range(i*spritemap_image_width, (i+1)*spritemap_image_width):
            for y in range(image.size[1]):
                # print(image.getpixel((x,y)))
                # exit()
                if image.getpixel((x,y)) == CODE:
                    minx = min(minx, x)
                    miny = min(miny, y)
                    maxx = max(maxx, x)
                    maxy = max(maxy, y)
        if minx == Infinity:
            minx = i * spritemap_image_width
            miny = 0
            maxx = (i+1) * spritemap_image_width
            maxy = image.size[1]
        subimage = image.crop((minx+1, miny+1, maxx, maxy))
        subimage.save(out_dir + "\\" + str(i) + "_" + image_file)