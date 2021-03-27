package com.CodingErgo.sticker.MyManager;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class RunnableHandler {
    public  static  void TextViewChanger(Context context , TextView textView) {
        String [] tv = {"text1" , "text 2", "text 3"};
        CountDownTimer countDownTimer = new CountDownTimer(10000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
               for (int i = tv.length-1; ;){ textView.setText(tv[i]);
                   textView.setText(tv[i]);
                if (i==tv.length-1 ) i=0;{
                    start();
                   }
               }


            }
        }.start();

    }
}
