package test.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fav extends AppCompatActivity {
    private static final int EDIT_CODE = 31;
    private String[] price, area, addr, avg_score;
    private String[] post_id, region, rent;//接收上一頁變數(要傳到下一頁)
    private Intent mIntent;
    private ImageView imgBack;
    private TextView goToMap, noCol, rentType;
    private boolean reloadNeed = true, isFav;

    private int iType;
    private String sType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        //宣告元件
        findViews();

        //引入全域變數
        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        iType = globalVariable.iType;
        sType = globalVariable.sType;
        rentType.setText(sType);

        //連接mysql
        getFav();

        //*************************************下拉式選單******************************************
        Spinner spinner = (Spinner) findViewById(R.id.field_item_spinner_content);

        //资源转[]
        final String meinv[] = getResources().getStringArray(R.array.rents);

        //构造ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, meinv);
        //设置下拉样式以后显示的样式
        adapter.setDropDownViewResource(R.layout.my_drop_down_item);

        spinner.setAdapter(adapter);
        // prevent go to OnItemSelectedListener on create activity, ONLY when user select
        spinner.setSelection(0,false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //引入全域變數
                GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                if(meinv[position].equals("出租")) {
                    if(sType.equals(meinv[position]))
                        return;
                    globalVariable.iType = 1;
                }
                else if(meinv[position].equals("出售")) {
                    if(sType.equals(meinv[position]))
                        return;
                    globalVariable.iType = 2;
                }
                else {
                    if(sType.equals(meinv[position]))
                        return;
                    globalVariable.iType = 0;
                }
                globalVariable.sType = meinv[position];

                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                setResult(Activity.RESULT_OK, intent); // or RESULT_CANCELED if user did not make any changes
                finish();
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //*******************************客製化listview******************************************
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
                bundle.putString("region", region[position]);
                bundle.putString("rent", rent[position]);

                mIntent = new Intent(Fav.this, MAP.class);
                mIntent.putExtras(bundle);
                startActivityForResult(mIntent, EDIT_CODE);
            }
        });

        //監聽元件
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent); // or RESULT_CANCELED if user did not make any changes
                // it would be better to use some custom defined codes here
                finish();
            }
        });

        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFav) {
                    //引入全域變數
                    GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                    //將目前的租售狀態存到global，在FavMap引用
                    globalVariable.mType = sType;
                    globalVariable.iMType = iType;

                    mIntent = new Intent(Fav.this, FavMap.class);
                    startActivityForResult(mIntent, EDIT_CODE);
                }
                else {
                    //產生視窗物件
                    new AlertDialog.Builder(Fav.this)
                            .setTitle("錯誤!")  //設定視窗標題
                            .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                            .setMessage("目前無任何收藏")//設定顯示的文字
                            .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })//設定結束的子視窗
                            .show();//呈現對話視窗
                }
            }
        });

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

    @Override
    public void onResume() {
        super.onResume();

        if (this.reloadNeed) {
            //宣告元件
            findViews();

            //引入全域變數
            GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
            iType = globalVariable.iType;
            sType = globalVariable.sType;
            rentType.setText(sType);

            //連接mysql
            getFav();

            //*******************************客製化listview******************************************
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
                    bundle.putString("region", region[position]);
                    bundle.putString("rent", rent[position]);

                    mIntent = new Intent(Fav.this, MAP.class);
                    mIntent.putExtras(bundle);
                    startActivityForResult(mIntent, EDIT_CODE);
                }
            });

            //監聽元件
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent); // or RESULT_CANCELED if user did not make any changes
                    // it would be better to use some custom defined codes here
                    finish();
                }
            });

            goToMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isFav) {
                        //引入全域變數
                        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                        //將目前的租售狀態存到global，在FavMap引用
                        globalVariable.mType = sType;
                        globalVariable.iMType = iType;

                        mIntent = new Intent(Fav.this, FavMap.class);
                        startActivityForResult(mIntent, EDIT_CODE);
                    }
                    else {
                        //產生視窗物件
                        new AlertDialog.Builder(Fav.this)
                                .setTitle("錯誤!")  //設定視窗標題
                                .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                                .setMessage("目前無任何收藏")//設定顯示的文字
                                .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })//設定結束的子視窗
                                .show();//呈現對話視窗
                    }
                }
            });

        }

        this.reloadNeed = false; // do not reload anymore, unless I tell you so...
    }

    @Override
    public void onBackPressed() {
        //引入全域變數
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent); // or RESULT_CANCELED if user did not make any changes
        // it would be better to use some custom defined codes here
        finish();
    }

    private void getFav() {
        try {
            String result = DB_FETCH_COLLECTION.executeQuery();
            //如果沒資料
            if(result.equals("-1")) {
                isFav = false;
                noCol.setVisibility(View.VISIBLE);
                return;
            }
            else {
                isFav = true;
                noCol.setVisibility(View.INVISIBLE);
                //列出所有
                if(iType == 0) {
                    //解析資料
                    JSONArray jsonArray = new JSONArray(result);
                    price = new String[jsonArray.length()];
                    addr = new String[jsonArray.length()];
                    area = new String[jsonArray.length()];
                    post_id = new String[jsonArray.length()];
                    avg_score = new String[jsonArray.length()];
                    rent = new String[jsonArray.length()];
                    region = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        price[i] = jsonData.getString("price");
                        addr[i] = jsonData.getString("addr");
                        area[i] = jsonData.getString("area");
                        avg_score[i] = jsonData.getString("avg_score");
                        post_id[i] = jsonData.getString("post_id");
                        rent[i] = jsonData.getString("rent");
                        region[i] = jsonData.getString("region");

                    }
                }
                //只列出租
                else if(iType == 1) {
                    JSONArray jsonArray = new JSONArray(result);
                    int cnt = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        if(jsonData.getString("rent").equals("lease"))
                            cnt++;
                    }
                    //check if data exist
                    if(cnt == 0) {
                        isFav = false;
                        noCol.setVisibility(View.VISIBLE);
                        return;
                    }
                    else {
                        price = new String[cnt];
                        addr = new String[cnt];
                        area = new String[cnt];
                        post_id = new String[cnt];
                        avg_score = new String[cnt];
                        rent = new String[cnt];
                        region = new String[cnt];

                        cnt = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonData = jsonArray.getJSONObject(i);
                            if(jsonData.getString("rent").equals("lease")) {
                                price[cnt] = jsonData.getString("price");
                                addr[cnt] = jsonData.getString("addr");
                                area[cnt] = jsonData.getString("area");
                                avg_score[cnt] = jsonData.getString("avg_score");
                                post_id[cnt] = jsonData.getString("post_id");
                                rent[cnt] = jsonData.getString("rent");
                                region[cnt] = jsonData.getString("region");
                                cnt++;
                            }
                        }
                    }
                }
                //只列出售
                else {
                    JSONArray jsonArray = new JSONArray(result);
                    int cnt = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        if(jsonData.getString("rent").equals("lease"))
                            continue;
                        cnt++;
                    }
                    //check if data exist
                    if(cnt == 0) {
                        isFav = false;
                        noCol.setVisibility(View.VISIBLE);
                        return;
                    }
                    else {
                        price = new String[cnt];
                        addr = new String[cnt];
                        area = new String[cnt];
                        post_id = new String[cnt];
                        avg_score = new String[cnt];
                        rent = new String[cnt];
                        region = new String[cnt];

                        cnt = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonData = jsonArray.getJSONObject(i);
                            if(jsonData.getString("rent").equals("lease"))
                                continue;
                            price[cnt] = jsonData.getString("price");
                            addr[cnt] = jsonData.getString("addr");
                            area[cnt] = jsonData.getString("area");
                            avg_score[cnt] = jsonData.getString("avg_score");
                            post_id[cnt] = jsonData.getString("post_id");
                            rent[cnt] = jsonData.getString("rent");
                            region[cnt] = jsonData.getString("region");
                            cnt++;

                        }
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void findViews() {
        imgBack = findViewById(R.id.back);
        goToMap = findViewById(R.id.goToMap);
        noCol = findViewById(R.id.noCol);
        rentType = findViewById(R.id.rents);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(isFav)
                return price.length;
            else
                return 0;
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

            ImageView imageView =  view.findViewById(R.id.imageView);
            TextView textView_name =  view.findViewById(R.id.textView_name);
            TextView textView_description =  view.findViewById(R.id.textView_description);
            TextView textView_score =  view.findViewById(R.id.textView_score);
            TextView textView_price =  view.findViewById(R.id.textView_price);
            TextView textView_postID =  view.findViewById(R.id.textView_post_id);

            String url = "http://140.136.149.235/img/" + rent[i] + "/" + region[i] + "/" + post_id[i] + "/" + "0.jpg";
            Picasso.with(getApplicationContext())
                    .load(url)
                    .error(R.drawable.dong)
                    .into(imageView);

            textView_name.setText(addr[i]);
            textView_description.setText(area[i] + "坪");
            textView_score.setText(avg_score[i]);
            if (rent[i].equals("lease"))
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
