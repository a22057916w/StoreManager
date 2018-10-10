import requests
from bs4 import BeautifulSoup
import time
import os
import re
import urllib.request
import json
import pandas as pd

LEASE_URL = "https://business.591.com.tw/home/search/rsList?is_new_list=1&storeType=1&type=1&kind=5&searchtype=1&region=1"
total_row = 2761
pageRow = 30

def get_web_page(url):
    resp = requests.get(url=url, headers={'User-Agent': 'Custom'})
    if resp.status_code != 200:
        print("Invalid url:", resp.url)
        return None
    else:
        return resp.text # return a dict of dict of list of dict

def get_info(page):
    dict1 = json.loads(page) # page is a dict of dict of list of dict

    data = dict1["data"]["data"]
    lease_data_info = []

    for d in data:
        lease_data_info.append({
            "post_id": d["post_id"],
            "url": "rent-detail-" + str(d["post_id"]) + ".html",
            "price": d["price"],
            "area": d["area"],
            "addr": d["region_name"] + d["section_name"] + d["street_name"]
                + d["alley_name"]
        })

    return lease_data_info

def save(row_data):
    df = pd.DataFrame.from_dict(row_data)
    writer = pd.ExcelWriter('total_rows.xlsx', engine='xlsxwriter')
    df.to_excel(writer, sheet_name='total_rows_data')
    writer.save()

    with open('total_rows.json', 'w', encoding='utf-8') as f:
        json.dump(row_data, f, indent=2, sort_keys=True, ensure_ascii=False)

if __name__ == "__main__":
    page_count = 0
    current_page = get_web_page(LEASE_URL + "&firstRow=" + str(page_count) + "&totalRows=" + str(total_row)) # return a dict of dict of list of dict
    row_data = []

    while page_count <= total_row:
        data = get_info(current_page)
        row_data += data
        page_count += pageRow
        current_page = get_web_page(LEASE_URL + "&firstRow=" + str(page_count) + "&totalRows=" + str(total_row))

    save(row_data)
