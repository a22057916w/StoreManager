import requests
from bs4 import BeautifulSoup
import time
import os
import re
import urllib.request
import json
import pandas as pd

SELLS_URL = "https://business.591.com.tw/home/search/rsList?is_new_list=1&storeType=2&type=2&kind=5&searchtype=1&region=3"
pageRow = 30

def get_web_page(url):
    resp = requests.get(url=url, headers={'User-Agent': 'Custom'}, cookies={"urlJumpIp": "3"})
    if resp.status_code != 200:
        print("Invalid url:", resp.url)
        return None
    else:
        return resp.text # return a dict of dict of list of dict

def get_info(page):
    dict1 = json.loads(page) # page is a dict of dict of list of dict

    data = dict1["data"]["data"]
    sells_data_info = []

    for d in data:
        sells_data_info.append({
            "post_id": d["post_id"],
            "url": "rent-detail-" + str(d["post_id"]) + ".html",
            "price": d["price"].replace(",", ""),
            "unit": "萬",
            "area": d["area"],
            "addr": d["region_name"] + d["section_name"] + d["street_name"]
                + d["alley_name"]
        })

    return sells_data_info

def get_total_rows(page):
    str_total = json.loads(page)["records"]
    int_total = int(str_total.replace(",", ""))

    return int_total

def save(row_data):
    df = pd.DataFrame.from_dict(row_data)
    writer = pd.ExcelWriter('sells_total_rows_NTC.xlsx', engine='xlsxwriter')
    df.to_excel(writer, sheet_name='sells_total_rows_data')
    writer.save()

    with open('sells_total_rows_NTC.json', 'w', encoding='utf-8') as f:
        json.dump(row_data, f, indent=2, sort_keys=True, ensure_ascii=False)

if __name__ == "__main__":
    current_page = get_web_page(SELLS_URL) # return a dict of dict of list of dict
    total_rows = get_total_rows(current_page)

    page_count = 0
    row_data = []

    while page_count <= total_rows:
        data = get_info(current_page)
        row_data += data
        page_count += pageRow
        current_page = get_web_page(SELLS_URL + "&firstRow=" + str(page_count) + "&totalRows=" + str(total_rows))

    save(row_data)
