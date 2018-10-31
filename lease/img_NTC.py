import requests
from bs4 import BeautifulSoup
import time
import os
import re
import urllib.request
import json
import pandas as pd
import shutil

DETAIL_URL = "https://rent.591.com.tw/"

def get_web_page(url):
    resp = requests.get(url=url, headers={'User-Agent': 'Custom'}, cookies={"urlJumpIp": "3"})
    if resp.status_code != 200:
        print("Invalid url:", resp.url)
        return None
    else:
        return resp.text

def read_excel():
    try:
        df = pd.read_excel("lease/data/total_rows_NTC.xlsx")
        my_dict = df.to_dict("records")
        return my_dict  # return a list of dict

    except Exception as e:
        print(e)

def save(image_urls, post_id, dir):
    if img_urls:
        try:
            dname = dir + str(post_id)
            os.makedirs(dname, exist_ok=False)

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



if __name__ == "__main__":
    row_data = read_excel() # get the excel info

    dir = "lease/images/NTC/"
    if os.path.exists(dir): # 先刪除原本的images資料夾
        shutil.rmtree(dir, ignore_errors=True)

    for data in row_data:
        page = get_web_page(DETAIL_URL + data["url"])
        print(data["post_id"])
        img_urls = get_images(page)
        save(img_urls, data["post_id"], dir)
