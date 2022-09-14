package com.example.withganada;

import android.app.Application;

public class AppValue extends Application {     //문제풀이 시도 횟수와 스코어를 전역변수로 선언하여 이용한다.
    private int trynum;
    private int Lastscore;

    public int getTry(){
        return trynum;
    }
    public void setTry( int trynum){
        this.trynum = trynum;
    }
    public int getLastscore(){
        return Lastscore;
    }
    public void setLastscore( int Lastscore){
        this.Lastscore = Lastscore;
    }

}
