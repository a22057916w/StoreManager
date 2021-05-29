package test.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class square extends AppCompatActivity implements View.OnClickListener {
    private Button sqtn1,sqtn2,sqtn3,sqtn4,sqtn5,sqtn6;
    private Intent mIntent;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        //元件宣告
        findViews();
        //監聽按鍵
        setOnClickListener();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {square.super.finish();
            }
        });
    }

    private void findViews(){
        sqtn1 = findViewById(R.id.sqtn1);
        sqtn2 = findViewById(R.id.sqtn2);
        sqtn3 = findViewById(R.id.sqtn3);
        sqtn4 = findViewById(R.id.sqtn4);
        sqtn5 = findViewById(R.id.sqtn5);
        sqtn6 = findViewById(R.id.sqtn6);
        imgBack = findViewById(R.id.back);
    }
    private void setOnClickListener() {
        sqtn1.setOnClickListener(this);
        sqtn2.setOnClickListener(this);
        sqtn3.setOnClickListener(this);
        sqtn4.setOnClickListener(this);
        sqtn5.setOnClickListener(this);
        sqtn6.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
        switch (v.getId()) {
            case R.id.sqtn1:
                globalVariable.main_area = String.valueOf(sqtn1.getText());
                globalVariable.key_area = "0";
                mIntent = new Intent(square.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.sqtn2:
                globalVariable.main_area = String.valueOf(sqtn2.getText());
                globalVariable.key_area = "10";
                mIntent = new Intent(square.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.sqtn3:
                globalVariable.main_area = String.valueOf(sqtn3.getText());
                globalVariable.key_area = "20";
                mIntent = new Intent(square.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.sqtn4:
                globalVariable.main_area = String.valueOf(sqtn4.getText());
                globalVariable.key_area = "30";
                mIntent = new Intent(square.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.sqtn5:
                globalVariable.main_area = String.valueOf(sqtn5.getText());
                globalVariable.key_area = "40";
                mIntent = new Intent(square.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.sqtn6:
                globalVariable.main_area = "坪數";
                globalVariable.key_area = "-1";
                mIntent = new Intent(square.this, MainActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}