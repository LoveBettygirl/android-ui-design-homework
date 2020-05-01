package com.example.uidesign;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class SwitchActivity {
    public static void switchActivity(AppCompatActivity srcActivity, Class datActivity, boolean finish) {
        Intent intent = new Intent(srcActivity, datActivity);
        srcActivity.startActivity(intent);
        if (finish) {
            srcActivity.finish();
        }
    }
}
