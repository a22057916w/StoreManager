import mysql.connector
import os
import myio

if __name__ == "__main__":
    a = myio.read_excel("lease/data/total_rows_NTC.xlsx")
    print(a)
