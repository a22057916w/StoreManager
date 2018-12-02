import googlemaps
from datetime import datetime
from myio import read_excel, save

if __name__ == "__main__":
    row_data = read_excel("lease/data/total_rows_TPE.xlsx")

    gmaps = googlemaps.Client(key = "AIzaSyDcixkMKgROY2tE_4VLPTioPtDOwbmzfcI")
    geo_result = []
    for data in row_data:
        coordinate = gmaps.geocode(data["addr"])[0]["geometry"]["location"]
        print(data["addr"] + ":", coordinate)
        geo_result.append({
            "addr": data["addr"],
            "coordinate": coordinate
        })
    print(geo_result)

    save(geo_result, "sells/data/loc_TPE")
