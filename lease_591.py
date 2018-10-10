import requests
from bs4 import BeautifulSoup
import time
import os
import re
import urllib.request
import json

LEASE_URL = "https://business.591.com.tw/home/search/rsList?is_new_list=1&storeType=1&type=1&kind=5&searchtype=1&region=1&firstRow=0&totalRows=2775"

def get_web_page(url):
    time.sleep(0.5)
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


if __name__ == "__main__":
    current_page = get_web_page(LEASE_URL) # return a dict of dict of list of dict
    data = get_info(current_page)

    for d in data:
        print(d)
