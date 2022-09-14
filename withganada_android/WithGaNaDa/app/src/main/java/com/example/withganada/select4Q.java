package com.example.withganada;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.kofigyan.stateprogressbar.StateProgressBar;
import java.util.Random;

public class select4Q extends AppCompatActivity {

    private CheckAnsDialog customDialog;

    String[] wordlist = {"아이","화가","뿌리","가수","오리","파도","부모","까치","의자"};
    //단어 라벨 0-아이 1-화가 2-뿌리 3-가수 4-오리 5-파도 6-부모 7-까치 8-의자

    int answer;     //문제의 답안 저장
    int currenchk = 99;     //현재 선택한 선택지 표시
    Button A1,A2,A3,A4,btn_check;
    ImageButton imgA1,imgA2,imgA3,imgA4;
    ImageView ans_Img;
    TextView text, ans_word;
    String tmp ="";

    int count = 4; // 난수 생성 갯수
    int a[] = new int[count];

    int Currentstate = 1;
    int Stagenum;
    StateProgressBar stateProgressBar;

    int QuestionType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select4_q);

        Intent intent = getIntent(); /*데이터 수신*/
        Stagenum = intent.getExtras().getInt("stageIndex");
        Currentstate = intent.getExtras().getInt("Currentstate");
        //어떤 단원의 문제인지 데이터 넘겨받기

        text = findViewById(R.id.select4_text);
        A1 = findViewById(R.id.Q4_ans1);
        A2 = findViewById(R.id.Q4_ans2);
        A3 = findViewById(R.id.Q4_ans3);
        A4 = findViewById(R.id.Q4_ans4);
        ans_Img = findViewById(R.id.ans_img);

        imgA1 = findViewById(R.id.Img_Q4_ans1);
        imgA2 = findViewById(R.id.Img_Q4_ans2);
        imgA3 = findViewById(R.id.Img_Q4_ans3);
        imgA4 = findViewById(R.id.Img_Q4_ans4);
        ans_word = findViewById(R.id.ans_word);
        //문제 전처리 부분

        stateProgressBar = findViewById(R.id.progress_bar);
        SetProgressView();
        //현재 단계를 받아와서 프로그레스 바에 출력

        Random rnd = new Random();
        QuestionType = rnd.nextInt(2);      //문제유형

        //정답을 정하고, 보기 세개 정함
        makeQuestion();
        makeImg();

        //버튼별 정답 체크하고, 다음 액티비티 랜덤으로 넘겨주는 부분(만약 마지막 문제라면 점수 페이지로)

        if (QuestionType == 0){     //그림을 맞추는 유형
            ans_word.setText(wordlist[answer]);
            text.setText("단어에 맞는 그림을 고르세요.");
        }
        else {
            A1.setVisibility(View.VISIBLE);
            A2.setVisibility(View.VISIBLE);
            A3.setVisibility(View.VISIBLE);
            A4.setVisibility(View.VISIBLE);
            ans_Img.setVisibility(View.VISIBLE);

            imgA1.setVisibility(View.INVISIBLE);
            imgA2.setVisibility(View.INVISIBLE);
            imgA3.setVisibility(View.INVISIBLE);
            imgA4.setVisibility(View.INVISIBLE);
            ans_word.setVisibility(View.INVISIBLE);
        }

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        btn_check = findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {        //아무것도 선택하지 않고 정답을 제출하려는 경우
                if (currenchk == 99){
                    Toast.makeText(getApplicationContext(), "정답을 선택하세요.", Toast.LENGTH_SHORT).show();

                }
                else {      //오답을 선택한 경우
                    customDialog = new CheckAnsDialog(view.getContext(), "[ "+ wordlist[a[currenchk]] + " ]가 아닌 다른 것을 생각해봅시다.", answer, a[currenchk], Currentstate, Stagenum, -99);
                    customDialog.show();
                }
            }
        });


        {   /*단어를 고르는 문제 유형의 경우 선택지 UI*/
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
        {   /*그림을 고르는 문제 유형의 경우 선택지 UI*/
            imgA1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imgA1.getBackgroundTintList() == null || imgA1.getBackgroundTintList() == ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal))) {
                        imgA1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn)));
                        imgA2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        imgA3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        imgA4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 0;
                    } else {
                        imgA1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 99;
                    }
                }
            });
            imgA2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imgA2.getBackgroundTintList() == null || imgA2.getBackgroundTintList() == ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal))) {
                        imgA2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn)));
                        imgA1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        imgA3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        imgA4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 1;
                    } else {
                        imgA2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 99;
                    }
                }
            });
            imgA3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imgA3.getBackgroundTintList() == null || imgA3.getBackgroundTintList() == ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal))) {
                        imgA3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn)));
                        imgA2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        imgA1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        imgA4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 2;
                    } else {
                        imgA3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 99;
                    }
                }
            });
            imgA4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imgA4.getBackgroundTintList() == null || imgA4.getBackgroundTintList() == ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal))) {
                        imgA4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn)));
                        imgA2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        imgA3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        imgA1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
                        currenchk = 3;
                    } else {
                        imgA4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
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
            if (i == 0) { a[i] = r.nextInt(3)  + Stagenum*3; }
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

        makeImg_select(imgA1, a[0]);
        makeImg_select(imgA2, a[1]);
        makeImg_select(imgA3, a[2]);
        makeImg_select(imgA4, a[3]);


    }   //4지선다 문제를 생성하고, 정답 외의 선택지를 비복원추출하는 함수
    public void makeImg(){
        switch (answer){
            case 0: ans_Img.setImageResource(R.drawable.kid); break;
            case 1: ans_Img.setImageResource(R.drawable.painter); break;
            case 2: ans_Img.setImageResource(R.drawable.root); break;
            case 3: ans_Img.setImageResource(R.drawable.singer); break;
            case 4: ans_Img.setImageResource(R.drawable.duck); break;
            case 5: ans_Img.setImageResource(R.drawable.wave); break;
            case 6: ans_Img.setImageResource(R.drawable.parents); break;
            case 7: ans_Img.setImageResource(R.drawable.magpie); break;
            case 8: ans_Img.setImageResource(R.drawable.chair); break;

        }

    }
    public void makeImg_select(ImageButton imgB, int here){
        switch (here){
            case 0: imgB.setImageResource(R.drawable.kid); break;
            case 1: imgB.setImageResource(R.drawable.painter); break;
            case 2: imgB.setImageResource(R.drawable.root); break;
            case 3: imgB.setImageResource(R.drawable.singer); break;
            case 4: imgB.setImageResource(R.drawable.duck); break;
            case 5: imgB.setImageResource(R.drawable.wave); break;
            case 6: imgB.setImageResource(R.drawable.parents); break;
            case 7: imgB.setImageResource(R.drawable.magpie); break;
            case 8: imgB.setImageResource(R.drawable.chair); break;

        }

    }
    public void SetProgressView(){
        //현재 단계를 나타내는 부분 처리
        //StageIndex.setText(Integer.toString(Stagenum) + "단원의 문제를 출력하는 부분입니다.\n현재 문항 : " + Integer.toString(Currentstate) + "번");
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
