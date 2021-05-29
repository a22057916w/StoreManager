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

public class DB_addFav {
    public static void executeQuery(String post_id, String isFav, String addr, String area, String price, String avg_score, String rent, String region) {
        String result = "";
        try {
            URL url = new URL("http://140.136.149.235/test/addFav.php");

            //連線到 url網址
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost method = new HttpPost(String.valueOf(url));

            //傳值給PHP
            List<NameValuePair> vars = new ArrayList<NameValuePair>();
            vars.add(new BasicNameValuePair("post_id", post_id));
            vars.add(new BasicNameValuePair("isFav", isFav));
            vars.add(new BasicNameValuePair("addr", addr));
            vars.add(new BasicNameValuePair("area", area));
            vars.add(new BasicNameValuePair("price", price));
            vars.add(new BasicNameValuePair("avg_score", avg_score));
            vars.add(new BasicNameValuePair("rent", rent));
            vars.add(new BasicNameValuePair("region", region));
            method.setEntity(new UrlEncodedFormEntity(vars, HTTP.UTF_8));

            //接收PHP回傳的資料
            HttpResponse response = httpclient.execute(method);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (Exception e) {
            Log.e("DBConnector", e.toString());
            result = e.toString();
        }
    }
}
