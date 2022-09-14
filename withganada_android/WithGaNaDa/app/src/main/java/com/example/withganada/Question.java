package com.example.withganada;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Question extends AppCompatActivity {
    private CheckAnsDialog customDialog;

    String[] wordlist = {"아이","화가","뿌리","가수","오리","파도","부모","까치","의자"};
    //단어 라벨 0-아이 1-화가 2-뿌리 3-가수 4-오리 5-파도 6-부모 7-까치 8-의자

    int answer;

    Button CheckAns;
    TextView StageIndex;
    int Currentstate = 1;
    int Stagenum;
    StateProgressBar stateProgressBar;

    //통신 헤더 선언부
    private Socket client;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;
    private static String SERVER_IP = "116.41.108.67";
    private static String CONNECT_MSG = "connect";
    private static String STOP_MSG = "stop";
    private static String NOFILE_MSG = "nofile";

    private static int BUF_SIZE = 100;

    MediaRecorder recorder;
    String fileName;
    MediaPlayer mediaPlayer;

    boolean isrecoding = false;
    boolean hadrecorded = false;

    ImageView imgspeak;
    TextView wordspeak;

    ImageButton rec;

    ImageButton speaker;
    SoundPool soundPool = null;     //사운드 참조변수 선언
    boolean isspeaker = false;
    int soundId = 0;

    int aiFind = 99;
    int finalai = 99;
    int accuracy = -99;
    boolean isUpload = false;

    ValueHandler handler = new ValueHandler();
    Connect connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Intent intent = getIntent(); /*데이터 수신*/
        Stagenum = intent.getExtras().getInt("stageIndex");
        Currentstate = intent.getExtras().getInt("Currentstate");
        //어떤 단원의 문제인지 데이터 넘겨받기


        permissionCheck();      //마이크 권한 체크

        StageIndex = findViewById(R.id.textView2);
        StageIndex.setText("");
        stateProgressBar = findViewById(R.id.progress_bar);
        SetProgressView();

        imgspeak = findViewById(R.id.imagespeak);
        wordspeak = findViewById(R.id.wordspeak);
        makeQuestion();


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            soundPool = new SoundPool.Builder().build();
        }else{
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        }

        speaker = findViewById(R.id.btn_speaker);
        Random rnd = new Random();
        if (rnd.nextInt(2) == 0) {
            isspeaker = true;         //예시 음성이 있는지 없는지 값 결정
            TextView Question = findViewById(R.id.select4_text);
            Question.setText("듣고 따라해보세요.");
            speaker.setVisibility(View.VISIBLE);
            makesoundId();
        }

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                soundPool.play(soundId,1.0f,1.0f,1,0,1.0f);
            }
        });

        CheckAns = findViewById(R.id.btn_check);
        CheckAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hadrecorded) {
                    Log.w("aifind","aiFind의 값은"+ aiFind+"입니다.");
                    if(aiFind <10) {
                        finalai = aiFind+(Stagenum*3);
                        customDialog = new CheckAnsDialog(view.getContext(), "혹시 "+ wordlist[finalai] + "라고 말했나요?", answer, finalai, Currentstate, Stagenum, accuracy);
                        customDialog.show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "먼저 단어를 읽어보세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // SD 카드 폴더 지정
        File file = new File(getApplicationInfo().dataDir, "recorded.mp3");
        fileName = file.getAbsolutePath();  // 파일 위치 가져옴

        rec = findViewById(R.id.test_rec);

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isrecoding == false) {
                    CheckAns.setText("잠시만 기다리세요...");
                    CheckAns.setClickable(false);
                    rec.setImageResource(R.drawable.mic_stop);
                    // 녹음 시작
                    if (recorder == null) {
                        recorder = new MediaRecorder(); // 미디어리코더 객체 생성
                    }
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 오디오 입력 지정(마이크)
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);    // 출력 형식 지정
                    //마이크로 들어오는 음성데이터는 용량이 크기 때문에 압축이 필요
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);   // 인코딩
                    recorder.setOutputFile(fileName);  // 음성 데이터를 저장할 파일 지정
                    try {
                        recorder.prepare();
                        recorder.start();
                        isrecoding = true;
                        hadrecorded = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    rec.setImageResource(R.drawable.mic);
                    if (recorder != null) {
                        recorder.stop();
                        recorder.release();
                        recorder = null;
                    }
                    isrecoding = false;
                    BackgroundThread thread = new BackgroundThread();
                    thread.setDaemon(true);
                    thread.start();
                }
            }
        });

        findViewById(R.id.test_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // 재생
                try {
                    if(mediaPlayer != null){    // 사용하기 전에
                        mediaPlayer.release();  // 리소스 해제
                        mediaPlayer = null;
                    }
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(fileName); // 음악 파일 위치 지정
                    mediaPlayer.prepare();  // 미리 준비
                    mediaPlayer.start();    // 재생
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }


    // 파일을 녹음하고 처리하는 스레드 클래스 생성
    class BackgroundThread extends Thread {

        int value = 0;
        boolean running = false;

        public void run() {

            isUpload = false;
            uploadFile();
            running = true;
            try {
                Thread.sleep(2000);
            } catch (Exception e) {}
            while(running) {
                Log.w("aifind","스레드 순환중입니다...");
                if (isUpload){  //만약 업로드 성공이라면

                    connect = new Connect();
                    connect.execute(CONNECT_MSG);       //서버에 업로드
                    running = false;
                }
                else {
                    //다시 해보세요
                    Log.w("aifind","다시 시도해보세요.");
                    Message message = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt("value", 1);
                    message.setData(bundle);
                    handler.sendMessage(message);

                    running = false;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {}
            }
            if (isUpload) {
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("value", 5);
                message.setData(bundle);
                handler.sendMessage(message);
                Log.i("aifind","aiFind의 값은"+ aiFind+"입니다.");
            }
        }
    }

    class ValueHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            int value = bundle.getInt("value");
            if (value == 1){
                Toast.makeText(getApplicationContext(),"업로드 불가, 잠시 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void permissionCheck(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);        }

    }
    public void SetProgressView(){
        //현재 단계를 나타내는 부분 처리
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

    /*서버에 신호를 보내 녹음파일 recored.mp3를 학습모델로 연산하도록 한다.*/
    private class Connect extends AsyncTask< String , String,Void > {
        private String output_message;
        private String input_message;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                client = new Socket(SERVER_IP, 12344);
                dataOutput = new DataOutputStream(client.getOutputStream());
                dataInput = new DataInputStream(client.getInputStream());
                output_message = Integer.toString(Stagenum);        //현재 단원 숫자를 발신
                dataOutput.writeUTF(output_message);

            } catch (UnknownHostException e) {
                String str = e.getMessage().toString();
                Log.w("discnt", str + " 1");
            } catch (IOException e) {
                String str = e.getMessage().toString();
                Log.w("discnt", str + " 2");
            }

            while (true){

                try {
                    byte[] buf = new byte[BUF_SIZE];
                    int read_Byte  = dataInput.read(buf);
                    input_message = new String(buf, 0, read_Byte);
                    if (!input_message.equals(STOP_MSG)){
                        publishProgress(input_message);
                    }
                    else if (input_message.equals(STOP_MSG)){
                        input_message = "null";
                        Log.w("closetrans","접속을 정상 종료합니다.");
                        client.close();
                        CheckAns.setText("정답 제출");
                        CheckAns.setClickable(true);
                        break;
                    }
                    else if (input_message.equals(NOFILE_MSG)){
                        input_message = "null";
                        Log.w("closetrans","파일이 없습니다.");
                        CheckAns.setText("다시 시도해주세요.");
                        client.close();
                        break;
                    }
                    else{
                        input_message = "null";
                        Log.w("closetrans","접속을 종료합니다.");
                        client.close();
                        break;
                    }
                    Thread.sleep(2);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    try {
                        client.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... params){
            String finalresponse = params[0];
            Log.i("cutstring","받아온 문자열은 : " + finalresponse);
            String[] strArr = finalresponse.split(",");
            int accIndex = answer - (Stagenum*3) + 1;
            Log.i("cutstring","accIndex : " + Integer.toString(accIndex));
            Log.i("cutstring","배열은 : " + Arrays.toString(strArr));
            accuracy = Integer.parseInt(strArr[accIndex]);
            aiFind = Integer.parseInt(strArr[0]);
            Log.i("cutstring","strArr[0] : " + strArr[0] + "정답의 정확도 : " + strArr[accIndex] );
            StageIndex.append("받은 메세지: " + strArr[0] + aiFind );
        }
    }

    private void uploadFile(){      //recorded.mp3 파일을 서버로 업로드하는 구문입니다.
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, "testna");
        File originalFile = new File(getApplicationInfo().dataDir, "recorded.mp3");
        RequestBody filePart = RequestBody.create(
                MediaType.parse("audio/*"),
                originalFile
        );

        MultipartBody.Part file = MultipartBody.Part.createFormData("file", originalFile.getName(), filePart);

        //create Retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://116.41.108.67:12345/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        //get client 오브젝트 콜

        UserClient client = retrofit.create(UserClient.class);

        //리퀘스트
        Call<ResponseBody> call = client.uploadMp3(descriptionPart, file);
        call.enqueue(new Callback<ResponseBody>() {
            @Override       //업로드에 성공했을 때
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(),"잠시만 기다려주세요...",Toast.LENGTH_SHORT).show();
                isUpload = true;
            }

            @Override       //업로드에 실패했을 때
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),"업로드 불가, 잠시 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void makeQuestion(){
        Random r = new Random();
        answer = r.nextInt(3)  + Stagenum*3;
        //화면에 UI처리
        makeImg();
        wordspeak.setText(wordlist[answer]);
    }
    public void makeImg(){
        switch (answer){
            case 0: imgspeak.setImageResource(R.drawable.kid); break;
            case 1: imgspeak.setImageResource(R.drawable.painter); break;
            case 2: imgspeak.setImageResource(R.drawable.root); break;
            case 3: imgspeak.setImageResource(R.drawable.singer); break;
            case 4: imgspeak.setImageResource(R.drawable.duck); break;
            case 5: imgspeak.setImageResource(R.drawable.wave); break;
            case 6: imgspeak.setImageResource(R.drawable.parents); break;
            case 7: imgspeak.setImageResource(R.drawable.magpie); break;
            case 8: imgspeak.setImageResource(R.drawable.chair); break;
        }

    }
    public void makesoundId(){
        switch (answer){
            case 0: soundId = soundPool.load(this,R.raw.kid,1); break;
            case 1: soundId = soundPool.load(this,R.raw.painter,1); break;
            case 2: soundId = soundPool.load(this,R.raw.root,1);break;
            case 3: soundId = soundPool.load(this,R.raw.singer,1);break;
            case 4: soundId = soundPool.load(this,R.raw.duck,1);break;
            case 5: soundId = soundPool.load(this,R.raw.wave,1);break;
            case 6: soundId = soundPool.load(this,R.raw.parents,1);break;
            case 7: soundId = soundPool.load(this,R.raw.magpie,1);break;
            case 8: soundId = soundPool.load(this,R.raw.chair,1);break;
        }

    }
    protected void onDestroy(){
        super.onDestroy();
        if (soundPool != null){
            soundPool.release();
            soundPool = null;
        }
    }
}