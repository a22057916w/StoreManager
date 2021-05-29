package test.myapplication;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_local, btn_rent, btn_type, btn_price, btn_square, btn_search;
    private Intent mIntent;

    private String[] price, area, addr, post_id, avg_score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //元件宣告
        findViews();
        //監聽按鍵
        setOnClickListener();
        //設定button文字
        setButtonText();


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    private void setButtonText() {
        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        btn_local.setText(globalVariable.main_local);
        btn_price.setText(globalVariable.main_price);
        btn_rent.setText(globalVariable.main_rent);
        btn_type.setText(globalVariable.main_type);
        btn_square.setText(globalVariable.main_area);
    }

    private void findViews() {
        btn_local = findViewById(R.id.btn_local);
        btn_rent = findViewById(R.id.btn_rent);
        btn_type = findViewById(R.id.btn_type);
        btn_price = findViewById(R.id.btn_price);
        btn_square = findViewById(R.id.btn_square);
        btn_search = findViewById(R.id.btn_search);
    }

    private void setOnClickListener() {
        btn_local.setOnClickListener(this);
        btn_rent.setOnClickListener(this);
        btn_type.setOnClickListener(this);
        btn_price.setOnClickListener(this);
        btn_square.setOnClickListener(this);
        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //引入全域變數
        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        switch (v.getId()) {
            case R.id.btn_local:
                mIntent = new Intent(MainActivity.this, local.class);
                startActivity(mIntent);
                break;
            case R.id.btn_rent:
                //若選擇租售，則初始化價錢和坪數(rent.class)
                mIntent = new Intent(MainActivity.this, rent.class);
                startActivity(mIntent);
                break;
            case R.id.btn_type:
                mIntent = new Intent(MainActivity.this, type.class);
                startActivity(mIntent);
                break;
            case R.id.btn_price:
                if (globalVariable.key_rent == "") {
                    //產生視窗物件
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("錯誤!")  //設定視窗標題
                            .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                            .setMessage("請先選擇租或售")//設定顯示的文字
                            .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })//設定結束的子視窗
                            .show();//呈現對話視窗*/
                    break;
                } else if (globalVariable.key_rent == "lease") {
                    mIntent = new Intent(MainActivity.this, price.class);
                    startActivity(mIntent);
                    break;
                } else {
                    mIntent = new Intent(MainActivity.this, sell_price.class);
                    startActivity(mIntent);
                    break;
                }
            case R.id.btn_square:
                mIntent = new Intent(MainActivity.this, square.class);
                startActivity(mIntent);
                break;
            case R.id.btn_search:
                //獲取要傳給php的值(會先傳到LISTVIEW再連)
                String php_price, php_area, php_region, php_type, php_rent;

                php_price = globalVariable.key_price;
                php_area = globalVariable.key_area;
                php_region = globalVariable.key_local;
                php_type = globalVariable.key_type;
                php_rent = globalVariable.key_rent;

                //確認是否選取php_region
                if (php_region == "") {
                    //產生視窗物件
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("錯誤!")  //設定視窗標題
                            .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                            .setMessage("請選擇地區")//設定顯示的文字
                            .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })//設定結束的子視窗
                            .show();//呈現對話視窗*/
                    break;
                }
                //確認是否選取php_rent
                if (php_rent == "") {
                    //產生視窗物件
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("錯誤!")  //設定視窗標題
                            .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                            .setMessage("請選擇租或售")//設定顯示的文字
                            .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })//設定結束的子視窗
                            .show();//呈現對話視窗*/
                    break;
                }
                //php連線
                try {
                    //連結php
                    String result = DB_SEARCH.executeQuery(php_price, php_area, php_region, php_type, php_rent);
                    //Check if there is a result
                    if (result.equals("-1")) {
                        //產生視窗物件
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("錯誤!")  //設定視窗標題
                                .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                                .setMessage("查無搜尋結果")//設定顯示的文字
                                .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })//設定結束的子視窗
                                .show();//呈現對話視窗
                        break;
                    }
                    //解析資料
                    JSONArray jsonArray = new JSONArray(result);
                    price = new String[jsonArray.length()];
                    addr = new String[jsonArray.length()];
                    area = new String[jsonArray.length()];
                    post_id = new String[jsonArray.length()];
                    avg_score = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        price[i] = jsonData.getString("price");
                        addr[i] = jsonData.getString("addr");
                        area[i] = jsonData.getString("area");
                        avg_score[i] = jsonData.getString("avg_score");
                        post_id[i] = jsonData.getString("post_id");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Bundle bundle = new Bundle();
                pushBundle(bundle, php_region, php_rent, php_type, price, addr, area, avg_score, post_id);

                mIntent = new Intent(MainActivity.this, LISTVIEW.class);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
                break;


        }
    }

    private void pushBundle(Bundle bundle, String php_region, String php_rent, String php_type, String[] price, String[] addr, String[] area, String[] avg_score, String[] post_id) {
        //還要繼續傳到下下頁的變數
        bundle.putString("type", php_type);
        bundle.putString("region", php_region);
        bundle.putString("rent", php_rent);
        //傳到下頁的變數
        bundle.putStringArray("price", price);
        bundle.putStringArray("addr", addr);
        bundle.putStringArray("area", area);
        bundle.putStringArray("avg_score", avg_score);
        bundle.putStringArray("post_id", post_id);

    }

}