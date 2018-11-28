import requests
from bs4 import BeautifulSoup
import time
import os
import re
import urllib.request
import json
import pandas as pd

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

def get_house_box(dom, post_id):
    house_boxes = []
    my_list = ["post_id", "房屋資料", "生活機能", "附近交通"]
    my_dict = {}.fromkeys(my_list)
    my_dict["post_id"] = post_id

    try:
        #獲取資訊
        soup = BeautifulSoup(dom, "html.parser")
        house_li = soup.find_all("li", "clearfix") #房屋資訊
        life_p = soup.find("div", "lifeBox").find_all("p") #生活交通

        #解析資訊
        str1 = ""
        for li in house_li: #房屋資料
            key = li.find("div", "one").get_text()
            value = li.find("div", "two").get_text()
            str1 += key + value + ","
        my_dict["房屋資料"] = str1

        for p in life_p: #生活機能
            text = p.get_text().strip()
            key = text.split("：")[0]
            value = text.split("：")[1].replace("；", ",")
            my_dict[key] = value

        #回傳資料
        house_boxes.append(my_dict)
        return house_boxes
    except:
        print("post_id " + str(post_id) + ": webpage is no longer exist")
        house_boxes.append(my_dict)
        return house_boxes


def save(data):

    df = pd.DataFrame.from_dict(data)
    writer = pd.ExcelWriter('lease/data/house_box_NTC.xlsx', engine='xlsxwriter')
    df.to_excel(writer, sheet_name='lease_house_box_data')
    writer.save()

    with open('lease/data/house_box_NTC.json', 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, sort_keys=True, ensure_ascii=False)

if __name__ == "__main__":
    row_data = read_excel() # get the excel info

    house_boxes = []
    for data in row_data:
        page = get_web_page(DETAIL_URL + data["url"])
        house_boxes += get_house_box(page, data["post_id"])

    save(house_boxes)
