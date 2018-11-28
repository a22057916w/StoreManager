import mysql.connector



if __name__ == "__main__":
    mydb = mysql.connector.connect(
        host = "140.136.149.235",
        user = "s22057916w",
        passwd = "40205gup",
        database = "stroemanager"
    )

    mycursor = mydb.cursor()

    mycursor.execute("SHOW DATABASES")

    for db in mycursor:
        print(db)
