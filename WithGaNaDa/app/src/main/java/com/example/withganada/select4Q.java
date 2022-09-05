package com.example.withganada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class select4Q extends AppCompatActivity {

    private CheckAnsDialog customDialog;

    //단어의 배열을 기본설정
    String[] wordlist = {"아이","부모","뿌리","가수","오리","파도","나무","까치","의자"};
    //단어 라벨 0-아이 1-부모 2-뿌리 3-가수 4-오리 5-파도 6-나무 7-까치 8-의자
    int answer;
    int currenchk = 99;
    Button A1,A2,A3,A4,btn_check;
    ImageView ans_Img;
    TextView text;
    String tmp ="";

    int stage = 0; //단원 숫자 체크

    int count = 4; // 난수 생성 갯수
    int a[] = new int[count];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select4_q);

        text = findViewById(R.id.select4_text);
        A1 = findViewById(R.id.Q4_ans1);
        A2 = findViewById(R.id.Q4_ans2);
        A3 = findViewById(R.id.Q4_ans3);
        A4 = findViewById(R.id.Q4_ans4);
        ans_Img = findViewById(R.id.ans_img);
        //문제 전처리 부분
        //현재 단계를 받아와서 프로그레스 바에 출력

        //정답을 정하고, 보기 세개 정함
        makeQuestion();
        makeImg();

        //버튼별 정답 체크하고, 다음 액티비티 랜덤으로 넘겨주는 부분(만약 마지막 문제라면 점수 페이지로)

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        btn_check = findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog = new CheckAnsDialog(view.getContext(),"정답인지 판별합니다. 전달 받은 값은 "+currenchk+"입니다.", answer,a[currenchk]);
                customDialog.show();
            }
        });


        {
            A1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (A1.getBackgroundTintList() == null || A1.getBackgroundTintList() == ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal))) {
                        A1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn)));
                        A2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        A3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        A4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 0;
                    } else {
                        A1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 99;
                    }
                }
            });
            A2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (A2.getBackgroundTintList() == null || A2.getBackgroundTintList() == ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal))) {
                        A2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn)));
                        A1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        A3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        A4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 1;
                    } else {
                        A2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 99;
                    }
                }
            });
            A3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (A3.getBackgroundTintList() == null || A3.getBackgroundTintList() == ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal))) {
                        A3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn)));
                        A2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        A1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        A4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 2;
                    } else {
                        A3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 99;
                    }
                }
            });
            A4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (A4.getBackgroundTintList() == null || A4.getBackgroundTintList() == ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal))) {
                        A4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn)));
                        A2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        A3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        A1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 3;
                    } else {
                        A4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 99;
                    }
                }
            });
        }

}


    public void makeQuestion(){
        //ArrayList<Integer> tmparray = new ArrayList<Integer>();
        Random r = new Random();
        for(int i=0; i<count; i++){
            if (i == 0) { a[i] = r.nextInt(3)  + stage*3; }
            else { a[i] = r.nextInt(9) ;} // 0 ~ 8까지의 난수 }
            for(int j=0; j<i; j++){
                if(a[i] == a[j]){
                    i--;
                }
            }

        }

        answer = a[0];      //정답 적어두기
        int k = r.nextInt(4);
        int temp = a[k];
        a[k] = a[0];
        a[0] = temp;

        for(int i=0; i<count; i++){

            tmp += a[i];
            //난수를 차례로 배열에 넣기
        }
        //text.setText("난수 : " + tmp + "정답 : " + answer);

        //선택지 버튼에 입력
        A1.setText(wordlist[a[0]]);
        A2.setText(wordlist[a[1]]);
        A3.setText(wordlist[a[2]]);
        A4.setText(wordlist[a[3]]);
    }
    public void makeImg(){
        switch (answer){
            case 0: ans_Img.setImageResource(R.drawable.kid); break;
            case 1: ans_Img.setImageResource(R.drawable.parents); break;
            case 2: ans_Img.setImageResource(R.drawable.root); break;
            case 3: ans_Img.setImageResource(R.drawable.singer); break;
            case 4: ans_Img.setImageResource(R.drawable.duck); break;
            case 5: ans_Img.setImageResource(R.drawable.wave); break;
            case 6: ans_Img.setImageResource(R.drawable.tree); break;
            case 7: ans_Img.setImageResource(R.drawable.magpie); break;
            case 8: ans_Img.setImageResource(R.drawable.chair); break;

        }

    }

    }
