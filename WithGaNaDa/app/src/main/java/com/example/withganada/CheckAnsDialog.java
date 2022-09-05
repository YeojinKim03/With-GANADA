package com.example.withganada;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CheckAnsDialog extends Dialog {
    private TextView txt_contents;
    private Button shutdownClick;

    @SuppressLint("ResourceAsColor")
    public CheckAnsDialog(@NonNull Context context, String contents, int ans, int select) {
        super(context);
        setContentView(R.layout.activity_custom_dialog);

        txt_contents = findViewById(R.id.txt_contents);
        txt_contents.setText(contents);
        shutdownClick = findViewById(R.id.btn_shutdown);

        if (ans == select){     //정답인경우
            shutdownClick.setText("다음으로");
            txt_contents.setText("정답입니다");
                    }
        else{

        }




        shutdownClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ans == select){     //정답인경우


                }
                else{
                    dismiss();      //여기서 다음 액티비티로 넘어갈건지 확인해줌
                }

                }


        });





    }
}
