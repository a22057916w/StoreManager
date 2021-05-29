package test.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class type extends AppCompatActivity implements View.OnClickListener {
    private Button tbtn1,tbtn2,tbtn3;
    private Intent mIntent;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        //元件宣告
        findViews();
        //監聽按鍵
        setOnClickListener();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type.super.finish();
            }
        });
    }

    private void findViews(){
        tbtn1 = findViewById(R.id.tbtn1);
        tbtn2 = findViewById(R.id.tbtn2);
        tbtn3 = findViewById(R.id.tbtn3);
        imgBack = findViewById(R.id.back);
    }
    private void setOnClickListener() {
        tbtn1.setOnClickListener(this);
        tbtn2.setOnClickListener(this);
        tbtn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
        switch (v.getId()) {
            case R.id.tbtn1:
                globalVariable.main_type = String.valueOf(tbtn1.getText());
                globalVariable.key_type = "1";
                mIntent = new Intent(type.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tbtn2:
                globalVariable.main_type = String.valueOf(tbtn2.getText());
                globalVariable.key_type = "2";
                mIntent = new Intent(type.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tbtn3:
                globalVariable.main_type = "店家類型";
                globalVariable.key_type = "-1";
                mIntent = new Intent(type.this, MainActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}