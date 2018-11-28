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
    resp = requests.get(url=url, headers={'User-Agent': 'Custom'}, cookies={"urlJumpIp": "1"})
    if resp.status_code != 200:
        print("Invalid url:", resp.url)
        return None
    else:
        return resp.text

def read_excel():
    try:
        df = pd.read_excel("lease/data/total_rows_TPE.xlsx")
        my_dict = df.to_dict("records")
        return my_dict  # return a list of dict

    except Exception as e:
        print(e)

def get_info_box(dom, post_id):
    info_box = []
    my_list = ["post_id", "坪數", "樓層", "型態", "現況", "社區", "權狀坪數"]
    my_dict = {}.fromkeys(my_list)
    my_dict["post_id"] = post_id

    try:
        # 獲取 info_box 數據
        soup = BeautifulSoup(dom, "html.parser")
        div = soup.find("div", "detailInfo clearfix")
        attrs = div.find_all("li") # a list of info

        #解析數據
        for attr in attrs:
            key = attr.get_text().split(":")[0].strip()
            value = attr.get_text().split(":")[1].strip()
            my_dict[key] = value

        #回傳數據
        info_box.append(my_dict)
        return info_box
    except:
        print("post_id" + str(post_id) + ": 網頁資料不存在")
        info_box.append(my_dict)
        return info_box

def save(data):

    df = pd.DataFrame.from_dict(data)
    writer = pd.ExcelWriter('lease/data/info_box_TPE.xlsx', engine='xlsxwriter')
    df.to_excel(writer, sheet_name='lease_info_box_data')
    writer.save()

    with open('lease/data/info_box_TPE.json', 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, sort_keys=True, ensure_ascii=False)

if __name__ == "__main__":
    row_data = read_excel() # get the excel info

    info_boxes = []
    for data in row_data:
        page = get_web_page(DETAIL_URL + data["url"])
        info_boxes += get_info_box(page, data["post_id"])

    save(info_boxes)
