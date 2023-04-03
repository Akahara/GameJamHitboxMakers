from PIL import Image, ImageDraw, ImageFont, ImageFilter
import os
import re
from math import ceil, floor
import sys


font = ImageFont.truetype("../../world_card_game/Alkatra-Medium.ttf", 90)

dir = "C:\\Users\\Albin\\Dev\\eclipse\\GGJ\\res\\texts"

def readfile(path):
    with open(path, 'r', encoding='utf-8') as file:
        return file.read()
    
dummy_img = Image.new('RGB', (1,1))
dummy_draw = ImageDraw.Draw(dummy_img)

def split_text(text, N=50):
    text = text.replace('\\n', '\n').replace('\r', '')
    lines = []
    prev = 0
    for i in range(0, len(text)):
        if text[i] == '\n' or (i-prev >= N and text[i] == ' '):
            lines.append(text[prev:i])
            prev = i+1
    lines.append(text[prev:])
    return "\n".join(lines)


for text_dir in os.listdir(dir):
    texts = readfile(dir + "\\" + text_dir + "\\texts.txt")
    if texts[0] == '0':
        texts = [ line[line.index(' ', line.index(' ')+1)+1:] for line in texts.split('\n') ]
        texts = [ split_text(text, 30) for text in texts ]
        for i,text in enumerate(texts):
            _x,_y,width,height = dummy_draw.textbbox((0,0), text, font=font)
            img = Image.new('RGBA', (width, height), (255,255,255,0))
            draw = ImageDraw.Draw(img)
            draw.text((0,0), text, font=font, align='center')
            img.save("%s\\%s\\text_%d.png" % (dir, text_dir, i))
    else:
        _x,_y,width,height = dummy_draw.textbbox((0,0), split_text(texts), font=font)
        img = Image.new('RGBA', (width, height*5), (255,255,255,0))
        draw = ImageDraw.Draw(img)
        y = 0
        colors = [ (152, 142, 158,255), (176, 221, 202,255) ]
        for i,line in enumerate(texts.split('\n')):
            N = 20
            line = split_text(line)
            # line = "\n".join([ line[j:j+N] for j in range(0, len(line), N) ])
            box = dummy_draw.textbbox((0,0), line, font=font)
            x = width - box[2] if i%2==1 else 0
            draw.text((x,y), line, font=font, fill=colors[i%2], align='right' if i%2==1 else 'left')
            y += box[3] - box[1] + 30
        img = img.crop((0,0,width,y))
        img.save("%s\\%s\\text.png" % (dir, text_dir))