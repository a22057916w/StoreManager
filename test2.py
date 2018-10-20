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
    df = pd.read_excel("sells_total_rows_TPE.xlsx")
    my_dict = df.to_dict("records")

    return my_dict  # return a list of dict

def get_articles(dom, post_id):
    info_boxes = []

    soup = BeautifulSoup(dom, "html.parser")
    info_box = soup.find("div", "info-box")

    # ********************** floor data ***********************
    floor_attr = ["樓層", "屋齡", "權狀坪數"]
    floor_data = [None] * 3

    try:
        keys = info_box.find_all("div", "info-floor-key")
        values = info_box.find_all("div", "info-floor-value")

        counts = 0
        while counts < len(values):
            for i in range(3):
                regex = r"(.*)" + re.escape(str(floor_attr[i])) + r"(.*)" #regular expression string
                if re.match(regex, values[counts].string):
                    floor_data[i] = keys[counts].get_text()
                    break
            counts += 1
    except:
        pass
    # ********************* addr data ***************************
    addr_attr = ["型態", "社區", "地址"]
    addr_data = [None] * 3

    try:
        keys = info_box.find_all("span", "info-addr-key")
        values = info_box.find_all("span", "info-addr-value")

        counts = 0
        while counts < len(values):
            for i in range(3):
                regex = r"(.*)" + re.escape(str(addr_attr[i])) + r"(.*)"
                if re.match(regex, keys[counts].string): #key and values are oppsite to floor's data
                    addr_data[i] = values[counts].get_text()
                    break
            counts += 1
    except:
        pass
    # return info
    info_boxes.append({
        "post_id": post_id,
        "floor": floor_data[0],
        "age": floor_data[1],
        "area": floor_data[2],
        "form": addr_data[0],
        "community": addr_data[1],
        "addr": addr_data[2]
    })
    return info_boxes

if __name__ == "__main__":
    row_data = read_excel() # get the excel info

    info_boxes = []
    for data in row_data:
        page = get_web_page(DETAIL_URL + data["url"])
        info_boxes += get_articles(page, data["post_id"])

    print(info_boxes)
