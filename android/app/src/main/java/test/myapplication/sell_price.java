package test.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class sell_price extends AppCompatActivity implements View.OnClickListener {
    private Button pbtn1,pbtn2,pbtn3,pbtn4,pbtn5,pbtn6;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_price);
        //元件宣告
        findViews();
        //監聽按鍵
        setOnClickListener();
    }

    private void findViews(){
        pbtn1 = findViewById(R.id.pbtn1);
        pbtn2 = findViewById(R.id.pbtn2);
        pbtn3 = findViewById(R.id.pbtn3);
        pbtn4 = findViewById(R.id.pbtn4);
        pbtn5 = findViewById(R.id.pbtn5);
        pbtn6 = findViewById(R.id.pbtn6);
    }
    private void setOnClickListener() {
        pbtn1.setOnClickListener(this);
        pbtn2.setOnClickListener(this);
        pbtn3.setOnClickListener(this);
        pbtn4.setOnClickListener(this);
        pbtn5.setOnClickListener(this);
        pbtn6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
        switch (v.getId()) {
            case R.id.pbtn1:
                globalVariable.main_price = String.valueOf(pbtn1.getText());
                globalVariable.key_price = "0";
                mIntent = new Intent(sell_price.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.pbtn2:
                globalVariable.main_price = String.valueOf(pbtn2.getText());
                globalVariable.key_price = "500";
                mIntent = new Intent(sell_price.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.pbtn3:
                globalVariable.main_price = String.valueOf(pbtn3.getText());
                globalVariable.key_price = "1000";
                mIntent = new Intent(sell_price.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.pbtn4:
                globalVariable.main_price = String.valueOf(pbtn4.getText());
                globalVariable.key_price = "2000";
                mIntent = new Intent(sell_price.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.pbtn5:
                globalVariable.main_price = String.valueOf(pbtn5.getText());
                globalVariable.key_price = "5000";

                mIntent = new Intent(sell_price.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.pbtn6:
                globalVariable.main_price = "價錢";
                globalVariable.key_price = "-1";

                mIntent = new Intent(sell_price.this, MainActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}