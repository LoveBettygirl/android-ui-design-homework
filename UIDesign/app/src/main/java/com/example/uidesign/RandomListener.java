package com.example.uidesign;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class RandomListener implements View.OnClickListener {
    private static String[] str = {
            "？？？",
            "别点我呀~",
            "干什么？",
            "有事吗？"
    };
    private AppCompatActivity activity;
    public RandomListener(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Random random = new Random();
        Toast.makeText(activity, str[random.nextInt(4)], Toast.LENGTH_SHORT).show();
    }
}
