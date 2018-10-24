import requests
from bs4 import BeautifulSoup
import time
import os
import re
import urllib.request
import json
import pandas as pd

DETAIL_URL = "https://sale.591.com.tw/home/house/detail/2/"

def get_web_page(url):
    resp = requests.get(url=url, headers={'User-Agent': 'Custom'}, cookies={"urlJumpIp": "1"})
    if resp.status_code != 200:
        print("Invalid url:", resp.url)
        return None
    else:
        return resp.text

def read_excel():
    try:
        df = pd.read_excel("sells/data/total_rows_TPE.xlsx")
        my_dict = df.to_dict("records")
    except Exception as e:
        print(e)

    return my_dict  # return a list of dict

def get_house_box(dom, post_id):
    house_boxes = []

    soup = BeautifulSoup(dom, "html.parser")
    names = soup.find_all("div", "detail-house-name")
    contents = soup.find_all("div", "detail-house-content") # get all houes box data

    cLen = len(contents)
    nLen = len(names)
    str = [""] * cLen

    for i in range(0, cLen): # get all info of houes contents
        items = contents[i].find_all("div", "detail-house-item")

        cnt = 0
        for item in items:
            if cnt > 0:
                str[i] += ","
            str[i] += item.get_text().replace("\n", "")
            cnt += 1

    # return house boxes info
    my_list = ["post_id", "房屋資料", "坪數說明", "生活機能", "附近交通"]
    my_dict = {}.fromkeys(my_list)

    my_dict["post_id"] = post_id
    for i in range(0, len(names)):
        my_dict[names[i].string] = str[i]

    house_boxes.append(my_dict)
    return house_boxes

def save(data):

    df = pd.DataFrame.from_dict(data)
    writer = pd.ExcelWriter('sells/data/house_box_TPE.xlsx', engine='xlsxwriter')
    df.to_excel(writer, sheet_name='sells_house_box_data')
    writer.save()

    with open('sells/data/house_box_TPE.json', 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, sort_keys=True, ensure_ascii=False)

if __name__ == "__main__":
    row_data = read_excel() # get the excel info

    house_boxes = []
    for data in row_data:
        page = get_web_page(DETAIL_URL + data["url"])
        house_boxes += get_house_box(page, data["post_id"])

    save(house_boxes)
