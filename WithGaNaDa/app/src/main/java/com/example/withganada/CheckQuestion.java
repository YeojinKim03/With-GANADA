package com.example.withganada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckQuestion extends AppCompatActivity {

    Button btn_tomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_question);

        btn_tomain = findViewById(R.id.finish);
        btn_tomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);	//메인액티비티로 이동
                finish();	//스테이지 완료 액티비티 종료
            }
        });

    }
}