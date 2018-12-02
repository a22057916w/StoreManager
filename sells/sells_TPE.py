import sys
sys.path.append("script/")
from bs4 import BeautifulSoup
from wb import get_web_page
import time
import os
import re
import urllib.request
import json
import pandas as pd

SELLS_URL = "https://business.591.com.tw/home/search/rsList?is_new_list=1&storeType=2&type=2&kind=5&searchtype=1&region=1"
pageRow = 30
urlJumpIp = 1


def get_info(page):
    dict1 = json.loads(page) # page is a dict of dict of list of dict

    data = dict1["data"]["data"]
    sells_data_info = []

    for d in data:
        sells_data_info.append({
            "post_id": d["post_id"],
            "price": int(d["price"].replace(",", "")),
            "area": d["area"],
            "url": str(d["post_id"]) + ".html",
            "addr": d["region_name"] + d["section_name"] + d["street_name"]
                + d["alley_name"]
        })

    return sells_data_info

def get_total_rows(page):
    str_total = json.loads(page)["records"]
    int_total = int(str_total.replace(",", ""))

    return int_total

def save(row_data):
    os.makedirs("sells/data", exist_ok=True)

    df = pd.DataFrame.from_dict(row_data)
    writer = pd.ExcelWriter('sells/data/total_rows_TPE.xlsx', engine='xlsxwriter')
    df.to_excel(writer, sheet_name='sells_total_rows_data')
    writer.save()

    with open('sells/data/total_rows_TPE.json', 'w', encoding='utf-8') as f:
        json.dump(row_data, f, indent=2, sort_keys=True, ensure_ascii=False)

if __name__ == "__main__":
    current_page = get_web_page(SELLS_URL, urlJumpIp) # return a dict of dict of list of dict
    total_rows = get_total_rows(current_page)

    page_count = 0
    row_data = []

    while page_count <= total_rows:
        data = get_info(current_page)
        row_data += data
        page_count += pageRow
        current_page = get_web_page(SELLS_URL + "&firstRow=" + str(page_count) + "&totalRows=" + str(total_rows), urlJumpIp)

    save(row_data)
