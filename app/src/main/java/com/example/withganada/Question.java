package com.example.withganada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Question extends AppCompatActivity {

    Button CheckAns;
    TextView StageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Intent intent = getIntent(); /*데이터 수신*/
        int Stagenum = intent.getExtras().getInt("stageIndex");
        //어떤 단원의 문제인지 데이터 넘겨받기
        StageIndex = findViewById(R.id.textView2);
        StageIndex.setText(Integer.toString(Stagenum) + "단원의 문제를 출력하는 부분");

        CheckAns = findViewById(R.id.btn_check);
        CheckAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //////////////////////////////////////////////
                //추후 이 부분에 마지막 문제인지 체크하는 부분 넣을 것
                /////////////////////////////////////////////
                Intent intent = new Intent(getApplicationContext(), CheckQuestion.class);
                startActivity(intent);	//메인액티비티로 이동
                finish();	//문제 액티비티 종료
            }
        });
    }
}