package test.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class rent extends AppCompatActivity implements View.OnClickListener {
    private Button rbtn1,rbtn2;
    private Intent mIntent;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        //元件宣告
        findViews();
        //監聽按鍵
        setOnClickListener();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rent.super.finish();
            }
        });
    }

    private void findViews(){
        rbtn1 = findViewById(R.id.rbtn1);
        rbtn2 = findViewById(R.id.rbtn2);
        imgBack = findViewById(R.id.back);
    }
    private void setOnClickListener() {
        rbtn1.setOnClickListener(this);
        rbtn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
        //初始化價錢和坪數
        globalVariable.main_price = "價錢";
        globalVariable.key_price = "-1";
        globalVariable.main_area = "坪數";
        globalVariable.key_area = "-1";

        switch (v.getId()) {
            case R.id.rbtn1:
                globalVariable.main_rent = String.valueOf(rbtn1.getText());
                globalVariable.key_rent = "lease";
                mIntent = new Intent(rent.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.rbtn2:
                globalVariable.main_rent = String.valueOf(rbtn2.getText());
                globalVariable.key_rent = "sells";
                mIntent = new Intent(rent.this, MainActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}