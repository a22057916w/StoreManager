import googlemaps
import pandas as pd
from datetime import datetime

def read_excel():
    try:
        df = pd.read_excel("lease/data/total_rows_NTC.xlsx")
        my_dict = df.to_dict("records")
        return my_dict  # return a list of dict

    except Exception as e:
        print(e)

"""def save(data):

    df = pd.DataFrame.from_dict(data)
    writer = pd.ExcelWriter('lease/data/house_box_TPE.xlsx', engine='xlsxwriter')
    df.to_excel(writer, sheet_name='lease_house_box_data')
    writer.save()

    with open('lease/data/house_box_TPE.json', 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, sort_keys=True, ensure_ascii=False)"""

if __name__ == "__main__":
    row_data = read_excel()

    gmaps = googlemaps.Client(key = "AIzaSyDcixkMKgROY2tE_4VLPTioPtDOwbmzfcI")
    mrt_station = []
    geo_result = []
    for data in row_data:
        geo_result.append(gmaps.geocode(data["addr"]))
    for geo_loc in geo_result:
        print(geo_loc[0]["geometry"]["location"])
    #radar_result = gmaps.places_nearby(keyword = "cafe", location = ", radius = 1000, language = "chinese")
    #print(radar_result)
