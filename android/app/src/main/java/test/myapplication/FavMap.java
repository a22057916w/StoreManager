package test.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FavMap extends FragmentActivity implements OnMapReadyCallback {
    private static final int EDIT_CODE = 33;
    private GoogleMap mMap;
    private String[] post_id, region, rent, addr, avg_score;
    private float[] lat, lng;
    private boolean reloadNeed = false, isFav;
    private String mType;
    private int iMType;
    private TextView rentType;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_map);

        findViews();
        //引入全域變數
        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        iMType = globalVariable.iMType;
        mType = globalVariable.mType;
        rentType.setText(mType);

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
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //引入全域變數
                GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                if (meinv[position].equals("出租")) {
                    if (mType.equals(meinv[position]))
                        return;
                    globalVariable.iMType = 1;
                } else if (meinv[position].equals("出售")) {
                    if (mType.equals(meinv[position]))
                        return;
                    globalVariable.iMType = 2;
                } else {
                    if (mType.equals(meinv[position]))
                        return;
                    globalVariable.iMType = 0;
                }
                globalVariable.mType = meinv[position];

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
        getFav();

        if(isFav) {
            //初始化經緯度陣列
            lat = new float[post_id.length];
            lng = new float[post_id.length];
            getLoction(lat, lng);
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //監聽元件
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //引入全域變數
                GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                globalVariable.sType = mType;
                globalVariable.iType = iMType;

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent); // or RESULT_CANCELED if user did not make any changes
                // it would be better to use some custom defined codes here
                finish();
            }
        });
    }


    private void findViews() {
        imgBack = findViewById(R.id.back);
        rentType = findViewById(R.id.result);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //引入全域變數
        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();

        mMap = googleMap;

        if (isFav) {
            LatLng[] locations = new LatLng[post_id.length];
            for (int i = 0; i < locations.length; i++) {
                locations[i] = new LatLng(lat[i], lng[i]);
                mMap.addMarker(new MarkerOptions().position(locations[i]).title(addr[i]));
            }

            globalVariable.lat = lat[0];
            globalVariable.lng = lng[0];

            //LatLng locations = new LatLng(-34, 101);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[0], 12.0f));

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    // TODO Auto-generated method stub
                    String address = marker.getTitle();
                    for (int i = 0; i < addr.length; i++) {
                        if (address.equals(addr[i])) {
                            //傳值到MAP
                            Bundle bundle = new Bundle();
                            bundle.putString("post_id", post_id[i]);
                            bundle.putString("region", region[i]);
                            bundle.putString("rent", rent[i]);

                            Intent mIntent = new Intent(FavMap.this, MAP.class);
                            mIntent.putExtras(bundle);
                            startActivityForResult(mIntent, EDIT_CODE);
                            break;
                        }
                    }
                }
            });
        } else {
            LatLng defalut = new LatLng(globalVariable.lat, globalVariable.lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defalut, 12.0f));
            mMap.clear();
        }
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
            //refresh google map
            mMap.clear();

            findViews();

            getFav();
            if(isFav) {
                //初始化經緯度陣列
                lat = new float[post_id.length];
                lng = new float[post_id.length];
                getLoction(lat, lng);
            }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            //監聽元件
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //引入全域變數
                    GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
                    globalVariable.sType = mType;
                    globalVariable.iType = iMType;

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent); // or RESULT_CANCELED if user did not make any changes
                    // it would be better to use some custom defined codes here
                    finish();
                }
            });
        }

        this.reloadNeed = false; // do not reload anymore, unless I tell you so...
    }

    @Override
    public void onBackPressed() {
        //引入全域變數
        GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        globalVariable.sType = mType;
        globalVariable.iType = iMType;

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent); // or RESULT_CANCELED if user did not make any changes
        // it would be better to use some custom defined codes here
        finish();
    }

    private void getLoction(float[] lat, float[] lng) {
        String result = "";
        for (int i = 0; i < region.length; i++) {
            result = DB_CORRD.executeQuery(post_id[i], region[i], rent[i]);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                JSONObject jsonData = jsonArray.getJSONObject(0);
                lat[i] = Float.parseFloat(jsonData.getString("lat"));
                lng[i] = Float.parseFloat(jsonData.getString("lng"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return;
    }

    private void getFav() {
        try {
            String result = DB_FETCH_COLLECTION.executeQuery();
            //如果沒資料
            if (result.equals("-1")) {
                isFav = false;
                return;
            } else {
                isFav = true;
                //列出所有
                if (iMType == 0) {
                    //解析資料
                    JSONArray jsonArray = new JSONArray(result);
                    addr = new String[jsonArray.length()];
                    post_id = new String[jsonArray.length()];
                    avg_score = new String[jsonArray.length()];
                    rent = new String[jsonArray.length()];
                    region = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        addr[i] = jsonData.getString("addr");
                        avg_score[i] = jsonData.getString("avg_score");
                        post_id[i] = jsonData.getString("post_id");
                        rent[i] = jsonData.getString("rent");
                        region[i] = jsonData.getString("region");

                    }
                }
                //只列出租
                else if (iMType == 1) {
                    JSONArray jsonArray = new JSONArray(result);
                    int cnt = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        if (jsonData.getString("rent").equals("lease")) {
                            cnt++;
                        }
                    }
                    //check if data exist
                    if (cnt == 0) {
                        isFav = false;
                        return;
                    } else {
                        addr = new String[cnt];
                        post_id = new String[cnt];
                        avg_score = new String[cnt];
                        rent = new String[cnt];
                        region = new String[cnt];

                        cnt = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonData = jsonArray.getJSONObject(i);
                            if (jsonData.getString("rent").equals("lease")) {
                                addr[cnt] = jsonData.getString("addr");
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
                        if (jsonData.getString("rent").equals("lease"))
                            continue;
                        cnt++;
                    }
                    //check if data exist
                    if (cnt == 0) {
                        isFav = false;
                        return;
                    } else {
                        addr = new String[cnt];
                        post_id = new String[cnt];
                        avg_score = new String[cnt];
                        rent = new String[cnt];
                        region = new String[cnt];

                        cnt = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonData = jsonArray.getJSONObject(i);
                            if (jsonData.getString("rent").equals("lease"))
                                continue;
                            addr[cnt] = jsonData.getString("addr");
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
}
