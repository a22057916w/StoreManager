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
        print("Invalid url:", resp.url, end="\n")
        return None
    else:
        data = json.loads(resp.text)
        #print(data)
        #print(data.keys())
        #print(data["data"])
        #print(data["data"].keys())
        #print(data["data"]["topData"])
        #print("******************************************************************************************************")
        #print(type(data))
        #print(type(data["data"]))
        #print(type(data["data"]["topData"]))
        print(data["data"]["topData"][0].keys())
        d = data["data"]["topData"][0]
        print(type(d))
        print(d.items())
        print(data["data"]["data"][0].items())
        """for d in data["data"]["topData"]:
            print(d.keys())"""
        return resp.text


if __name__ == "__main__":
    current_page = get_web_page(LEASE_URL)
    #print(current_page, end="\n")
