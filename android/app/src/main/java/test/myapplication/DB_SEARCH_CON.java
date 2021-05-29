package test.myapplication;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DB_SEARCH_CON {
    public static String executeQuery(String price, String area, String region, String type, String rent, String searchType) {
        String result = "";

        try {
            URL url = new URL("http://140.136.149.235/test/" + rent + "_search_condition_" + region + ".php");

            //連線到 url網址
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost method = new HttpPost(String.valueOf(url));

            //傳值給PHP
            List<NameValuePair> vars = new ArrayList<NameValuePair>();
            vars.add(new BasicNameValuePair("price", price));
            vars.add(new BasicNameValuePair("area", area));
            vars.add(new BasicNameValuePair("type", type));
            vars.add(new BasicNameValuePair("search_type", searchType));
            method.setEntity(new UrlEncodedFormEntity(vars, HTTP.UTF_8));

            //接收PHP回傳的資料
            HttpResponse response = httpclient.execute(method);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (Exception e) {
            Log.e("DBConnector", e.toString());
            result = e.toString();
        }

        return result;
    }
}
