package com.example.withganada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class CheckQuestion extends AppCompatActivity {

    Button btn_tomain;
    RatingBar ratingBar;
    int finalscore;
    TextView stage_end;
    SoundPool soundPool = null;     //사운드 참조변수 선언
    int soundId = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_question);

        setSoundPool();
        context = this;
        soundId = R.raw.clear;
        playSound();

        ratingBar = findViewById(R.id.ratingBar);
        finalscore = ((AppValue)getApplication()).getLastscore();
        ratingBar.setRating(finalscore);

        stage_end = findViewById(R.id.stage_end);
        if(finalscore <=1) stage_end.setText("다시 한 번 해볼까요?");
        else if (finalscore >1 && finalscore <=3) stage_end.setText("조금 더 노력해봅시다!");
        else if (finalscore == 4) stage_end.setText("정말 잘했어요!");
        else if (finalscore == 5) stage_end.setText("완벽해요!");
        else stage_end.setText("점수 오류입니다.");

        btn_tomain = findViewById(R.id.finish);
        btn_tomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);	//메인액티비티로 이동
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();	//스테이지 완료 액티비티 종료
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
                // maxStream, streamType, quality
                soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            }
        }
    }
    public void playSound() {
        // context, resId, priority
        final int sound = soundPool.load(context, soundId, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                // soundId, leftVolumn, rightVolumn, priority, loop, rate
                soundPool.play(sound, 1f, 1f, 0, 0, 1.0f);
            }
        });
    }
    protected void onDestroy(){
        super.onDestroy();
        if (soundPool != null){
            soundPool.release();
            soundPool = null;
        }
    }
}