package test.myapplication;

import android.app.Application;

public class GlobalVariable extends Application {
    public String main_local = "地區";
    public String key_local = "";

    public String main_rent = "租售";
    public String key_rent = "";

    public String main_type = "店家類型";
    public String key_type = "-1";

    public String main_price = "價錢";
    public String key_price = "-1";

    public String main_area = "坪數";
    public String key_area = "-1";

    //for activity Fav(Mainly) and FavMap
    public String sType = "租售";
    public int iType = 0;

    //for activity FavMap(Mainly) and Fav
    public String mType = "租售";
    public int iMType = 0;
    public float lat, lng;

    //for activity LISTVIEW
    public String rType = "綜合排名";
    public int iRType = -1;
}
