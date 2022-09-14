package com.example.withganada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    SoundPool soundPool = null;     //사운드 참조변수 선언
    int soundId = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setSoundPool();
        context = this;
        soundId = R.raw.intro;
        playSound();
        MovetoMain(2);
    }

    private void MovetoMain(int sec) {
        new Handler().postDelayed(new Runnable()        //thread.sleep시 레이아웃 로딩도 멈춘다.
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);	//메인 액티비티로 이동
                overridePendingTransition(R.anim.fade_in, R.anim.hold);
                finish();	//스플래시 액티비티 종료
            }
        }, 1000 * sec); // sec초 정도 딜레이를 준 후 시작
    }       //스플래시 애니메이션 종료 후 자동으로 메인 페이지 액팅비티로 이동한다.

    private void setSoundPool() {       //효과음 로딩 함수
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
                soundPool.play(sound, 1f, 1f, 0, 0, 1.0f);
            }
        });
    }

    protected void onDestroy(){     //액티비티 종료시 SoundPool 객체의 release
        super.onDestroy();

        if (soundPool != null){
            soundPool.release();
            soundPool = null;
        }


    }
}