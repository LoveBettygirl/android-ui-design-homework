package com.example.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.io.InputStream;

import androidx.constraintlayout.widget.ConstraintLayout;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 3秒后自动切换到登录界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SwitchActivity.switchActivity(MainActivity.this, LoginActivity.class, true);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in); // 淡出淡入效果
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.main_background);
        InputStream is;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inSampleSize = 2;
        is = getResources().openRawResource(+R.drawable.welcome);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
        layout.setBackgroundDrawable(bd);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.exit(0);
        return super.onKeyDown(keyCode, event);
    }
}
