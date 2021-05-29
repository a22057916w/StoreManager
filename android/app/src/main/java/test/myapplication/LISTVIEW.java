package test.myapplication;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LISTVIEW extends AppCompatActivity {
    private static final int EDIT_CODE = 30;
    private String[] price, area, addr, post_id, avg_score, score;
    private String php_price, php_area, php_region, php_type, php_rent;
    private String region, rent;//接收上一頁變數(要傳到下一頁)
    //private String searchType;
    private Intent mIntent;
    private ImageView imgBack, imgCol;
    private TextView favCount, search_result;
    private int favCounter = 0;
    private boolean reloadNeed = true;

    private String rType;
    private int iRType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        //宣告元件
        findViews();
        //接收MainActivity變數
        setBundle();
        //取得收藏數量
        setFavCount();
        //引入全域變數
        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        rType = globalVariable.rType;
        iRType = globalVariable.iRType;
        search_result.setText(rType);


        //*************************************下拉式選單******************************************
        Spinner spinner = (Spinner) findViewById(R.id.field_item_spinner_content);

        //资源转[]
        final String meinv[] = getResources().getStringArray(R.array.meinv);

        //构造ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, meinv);
        //设置下拉样式以后显示的样式
        adapter.setDropDownViewResource(R.layout.my_drop_down_item);

        spinner.setAdapter(adapter);
        // prevent go to OnItemSelectedListener on create activity, ONLY when user select
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //引入全域變數
                GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                if (meinv[position].equals("捷運排名")) {
                    if (rType.equals(meinv[position]))
                        return;
                    globalVariable.iRType = 1;
                } else if (meinv[position].equals("生活排名")) {
                    if (rType.equals(meinv[position]))
                        return;
                    globalVariable.iRType = 2;
                } else {
                    if (rType.equals(meinv[position]))
                        return;
                    globalVariable.iRType = 0;
                }
                globalVariable.iRType = 0;
                globalVariable.rType = meinv[position];

                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                setResult(Activity.RESULT_OK, intent);
                finish();
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //*************************************客製化listview*************************************
        ListView listView = findViewById(R.id.listview);
        //設定客製化Listview
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        //設定點擊跳頁及傳值
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //傳值到MAP
                Bundle bundle = new Bundle();
                bundle.putString("post_id", post_id[position]);
                bundle.putString("region", region);
                bundle.putString("rent", rent);

                mIntent = new Intent(LISTVIEW.this, MAP.class);
                mIntent.putExtras(bundle);
                startActivityForResult(mIntent, EDIT_CODE);
            }
        });

        //監聽元件
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LISTVIEW.super.finish();
            }
        });
        imgCol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(LISTVIEW.this, Fav.class);
                startActivityForResult(mIntent, EDIT_CODE);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if (this.reloadNeed) {
            //宣告元件
            findViews();
            //接收MainActivity變數
            setBundle();
            //取得收藏數量
            setFavCount();

            getSearchResult(rType);
            //*************************************客製化listview*************************************
            ListView listView = findViewById(R.id.listview);
            //設定客製化Listview
            CustomAdapter customAdapter = new CustomAdapter();
            listView.setAdapter(customAdapter);
            //設定點擊跳頁及傳值
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    //傳值到MAP
                    Bundle bundle = new Bundle();
                    bundle.putString("post_id", post_id[position]);
                    bundle.putString("region", region);
                    bundle.putString("rent", rent);

                    mIntent = new Intent(LISTVIEW.this, MAP.class);
                    mIntent.putExtras(bundle);
                    startActivityForResult(mIntent, EDIT_CODE);
                }
            });

            //監聽元件
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LISTVIEW.super.finish();
                }
            });
            imgCol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIntent = new Intent(LISTVIEW.this, Fav.class);
                    startActivityForResult(mIntent, EDIT_CODE);
                }
            });

        }
        this.reloadNeed = false; // do not reload anymore, unless I tell you so...
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_CODE) { // Ah! We are back from EditActivity, did we make any changes?
            if (resultCode == Activity.RESULT_OK) {
                // Yes we did! Let's allow onResume() to reload the data
                this.reloadNeed = true;
            }
        }
    }

    private void getSearchResult(String searchType) {
        //引入全域變數
        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        //給予參數
        php_area = globalVariable.key_area;
        php_price = globalVariable.key_price;
        php_region = globalVariable.key_local;
        php_rent = globalVariable.key_rent;
        php_type = globalVariable.key_type;

        if (searchType.equals("綜合排名"))
            searchType = "0";
        else if (searchType.equals("捷運排名"))
            searchType = "1";
        else
            searchType = "2";
        //php連線
        try {
            //連結php
            String result = DB_SEARCH_CON.executeQuery(php_price, php_area, php_region, php_type, php_rent, searchType);
            //Check if there is a result
            if (result.equals("-1")) {
            }
            //解析資料
            JSONArray jsonArray = new JSONArray(result);
            price = new String[jsonArray.length()];
            addr = new String[jsonArray.length()];
            area = new String[jsonArray.length()];
            post_id = new String[jsonArray.length()];
            avg_score = new String[jsonArray.length()];
            score = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                price[i] = jsonData.getString("price");
                addr[i] = jsonData.getString("addr");
                area[i] = jsonData.getString("area");
                avg_score[i] = jsonData.getString("avg_score");
                post_id[i] = jsonData.getString("post_id");

                if(rType.equals("捷運排名"))
                    score[i] = jsonData.getString("mrt_score");
                else if(rType.equals("生活排名"))
                    score[i] = jsonData.getString("l_score");
                else
                    score[i] = avg_score[i];
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setFavCount() {
        favCounter = getFavCount();
        favCount.setText(Integer.toString(favCounter));
    }

    private int getFavCount() {
        String result = DB_FETCH_COLLECTION.executeQuery();

        if (result.equals("-1"))
            return 0;
        else {
            try {
                JSONArray jsonArray = new JSONArray(result);
                return jsonArray.length();
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        }

    }

    private void findViews() {
        imgBack = findViewById(R.id.back);
        imgCol = findViewById(R.id.collection);
        search_result = findViewById(R.id.result);
        favCount = findViewById(R.id.FavConut);
    }

    private void setBundle() {
        Bundle bundle = getIntent().getExtras();
        //接收要傳到下一頁的變數
        region = bundle.getString("region");
        rent = bundle.getString("rent");

        post_id = bundle.getStringArray("post_id");
        price = bundle.getStringArray("price");
        addr = bundle.getStringArray("addr");
        avg_score = bundle.getStringArray("avg_score");
        area = bundle.getStringArray("area");
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return price.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.customlayout, null);

            ImageView star = (ImageView) view.findViewById(R.id.star);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
            TextView textView_description = (TextView) view.findViewById(R.id.textView_description);
            TextView textView_score = (TextView) view.findViewById(R.id.textView_score);
            TextView textView_price = (TextView) view.findViewById(R.id.textView_price);
            TextView textView_postID = (TextView) view.findViewById(R.id.textView_post_id);

            String url = "http://140.136.149.235/img/" + rent + "/" + region + "/" + post_id[i] + "/" + "0.jpg";
            // Picasso.with(getApplicationContext()).load(url).into(imageView);
            Picasso.with(getApplicationContext())
                    .load(url)
                    .error(R.drawable.nopic)
                    .into(imageView);

            if(rType.equals("捷運排名"))
                star.setImageResource(R.drawable.mrt);
            else if(rType.equals("生活排名"))
                star.setImageResource(R.drawable.life);
            else star.setImageResource(R.drawable.star);

            //imageView.setImageResource(IMAGES);
            textView_name.setText(addr[i]);
            textView_description.setText(area[i] + "坪");
            textView_score.setText(score[i]);
            if (rent.equals("lease"))
                textView_price.setText(price[i] + "元/月");
            else
                textView_price.setText(price[i] + "萬元");
            // Hide post_id
            textView_postID.setText(post_id[i]);
            textView_postID.setVisibility(View.INVISIBLE);

            return view;
        }
    }
}
