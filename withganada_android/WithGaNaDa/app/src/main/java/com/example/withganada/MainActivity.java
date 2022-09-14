package com.example.withganada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton Stage1,Stage2,Stage3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((AppValue)getApplication()).setTry(0);     //전역변수 설정
        ((AppValue)getApplication()).setLastscore(0);       //전역변수 설정

        Stage1 = findViewById(R.id.button);
        Stage2 = findViewById(R.id.button2);
        Stage3 = findViewById(R.id.button3);     //각각 버튼별 스테이지 할당

        Stage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Question.class);
                //데이터 송신부
                intent.putExtra("Currentstate",1);
                intent.putExtra("stageIndex",0);
                startActivity(intent);	//문제 액티비티로 이동
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();	//액티비티 종료
            }
        });
        Stage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), select4Q.class);
                //데이터 송신부
                intent.putExtra("Currentstate",1);
                intent.putExtra("stageIndex",1);
                startActivity(intent);	//문제 액티비티로 이동
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();	//액티비티 종료
            }
        });
        Stage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Question.class);
                //데이터 송신부
                intent.putExtra("Currentstate",1);
                intent.putExtra("stageIndex",2);
                startActivity(intent);	//문제 액티비티로 이동
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();	//액티비티 종료
            }
        });

    }
}