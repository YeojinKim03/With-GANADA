package com.example.withganada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kofigyan.stateprogressbar.StateProgressBar;

public class Question extends AppCompatActivity {

    Button CheckAns;
    TextView StageIndex;
    int Currentstate = 1;
    int Stagenum;
    StateProgressBar stateProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Intent intent = getIntent(); /*데이터 수신*/
        Stagenum = intent.getExtras().getInt("stageIndex");
        //어떤 단원의 문제인지 데이터 넘겨받기
        StageIndex = findViewById(R.id.textView2);
        stateProgressBar = findViewById(R.id.progress_bar);
        SetProgressView();


        CheckAns = findViewById(R.id.btn_check);
        CheckAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //////////////////////////////////////////////
                //추후 이 부분에 마지막 문제인지 체크하는 부분 넣을 것
                if (Currentstate >=5){
                    Intent intent = new Intent(getApplicationContext(), CheckQuestion.class);
                    startActivity(intent);	//메인액티비티로 이동
                    finish();	//문제 액티비티 종료
                }
                else {
                    Currentstate++;
                    SetProgressView();
                }
                /////////////////////////////////////////////

            }
        });


    }

    public void SetProgressView(){
        //현재 단계를 나타내는 부분 처리
        StageIndex.setText(Integer.toString(Stagenum) + "단원의 문제를 출력하는 부분입니다.\n현재 문항 : " + Integer.toString(Currentstate) + "번");
        switch (Currentstate){

            case 1:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                break;
            case 2:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                break;
            case 3:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                break;
            case 4:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                break;
            case 5:
                stateProgressBar.setAllStatesCompleted(true);



        }

    }
}