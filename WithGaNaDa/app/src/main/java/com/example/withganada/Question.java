package com.example.withganada;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    Button CheckAns;
    TextView StageIndex;
    int Currentstate = 1;
    int Stagenum;
    StateProgressBar stateProgressBar;

    //통신 헤더 선언부
    Button send_button;
    EditText send_editText;
    TextView send_textView;
    TextView read_textView;
    private Socket client;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;
    private static String SERVER_IP = "116.41.108.67";
    private static String CONNECT_MSG = "connect";
    private static String STOP_MSG = "stop";

    private static int BUF_SIZE = 100;
    //


    MediaRecorder recorder;
    String fileName;
    MediaPlayer mediaPlayer;
    int position = 0;

    ProgressDialog progressDialog;
    Button upload_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Intent intent = getIntent(); /*데이터 수신*/
        Stagenum = intent.getExtras().getInt("stageIndex");
        //어떤 단원의 문제인지 데이터 넘겨받기

        permissionCheck();      //마이크 권한 체크

        StageIndex = findViewById(R.id.textView2);
        stateProgressBar = findViewById(R.id.progress_bar);
        SetProgressView();


        send_button = findViewById(R.id.send_button);
        send_editText = findViewById(R.id.send_editText);
        send_textView = findViewById(R.id.send_textView);
        read_textView = findViewById(R.id.read_textView);

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connect connect = new Connect();
                connect.execute(CONNECT_MSG);
            }
        });


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
                    //여기에서 별표 점수 데이터를 넘겨줍니다.
                }
                else {
                    Currentstate++;
                    SetProgressView();
                }
                /////////////////////////////////////////////

            }
        });

        // SD 카드 폴더 지정
        File file = new File(getApplicationInfo().dataDir, "recorded.mp4");
        fileName = file.getAbsolutePath();  // 파일 위치 가져옴
        Toast.makeText(getApplicationContext(), "파일 위치:"+fileName, Toast.LENGTH_SHORT).show();

        findViewById(R.id.test_rec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 녹음 시작
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
                    Toast.makeText(getApplicationContext(), "녹음시작", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {e.printStackTrace();}
            }
        });

        findViewById(R.id.test_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // 녹음 중지
                if (recorder != null) {
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                }
                Toast.makeText(getApplicationContext(), "녹음중지", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "재생시작", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        findViewById(R.id.test_p_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 정지
                if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop(); // 정지
                    Toast.makeText(getApplicationContext(), "정지", Toast.LENGTH_SHORT).show();
                    //save();
                }
            }
        });



        findViewById(R.id.test_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //파일 업로드 테스트

                uploadFile();
                Connect connect = new Connect();
                connect.execute(CONNECT_MSG);}
        });


    }

    // 녹음한 파일 저장
    public Uri save(){
        ContentValues values = new ContentValues(10);
        values.put(MediaStore.MediaColumns.TITLE, "Recorded");
        values.put(MediaStore.Audio.Media.ALBUM, "Audio_Album");
        values.put(MediaStore.Audio.Media.ARTIST, "Ton");
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "Recorded Audio");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, 1);
        values.put(MediaStore.Audio.Media.IS_MUSIC, 1);
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis()/1000);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4"); // 미디어 파일의 포맷
        values.put(MediaStore.Audio.Media.DATA, fileName); // 저장된 녹음 파일

        // ContentValues 객체를 추가할 때, 음성 파일에 대한 내용 제공자 URI 사용
        return getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
    }


    public void permissionCheck(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);        }

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


    private class Connect extends AsyncTask< String , String,Void > {
        private String output_message;
        private String input_message;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                client = new Socket(SERVER_IP, 12344);
                dataOutput = new DataOutputStream(client.getOutputStream());
                dataInput = new DataInputStream(client.getInputStream());
                output_message = strings[0];
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
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... params){
            send_textView.setText(""); // Clear the chat box
            send_textView.append("보낸 메세지: " + output_message );
            read_textView.setText(""); // Clear the chat box
            read_textView.append("받은 메세지: " + params[0]);
        }
    }


    private void uploadFile(){      //recorded.mp4 파일을 서버로 업로드하는 구문입니다.
        //final EditText name = (EditText) findViewById();        //

        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, "testna");

        File originalFile = new File(getApplicationInfo().dataDir, "recorded.mp4");

        RequestBody filePart = RequestBody.create(
                //MediaType.parse(getContentResolver().getType(fileUri)),
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
        Call<ResponseBody> call = client.uploadPhoto(descriptionPart, file);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(),"업로드 성공!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"업로드 실패",Toast.LENGTH_SHORT).show();
            }
        });

    }


}