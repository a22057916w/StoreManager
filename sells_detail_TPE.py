import requests
from bs4 import BeautifulSoup
import time
import os
import re
import urllib.request
import json
import pandas as pd

SELLS_URL = "https://business.591.com.tw/home/search/rsList?is_new_list=1&storeType=2&type=2&kind=5&searchtype=1&region=1"
DETAIL_URL = "https://sale.591.com.tw/home/house/detail/2/"
pageRow = 30

def get_web_page(url):
    resp = requests.get(url=url, headers={'User-Agent': 'Custom'}, cookies={"urlJumpIp": "1"})
    if resp.status_code != 200:
        print("Invalid url:", resp.url)
        return None
    else:
        return resp.text # return a dict of dict of list of dict

def get_detail_url(page):
    dict1 = json.loads(page) # page is a dict of dict of list of dict

    data = dict1["data"]["data"]
    sells_data_info = []

    for d in data:
        sells_data_info.append({
            "post_id": d["post_id"],
            "url": str(d["post_id"]) + ".html"
        })

    return sells_data_info

def get_total_rows(page):
    str_total = json.loads(page)["records"]
    int_total = int(str_total.replace(",", ""))

    return int_total


if __name__ == "__main__":
    current_page = get_web_page(SELLS_URL) # return a dict of dict of list of dict
    total_rows = get_total_rows(current_page)

    page_count = 0

    while page_count <= total_rows:
        detail_url = get_detail_url(current_page)
        page_count += pageRow
        current_page = get_web_page(SELLS_URL + "&firstRow=" + str(page_count) + "&totalRows=" + str(total_rows))

    #save(row_data)
