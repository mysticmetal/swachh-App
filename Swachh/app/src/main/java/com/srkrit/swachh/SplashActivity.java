package com.srkrit.swachh;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.View;
import 	android.view.Window;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Map;

public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }
    public void openReg(View view){
        Intent intent = new Intent(SplashActivity.this, Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(intent);
        finish();
    }


}
