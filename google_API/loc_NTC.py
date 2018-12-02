import googlemaps
import sys
sys.path.append("script/")
from datetime import datetime
from myio import read_excel, save

def get_geoLoc(row_data):
    geo_result = []

    for data in row_data:
        coordinate = gmaps.geocode(data["addr"])[0]["geometry"]["location"]
        print(data["addr"] + ":", coordinate)
        geo_result.append({
            "addr": data["addr"],
            "coordinate": coordinate
        })
    return geo_result

if __name__ == "__main__":
    gmaps = googlemaps.Client(key = "AIzaSyDcixkMKgROY2tE_4VLPTioPtDOwbmzfcI")

    # data for lease NTC
    row_data = read_excel("lease/data/total_rows_NTC.xlsx")
    geo_result = get_geoLoc(row_data)
    save(geo_result, "lease/data/loc_NTC")
    # data for sells NTC
    row_data = read_excel("sells/data/total_rows_NTC.xlsx")
    geo_result = get_geoLoc(row_data)
    save(geo_result, "sells/data/loc_NTC")
