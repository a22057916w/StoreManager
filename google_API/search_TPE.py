import googlemaps
import sys
sys.path.append("script/")
from myio import read_excel, save

def get_geoNearBy(locations):
    vic = []
    for loc in locations:
        print(loc["coordinate"])
        #vic.append(gmaps.places_nearby(location = loc["coordinate"], radius = 1000, type = "subway_station"))
    #print(vic[0])
    return vic


if __name__ == "__main__":
    gmaps = googlemaps.Client(key = "AIzaSyDcixkMKgROY2tE_4VLPTioPtDOwbmzfcI")

    # read location information
    leaseTPE = read_excel("lease/data/loc_TPE.xlsx")
    leaseNTC = read_excel("lease/data/loc_NTC.xlsx")
    sellsTPE = read_excel("sells/data/loc_TPE.xlsx")
    sellsNTC = read_excel("sells/data/loc_NTC.xlsx")

    locList = [leaseTPE, leaseNTC, sellsTPE, sellsNTC]
    vic = []
    for loc in locList:
        vic = get_geoNearBy(loc)
