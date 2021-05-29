package test.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class search extends AppCompatActivity implements View.OnClickListener {
    private Button sebtn1,sebtn2,sebtn3,sebtn4,sebtn5,sebtn6,sebtn7,sebtn8,sebtn9,sebtn10,sebtn11,sebtn12,sebtn13;
    private Intent mIntent;
    String[] price, area, addr, post_id, avg_score;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //元件宣告
        findViews();
        //監聽按鍵
        setBundle();
        result.setText(Integer.toString(area.length));
        setOnClickListener();

    }

    private void findViews(){
        sebtn1 = findViewById(R.id.sebtn1);
        sebtn2 = findViewById(R.id.sebtn2);
        sebtn3 = findViewById(R.id.sebtn3);
        sebtn4 = findViewById(R.id.sebtn4);
        sebtn5 = findViewById(R.id.sebtn5);
        sebtn6 = findViewById(R.id.sebtn6);
        sebtn7 = findViewById(R.id.sebtn7);
        sebtn8 = findViewById(R.id.sebtn8);
        sebtn9 = findViewById(R.id.sebtn9);
        sebtn10 = findViewById(R.id.sebtn10);
        sebtn11 = findViewById(R.id.sebtn11);
        sebtn12 = findViewById(R.id.sebtn12);
        sebtn13 = findViewById(R.id.sebtn13);
        result = findViewById(R.id.result);
    }
    private void setOnClickListener() {
        sebtn1.setOnClickListener(this);
        sebtn2.setOnClickListener(this);
        sebtn3.setOnClickListener(this);
        sebtn4.setOnClickListener(this);
        sebtn5.setOnClickListener(this);
        sebtn6.setOnClickListener(this);
        sebtn7.setOnClickListener(this);
        sebtn8.setOnClickListener(this);
        sebtn9.setOnClickListener(this);
        sebtn10.setOnClickListener(this);
        sebtn11.setOnClickListener(this);
        sebtn12.setOnClickListener(this);
        sebtn13.setOnClickListener(this);
    }

    private void setBundle() {
        Bundle bundle = getIntent().getExtras();
        price = bundle.getStringArray("price");
        area = bundle.getStringArray("area");
        addr = bundle.getStringArray("addr");
        post_id = bundle.getStringArray("post_id");
        avg_score = bundle.getStringArray("avg_score");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sebtn1:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn2:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn3:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn4:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn5:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn6:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn7:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn8:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn9:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn10:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn11:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn12:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
            case R.id.sebtn13:
                mIntent = new Intent(search.this, MAP.class);
                startActivity(mIntent);
                break;
        }
    }
}