package com.example.withganada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kofigyan.stateprogressbar.StateProgressBar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

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
    private static String SERVER_IP = "192.168.219.100";
    private static String CONNECT_MSG = "connect";
    private static String STOP_MSG = "stop";

    private static int BUF_SIZE = 100;
    //



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


    private class Connect extends AsyncTask< String , String,Void > {
        private String output_message;
        private String input_message;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                client = new Socket(SERVER_IP, 12345);
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
                    else{
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

}