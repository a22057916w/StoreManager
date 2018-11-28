def read_excel(fname):
    try:
        df = pd.read_excel(fname)
        my_dict = df.to_dict("records")
        return my_dict  # return a list of dict

    except Exception as e:
        print(e)

def save(data, fname):

    df = pd.DataFrame.from_dict(data)
    writer = pd.ExcelWriter(fname + ".xlsx", engine='xlsxwriter')
    df.to_excel(writer, sheet_name='lease_NTC_location')
    writer.save()

    with open(fname + ".json", 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, sort_keys=True, ensure_ascii=False)
