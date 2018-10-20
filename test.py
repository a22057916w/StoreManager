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
        return resp.text # return a dict of dict of list of dict

def read_excel():
    df = pd.read_excel("sells_total_rows_TPE.xlsx")
    my_dict = df.to_dict("records")

    return my_dict  # return a list of dict

def detail_info(row_data):
    print(row_data)
    page = get_web_page(DETAIL_URL + row_data[0]["url"])
    print(page)


if __name__ == "__main__":
    row_data = read_excel() # get the excel info
    print(row_data)
    detail_info(row_data)
