package com.example.uidesign;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.InputStream;
import com.example.profile.*;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDiyTheme();
        setContentView(R.layout.activity_login);
        this.setTitle(getText(R.string.login_page_title));

        /* 登录按钮设置 */
        String[] names = { ShareProfile.profile.USERID };
        String[] passwords = { ShareProfile.profile.getPassword() };
        final Button login = (Button)findViewById(R.id.login_button);
        final AutoCompleteTextView password = (AutoCompleteTextView) findViewById(R.id.login_password_edit);
        final AutoCompleteTextView name = (AutoCompleteTextView) findViewById(R.id.login_name_edit);
        ArrayAdapter<String> passwordAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, passwords);
        password.setAdapter(passwordAdapter);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);
        name.setAdapter(nameAdapter);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() == 0) {
                    Toast.makeText(LoginActivity.this, getString(R.string.name_cannot_empty)
                            , Toast.LENGTH_SHORT).show();
                }
                else if (password.getText().length() == 0) {
                    Toast.makeText(LoginActivity.this, getString(R.string.password_cannot_empty)
                            , Toast.LENGTH_SHORT).show();
                }
                else if (name.getText().toString().equals(ShareProfile.profile.USERID) &&
                        password.getText().toString().equals(ShareProfile.profile.getPassword())) {
                    login(LoginFrom.NAME);
                }
                else {
                    Toast.makeText(LoginActivity.this, getString(R.string.login_error),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* 注册按钮设置 */
        Button register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, getString(R.string.cannot_register),
                        Toast.LENGTH_SHORT).show();
            }
        });

        /* 按键主题设置 */
        switch (ShareProfile.theme) {
            case 0:
                login.setBackgroundResource(R.drawable.button_rectangle_purple);
                register.setBackgroundResource(R.drawable.button_rectangle_purple);
                break;
            case 1:
                login.setBackgroundResource(R.drawable.button_rectangle_red);
                register.setBackgroundResource(R.drawable.button_rectangle_red);
                break;
            case 2:
                login.setBackgroundResource(R.drawable.button_rectangle_yellow);
                register.setBackgroundResource(R.drawable.button_rectangle_yellow);
                break;
            case 3:
                login.setBackgroundResource(R.drawable.button_rectangle_green);
                register.setBackgroundResource(R.drawable.button_rectangle_green);
                break;
            case 4:
                login.setBackgroundResource(R.drawable.button_rectangle_pink);
                register.setBackgroundResource(R.drawable.button_rectangle_pink);
                break;
            case 5:
                login.setBackgroundResource(R.drawable.button_rectangle_blue);
                register.setBackgroundResource(R.drawable.button_rectangle_blue);
                break;
            default:
                login.setBackgroundResource(R.drawable.button_rectangle);
                register.setBackgroundResource(R.drawable.button_rectangle);
                break;
        }

        /* 微信登录按钮设置 */
        ImageButton weixin = (ImageButton) findViewById(R.id.button_weixin);
        weixin.setBackgroundResource(R.drawable.weixin);
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(LoginFrom.WEIXIN);
            }
        });

        /* 微博登录按钮设置 */
        ImageButton weibo = (ImageButton) findViewById(R.id.button_weibo);
        weibo.setBackgroundResource(R.drawable.weibo);
        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(LoginFrom.WEIBO);
            }
        });

        /* QQ登录按钮设置 */
        ImageButton qq = (ImageButton) findViewById(R.id.button_qq);
        qq.setBackgroundResource(R.drawable.qq);
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(LoginFrom.QQ);
            }
        });
    }

    private void setDiyTheme() {
        switch (ShareProfile.theme) {
            case 0:
                setTheme(R.style.PurpleTheme);
                break;
            case 1:
                setTheme(R.style.RedTheme);
                break;
            case 2:
                setTheme(R.style.YellowTheme);
                break;
            case 3:
                setTheme(R.style.GreenTheme);
                break;
            case 4:
                setTheme(R.style.PinkTheme);
                break;
            case 5:
                setTheme(R.style.BlueTheme);
                break;
            default:
                break;
        }
    }

    public void login(LoginFrom from) {
        dialog = new ProgressDialog(LoginActivity.this);
        String msg = "";
        switch (from) {
            case QQ:
                msg = getString(R.string.qq_login);
                break;
            case WEIBO:
                msg = getString(R.string.weibo_login);
                break;
            case WEIXIN:
                msg = getString(R.string.weixin_login);
                break;
            case NAME:
                msg = getString(R.string.name_login);
                break;
            default:
                return;
        }
        ShareProfile.profile.setFrom(from);
        dialog.setMessage(msg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        SwitchActivity.switchActivity(LoginActivity.this, ProfileActivity.class, true);
    }

    private long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(LoginActivity.this, getString(R.string.exit_program),
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout layout = (LinearLayout) findViewById(R.id.login_background);
        InputStream is ;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inSampleSize = 2;
        switch (ShareProfile.theme) {
            case 0:
                is = getResources().openRawResource(+R.drawable.purple_bg);
                break;
            case 1:
                is = getResources().openRawResource(+R.drawable.red_bg);
                break;
            case 2:
                is = getResources().openRawResource(+R.drawable.yellow_bg);
                break;
            case 3:
                is = getResources().openRawResource(+R.drawable.green_bg);
                break;
            case 4:
                is = getResources().openRawResource(+R.drawable.pink_bg);
                break;
            case 5:
                is = getResources().openRawResource(+R.drawable.blue_bg);
                break;
            default:
                is = getResources().openRawResource(+R.drawable.login);
                break;
        }
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
        bd.setAlpha(100); // 设置背景透明度
        layout.setBackgroundDrawable(bd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.login_about_item:
                new AboutDialog().showAboutDialog(LoginActivity.this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if(dialog != null) {
            dialog.dismiss();
        }
        super.onDestroy();
    }
}
