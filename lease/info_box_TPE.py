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
        df = pd.read_excel("lease/data/total_rows_TPE.xlsx")
        my_dict = df.to_dict("records")
    except Exception as e:
        print(e)

    return my_dict  # return a list of dict

def get_info_box(dom, post_id):
    info_boxes = []

    soup = BeautifulSoup(dom, "html.parser")
    div = soup.find("div", "detailInfo clearfix")
    attr = div.find_all("li") # a list of info


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
