package test.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MAP extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    private String post_id, region, rent, test;
    private String price, addr, area, type, floor, paper, community, info, life, traffic, mrt_score, t_score, l_score, avg_score, host_name, host_phone;

    private Button btncall;
    private TextView tvprice, tvarea, tvfloor, tvcommunity, tvtype, tvpaper, tvavg_score, tvmrt_score, tvt_score, tvl_score, tvhouse_info, tvlife, tvaddr, tvtraffic;
    private TextView hostName, hostPhone;
    private ImageView imgFav, imgBack, call;
    private ScrollView scrollview;
    private Toolbar toolbar;

    private boolean isActive = false;
    private float lat, lng;
    //private Menu menu;
    //private MenuItem call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //元件宣告
        findViews();
        //接收MainActivity變數
        setBundle();
        //連接php
        getInfo();
        //設定元件
        setViews();
        //抓圖片
        getimg();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //************************監聽各種元件*****************************
        //收藏
        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActive) {
                    isActive = false;
                    imgFav.setBackgroundResource(R.drawable.ic_favorite_border_white_24dp);
                    //移除mysql資料
                    DB_addFav.executeQuery(post_id, "0", addr.split(":")[1], area.split(":")[1], price, avg_score, rent, region);

                } else {
                    isActive = true;
                    imgFav.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
                    //丟出訊息
                    Toast toast = Toast.makeText(MAP.this, "收藏成功", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    //新增資料到mysql
                    DB_addFav.executeQuery(post_id, "1", addr.split(":")[1], area.split(":")[1], price, avg_score, rent, region);
                }
            }
        });
        //上一頁
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent); // or RESULT_CANCELED if user did not make any changes
                // it would be better to use some custom defined codes here
                finish();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(host_phone);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng location = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(location).title(addr));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14.0f));


    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent); // or RESULT_CANCELED if user did not make any changes
        // it would be better to use some custom defined codes here
        finish();
    }

    private void setViews() {
        tvprice.setText(price);
        tvarea.setText(area);
        tvaddr.setText(addr);
        tvl_score.setText(l_score);
        tvt_score.setText(t_score);
        tvmrt_score.setText(mrt_score);
        tvavg_score.setText(avg_score);
        tvcommunity.setText(community);
        tvfloor.setText(floor);
        tvlife.setText(life);
        tvtype.setText(type);
        tvpaper.setText(paper);
        tvhouse_info.setText(info);
        tvtraffic.setText(traffic);
        hostName.setText(host_name);
        hostPhone.setText(host_phone);
    }

    private void getimg() {
        HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.scrollView1);

        LinearLayout topLinearLayout = new LinearLayout(this);
        // topLinearLayout.setLayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.FILL_PARENT);
        topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < 15; i++) {
            final ImageView imageView = new ImageView(this);
            imageView.setTag(i);

            //imageView.setImageResource(R.drawable.ic_launcher);
            String url = "http://140.136.149.235/img/" + rent + "/" + region + "/" + post_id + "/" + Integer.toString(i) + ".jpg";
            Picasso.with(getApplicationContext()).load(url).into(imageView);

            topLinearLayout.addView(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.e("Tag", "" + imageView.getTag());
                }
            });

        }

        scrollView.addView(topLinearLayout);
    }

    private void findViews() {
        tvprice = findViewById(R.id.tvprice);
        tvarea = findViewById(R.id.tvarea);
        tvfloor = findViewById(R.id.tvfloor);
        tvcommunity = findViewById(R.id.tvcommunity);
        tvtype = findViewById(R.id.tvtype);
        tvpaper = findViewById(R.id.tvpaper);
        tvavg_score = findViewById(R.id.tvavg_score);
        tvmrt_score = findViewById(R.id.tvmrt_score);
        tvt_score = findViewById(R.id.tvt_score);
        tvl_score = findViewById(R.id.tvl_score);
        tvhouse_info = findViewById(R.id.tvhouse_info);
        tvlife = findViewById(R.id.tvlife);
        tvaddr = findViewById(R.id.tvaddr);
        tvtraffic = findViewById(R.id.tvtraffic);
        imgFav = findViewById(R.id.heart);
        imgBack = findViewById(R.id.back);
        hostName = findViewById(R.id.hostName);
        hostPhone = findViewById(R.id.hostPhone);
        call = findViewById(R.id.call);
    }

    private void getInfo() {
        try {
            //傳值給php並接收資料
            String result = DB_DETAIL.executeQuery(post_id, region, rent);
            //解析資料
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = new JSONObject(String.valueOf(jsonArray.getJSONObject(0)));
            if (rent.equals("lease"))
                price = jsonObject.getString("price") + "元/月";
            else
                price = jsonObject.getString("price") + "萬元";
            addr = "地址: " + jsonObject.getString("addr");
            area = "坪數: " + jsonObject.getString("坪數");
            type = "型態: " + jsonObject.getString("型態");
            floor = "樓層: " + jsonObject.getString("樓層");
            paper = "權狀: \r\n" + jsonObject.getString("坪數說明");
            community = "社區: " + jsonObject.getString("社區");
            info = "房屋資料:　\r\n" + jsonObject.getString("房屋資料");
            life = "生活機能: \r\n" + jsonObject.getString("生活機能");
            traffic = "附近交通: \r\n" + jsonObject.getString("附近交通");
            mrt_score = "捷運分數: " + jsonObject.getString("mrt_score");
            t_score = "交通分數: " + jsonObject.getString("t_score");
            l_score = "生活分數: " + jsonObject.getString("l_score");
            avg_score = jsonObject.getString("avg_score");
            //抓屋主電話和姓名
            host_name = jsonObject.getString("name");
            host_phone = jsonObject.getString("phone");

            //抓座標
            String location = DB_CORRD.executeQuery(post_id, region, rent);
            //解析資料
            JSONArray jsonArray2 = new JSONArray(location);
            JSONObject jsonObject2 = new JSONObject(String.valueOf(jsonArray2.getJSONObject(0)));

            lat = Float.parseFloat(jsonObject2.getString("lat"));
            lng = Float.parseFloat(jsonObject2.getString("lng"));

            //檢查是否有收藏
            String flag = DB_checkFav.executeQuery(post_id);
            //解析資料
            JSONArray jsonArray3 = new JSONArray(flag);
            JSONObject jsonObject3 = new JSONObject(String.valueOf(jsonArray3.getJSONObject(0)));
            String check = jsonObject3.getString("fav");
            if (check.equals("1")) {
                isActive = true;
                imgFav.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
            } else {
                isActive = false;
                imgFav.setBackgroundResource(R.drawable.ic_favorite_border_white_24dp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void call(String phonenumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phonenumber));
        startActivity(intent);
        //startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+ phonenumber)));
    }

    public void setBundle() {
        Bundle bundle = getIntent().getExtras();
        post_id = bundle.getString("post_id");
        region = bundle.getString("region");
        rent = bundle.getString("rent");
    }
}

