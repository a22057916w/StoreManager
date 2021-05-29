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

public class DB_HOST {
    public static String executeQuery(String post_id, String region, String rent) {
        String result = "";
        try {
            URL url = new URL("http://140.136.149.235/test/" + rent + "_host_box_" + region + ".php");

            //連線到 url網址
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost method = new HttpPost(String.valueOf(url));

            //傳值給PHP
            List<NameValuePair> vars = new ArrayList<NameValuePair>();
            vars.add(new BasicNameValuePair("post_id", post_id));
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
