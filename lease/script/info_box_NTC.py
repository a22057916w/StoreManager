import re
import sys
sys.path.append("lib/")
from myio import read_excel, save
from bs4 import BeautifulSoup
from wb import get_web_page
from progress_bar import progress, showProgess

DETAIL_URL = "https://rent.591.com.tw/"
urlJumpIp = 3

def get_info_box(dom, post_id):
        info_box = []
        my_list = ["post_id", "坪數", "樓層", "型態", "現況", "社區", "權狀坪數"]
        my_dict = {}.fromkeys(my_list)
        my_dict["post_id"] = post_id

        try:
            # 獲取 info_box 數據
            soup = BeautifulSoup(dom, "html.parser")
            div = soup.find("div", "detailInfo clearfix")
            attrs = div.find_all("li") # a list of info

            #解析數據
            for attr in attrs:
                key = attr.get_text().split(":")[0].strip()
                value = attr.get_text().split(":")[1].strip()
                my_dict[key] = value

            #回傳數據
            info_box.append(my_dict)
            return info_box
        except:
            print("post_id" + str(post_id) + ": 網頁資料不存在")
            info_box.append(my_dict)
            return info_box



def INFO_BOX_NTC_INIT()::
    row_data = read_excel("lease/data/NTC/info/total_rows_NTC.xlsx") # get the excel info

    info_boxes = []
    for data in row_data:
        page = get_web_page(DETAIL_URL + data["url"], urlJumpIp)
        info_boxes += get_info_box(page, data["post_id"])
        showProgess(__file__)

    save(info_boxes, "lease/data/NTC/info/info_box_NTC")
    print(str(__file__) + " complete")
