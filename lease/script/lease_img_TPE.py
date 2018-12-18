import sys
sys.path.append("lib/")
from bs4 import BeautifulSoup
from wb import get_web_page
from myio import read_excel, save
from progress_bar import progress, showProgess
import os
import re
import urllib.request
import json
import pandas as pd
import shutil

DETAIL_URL = "https://rent.591.com.tw/"
urlJumpIp = 1

def save(img_urls, post_id, dir):
    if img_urls:
        try:
            dname = dir + str(post_id)
            os.makedirs(dname, exist_ok=True)

            for img_url in img_urls:
                fname = img_url.split('/')[-1]
                urllib.request.urlretrieve(img_url, os.path.join(dname, fname))
        except Exception as e:
            print(e)

def get_images(dom):
    try:
        soup = BeautifulSoup(dom, "html.parser")
        div = soup.find("div", "imgList")
        images = div.find_all("img")

        img_urls = []
        for img in images:
            img_urls.append(img["src"].replace("125x85.crop", "765x517"))

        return img_urls
    except Exception as e:
        print("Webpage is not exists")
        return None



def IMG_TPE_INIT():
    row_data = read_excel("lease/data/TPE/info/total_rows_TPE.xlsx") # get the excel info

    dir = "D:/Python/database/lease/images/TPE/"
    if os.path.exists(dir): # 先刪除原本的images資料夾
        shutil.rmtree(dir, ignore_errors=True)

    for data in row_data:
        page = get_web_page(DETAIL_URL + data["url"], urlJumpIp)
        img_urls = get_images(page)
        save(img_urls, data["post_id"], dir)
        showProgess(__file__)

    print(str(__file__) + " complete")
