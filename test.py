import mysql.connector
import os
import googlemaps

if __name__ == "__main__":
    gmaps = googlemaps.Client(key = "AIzaSyDcixkMKgROY2tE_4VLPTioPtDOwbmzfcI")
    print(gmaps.places_nearby(location = (25.031995, 121.4690562), radius = 1000, type = "bus_station", language = "zh-TW"))
