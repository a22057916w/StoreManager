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
