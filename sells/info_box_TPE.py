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
        return my_dict  # return a list of dict

    except Exception as e:
        print(e)

def get_info_box(dom, post_id):
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
    # return info and get price
    # handle unexpected string of numbers
    regex = re.escape(soup.find("span", "info-price-num").get_text())
    match = re.findall(r"[0-9]", regex)
    price = int("".join(map(str, match)))

    #handling unexpected string of area to eval
    match = re.findall(r"[0-9]+\.*[0-9]*", floor_data[2])
    area = eval("".join(map(str, match)))

    info_boxes.append({
        "post_id": post_id,
        "price": price,
        "floor": floor_data[0],
        "age": floor_data[1],
        "area": area,
        "form": addr_data[0],
        "community": addr_data[1],
        "addr": addr_data[2]
    })
    return info_boxes

def save(data):

    df = pd.DataFrame.from_dict(data)
    writer = pd.ExcelWriter('sells/data/info_box_TPE.xlsx', engine='xlsxwriter')
    df.to_excel(writer, sheet_name='sells_info_box_data')
    writer.save()

    with open('sells/data/info_box_TPE.json', 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, sort_keys=True, ensure_ascii=False)

if __name__ == "__main__":
    row_data = read_excel() # get the excel info

    info_boxes = []
    for data in row_data:
        page = get_web_page(DETAIL_URL + data["url"])
        info_boxes += get_info_box(page, data["post_id"])

    save(info_boxes)
