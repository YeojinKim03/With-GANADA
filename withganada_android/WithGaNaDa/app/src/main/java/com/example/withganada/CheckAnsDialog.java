package com.example.withganada;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.Random;

public class CheckAnsDialog extends Dialog {        //정답 제출 시 결과를 출력하는 Dialog
    private TextView txt_contents;
    private Button shutdownClick;

    int Currentstate = 5;
    int Stagenum;

    ImageView resultimg;

    SoundPool soundPool = null;     //사운드 참조변수 선언
    int soundId = 0;
    private Context context1;

    @SuppressLint("ResourceAsColor")
    public CheckAnsDialog(@NonNull Context context, String contents, int ans, int select, int Current, int stage, int score) {
        super(context);
        setContentView(R.layout.activity_custom_dialog);
        Stagenum = stage;
        Currentstate = Current;
        txt_contents = findViewById(R.id.txt_contents);
        txt_contents.setText(contents);
        shutdownClick = findViewById(R.id.btn_shutdown);
        resultimg = findViewById(R.id.resultimgview);

        setSoundPool();
        context1 = getContext();

        if (ans == select){     //정답인경우
            soundId = R.raw.correct;
            playSound();
            shutdownClick.setText("다음으로");
            txt_contents.setText("정답입니다");
            if (score >=0) {        //만약 정확도를 전달받으면
                txt_contents.append("\n정확도 점수는 "+ score + "입니다.");
            }
        }
        else{
            if (score <50 && score>=0) {        //만약 정확도를 전달받으면
                txt_contents.setText("조용한 곳에서\n크고 정확하게 말해주세요.");
            }
            soundId = R.raw.no;
            playSound();
            if (((AppValue)(getContext().getApplicationContext())).getTry() == 1){
                shutdownClick.setText("다음으로");
            }
            resultimg.setImageResource(R.drawable.incorrect);
        }

        shutdownClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ans == select){     //정답인경우
                    int tmptry = ((AppValue)(getContext().getApplicationContext())).getTry();
                    int tmpLastscore = ((AppValue)(getContext().getApplicationContext())).getLastscore();
                    tmpLastscore = tmpLastscore - tmptry +1;
                    ((AppValue)(getContext().getApplicationContext())).setLastscore(tmpLastscore);
                    ((AppValue)(getContext().getApplicationContext())).setTry(0);
                    if (Currentstate >=5){
                        Intent intent = new Intent(getContext().getApplicationContext(), CheckQuestion.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);       //쌓인 액티비티 종료
                        getContext().startActivity(intent);
                        dismiss();	//문제 액티비티 종료
                    }
                    else {
                        //다음 문제로 진행해야하는 경우
                        Random r = new Random();
                        int next = r.nextInt(2);
                        if (next == 0){
                            Currentstate++;
                            Intent intent = new Intent(getContext().getApplicationContext(), select4Q.class);
                            intent.putExtra("Currentstate",Currentstate);
                            intent.putExtra("stageIndex",Stagenum);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);       //쌓인 액티비티 종료
                            getContext().startActivity(intent);
                            dismiss();	//문제 액티비티 종료
                        }
                        else{
                            //next 값이 1인경우
                            Currentstate++;
                            Intent intent = new Intent(getContext().getApplicationContext(), Question.class);
                            intent.putExtra("Currentstate",Currentstate);
                            intent.putExtra("stageIndex",Stagenum);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);       //쌓인 액티비티 종료
                            getContext().startActivity(intent);
                            dismiss();	//문제 액티비티 종료

                        }
                    }

                }
                else{
                    if(((AppValue)(getContext().getApplicationContext())).getTry() == 0) {
                        ((AppValue) (getContext().getApplicationContext())).setTry(1);
                        dismiss();      //오답 횟수를 증가하고 다시 풀도록 유도
                    }
                    else {              //재오답인 경우
                        ((AppValue)(getContext().getApplicationContext())).setTry(0);
                        if (Currentstate >=5){
                            Intent intent = new Intent(getContext().getApplicationContext(), CheckQuestion.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);       //쌓인 액티비티 종료
                            getContext().startActivity(intent);
                            dismiss();	//문제 액티비티 종료
                        }
                        else {
                            //다음 문제로 진행해야하는 경우
                            Random r = new Random();
                            int next = r.nextInt(2);
                            if (next == 0) {
                                Currentstate++;
                                Intent intent = new Intent(getContext().getApplicationContext(), select4Q.class);
                                intent.putExtra("Currentstate", Currentstate);
                                intent.putExtra("stageIndex", Stagenum);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);       //쌓인 액티비티 종료
                                getContext().startActivity(intent);
                                dismiss();    //문제 액티비티 종료
                            } else {
                                //next 값이 1인경우
                                Currentstate++;
                                Intent intent = new Intent(getContext().getApplicationContext(), Question.class);
                                intent.putExtra("Currentstate", Currentstate);
                                intent.putExtra("stageIndex", Stagenum);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);       //쌓인 액티비티 종료
                                getContext().startActivity(intent);
                                dismiss();    //문제 액티비티 종료

                            }
                        }
                    }
                }
            }
        });
    }

    private void setSoundPool() {
        if (soundPool == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();
                soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();
            } else {
                soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            }
        }
    }
    public void playSound() {
        final int sound = soundPool.load(context1, soundId, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sound, 1f, 1f, 0, 0, 1.0f);
            }
        });
    }
    public void dismiss(){
        super.dismiss();
        if (soundPool != null){
            soundPool.release();
            soundPool = null;
        }
    }

}
