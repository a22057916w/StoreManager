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
        df = pd.read_excel("sells/sells_total_rows_TPE.xlsx")
        my_dict = df.to_dict("records")
    except Exception as e:
        print(e)

    return my_dict  # return a list of dict

def get_house_box(dom, post_id):
    house_boxes = []

    soup = BeautifulSoup(dom, "html.parser")
    names = soup.find_all("div", "detail-house-name")
    contents = soup.find_all("div", "detail-house-content") # get all houes box data
    # ******************* first index of contents (house info)************************
    house_attr = ["現況", "裝潢程度", "管理費", "帶租約", "車位", "公設比"]
    house_data = [None] * 6

    try:
        keys = contents[0].find_all("div", "detail-house-key")
        values = contents[0].find_all("div", "detail-house-value")

        counts = 0
        while counts < len(values):
            for i in range(6):
                regex = r"(.*)" + re.escape(str(house_attr[i])) + r"(.*)" #regular expression string
                if re.match(regex, keys[counts].string):
                    house_data[i] = values[counts].get_text()
                    break
            counts += 1
    except:
        pass
    print(first_data)
    # *********************** second index of contents (area info) *********************
    area_attr = []

def save(data):

    df = pd.DataFrame.from_dict(data)
    writer = pd.ExcelWriter('sells/sells_house_box_TPE.xlsx', engine='xlsxwriter')
    df.to_excel(writer, sheet_name='sells_house_box_data')
    writer.save()

    with open('sells/sells_house_box_TPE.json', 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, sort_keys=True, ensure_ascii=False)

if __name__ == "__main__":
    row_data = read_excel() # get the excel info

    house_boxes = []
    page = get_web_page(DETAIL_URL + row_data[0]["url"])
    get_house_box(page, row_data[0]["post_id"])

    #save(house_boxes)
