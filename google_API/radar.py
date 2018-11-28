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

def save(data):

    df = pd.DataFrame.from_dict(data)
    writer = pd.ExcelWriter('lease/data/loc_NTC.xlsx', engine='xlsxwriter')
    df.to_excel(writer, sheet_name='lease_NTC_location')
    writer.save()

    with open('lease/data/loc_NTC.json', 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, sort_keys=True, ensure_ascii=False)

if __name__ == "__main__":
    row_data = read_excel()

    gmaps = googlemaps.Client(key = "AIzaSyDcixkMKgROY2tE_4VLPTioPtDOwbmzfcI")
    geo_result = []
    for data in row_data:
        coordinate = gmaps.geocode(data["addr"])[0]["geometry"]["location"]
        geo_result.append({
            "addr": data["addr"],
            "coordinate": coordinate
        })
    print(geo_result)

    save(geo_result)

    #radar_result = gmaps.places_nearby(keyword = "cafe", location = ", radius = 1000, language = "chinese")
    #print(radar_result)
