package com.example.uidesign;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profile.Profile;
import com.example.profile.ShareProfile;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDiyTheme();
        setContentView(R.layout.activity_profile);
        setTitle(getText(R.string.profile_page_title));

        /* 设置用户名主题颜色 */
        setIDTheme();

        /* 设置登录方式 */
        TextView loginFrom = (TextView) findViewById(R.id.login_from_text);
        Drawable loginDrawable;
        switch (ShareProfile.profile.getFrom()) {
            case NAME:
                loginFrom.setText(getText(R.string.login_from));
                loginDrawable = getResources().getDrawable(R.drawable.account);
                loginDrawable.setBounds(0,0,60,60);
                loginFrom.setCompoundDrawables(loginDrawable,null,null,null);
                break;
            case WEIXIN:
                loginFrom.setText(getText(R.string.login_from_weixin));
                loginDrawable = getResources().getDrawable(R.drawable.loginweixin);
                loginDrawable.setBounds(0,0,50,50);
                loginFrom.setCompoundDrawables(loginDrawable,null,null,null);
                break;
            case WEIBO:
                loginFrom.setText(getText((R.string.login_from_weibo)));
                loginDrawable = getResources().getDrawable(R.drawable.loginweibo);
                loginDrawable.setBounds(0,0,50,50);
                loginFrom.setCompoundDrawables(loginDrawable,null,null,null);
                break;
            case QQ:
                loginFrom.setText(getText(R.string.login_from_qq));
                loginDrawable = getResources().getDrawable(R.drawable.loginqq);
                loginDrawable.setBounds(0,0,50,50);
                loginFrom.setCompoundDrawables(loginDrawable,null,null,null);
                break;
            default:
                break;
        }

        /* 设置昵称 */
        TextView nickname = (TextView) findViewById(R.id.profile_nickname);
        nickname.setText(String.format(getString(R.string.nickname_display), ShareProfile.profile.getNickname().trim()));

        /* 设置性别 */
        TextView sex = (TextView) findViewById(R.id.profile_sex);
        Drawable sexDrawable;
        CircleImageView pic = (CircleImageView) findViewById(R.id.profile_pic);
        String sex_temp = "";
        switch (ShareProfile.profile.getSex()) {
            case MALE:
                sex_temp = getString(R.string.male);
                pic.setImageResource(R.drawable.male);
                pic.setBorderColor(getResources().getColor(R.color.maleColor));
                sexDrawable = getResources().getDrawable(R.drawable.maleicon);
                sexDrawable.setBounds(-1040,0,-990,50);
                sex.setCompoundDrawables(null, null, sexDrawable, null);
                break;
            case FEMALE:
                sex_temp = getString(R.string.female);
                pic.setImageResource(R.drawable.female);
                pic.setBorderColor(getResources().getColor(R.color.femaleColor));
                sexDrawable = getResources().getDrawable(R.drawable.femaleicon);
                sexDrawable.setBounds(-1040,0,-990,50);
                sex.setCompoundDrawables(null, null, sexDrawable, null);
                break;
            default:
                break;
        }
        sex.setText(String.format(getString(R.string.sex_display), sex_temp));

        /* 设置点赞 */
        final Button likes = (Button) findViewById(R.id.profile_likes);
        final Drawable likesDrawable = getResources().getDrawable(R.drawable.likes);
        final Drawable likesDrawableFill = getResources().getDrawable(R.drawable.likesfill);
        likesDrawable.setBounds(0,0,60,60);
        likesDrawableFill.setBounds(0,0,60,60);
        likes.setText(String.format(getString(R.string.likes_display), ShareProfile.profile.getLikes()));
        likes.setTextColor(getResources().getColor(R.color.likesColor));
        if (ShareProfile.liked)
            likes.setCompoundDrawables(likesDrawableFill, null, null ,null);
        else
            likes.setCompoundDrawables(likesDrawable, null, null ,null);
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = ShareProfile.profile.getLikes();
                temp++;
                likes.setText(String.format(getString(R.string.likes_display), temp));
                likes.setCompoundDrawables(likesDrawableFill, null, null ,null);
                ShareProfile.profile.setLikes(temp);
                ShareProfile.liked = true;
            }
        });

        /* 设置生日相关信息 */
        TextView birthday = (TextView) findViewById(R.id.profile_birthday);
        TextView age = (TextView) findViewById(R.id.profile_age);
        TextView starSign = (TextView) findViewById(R.id.profile_star_sign);
        TextView zodiac = (TextView) findViewById(R.id.profile_zodiac);
        if (ShareProfile.profile.hasBirthday()) {
            birthday.setText(String.format(getString(R.string.birthday_display), ShareProfile.profile.getBirthdayString()));
            age.setText(String.format(getString(R.string.age_display),
                    String.format(getString(R.string.age_format), ShareProfile.profile.getAge())));
            starSign.setText(String.format(getString(R.string.star_sign_display),
                    Profile.star_sign[ShareProfile.profile.getStarSign()]));
            zodiac.setText(String.format(getString(R.string.zodiac_display),
                    String.valueOf(Profile.zodiac.toCharArray()[ShareProfile.profile.getZodiac()])));
            setZodiacPic(zodiac, ShareProfile.profile.getZodiac());
            setStarSignPic(starSign, ShareProfile.profile.getStarSign());
        }
        else {
            birthday.setText(String.format(getString(R.string.birthday_display), getString(R.string.display_default)));
            age.setText(String.format(getString(R.string.age_display), getString(R.string.display_default)));
            starSign.setText(String.format(getString(R.string.star_sign_display), getString(R.string.display_default)));
            zodiac.setText(String.format(getString(R.string.zodiac_display), getString(R.string.display_default)));
        }

        /* 设置个性签名 */
        TextView motto = (TextView) findViewById(R.id.profile_motto_content);
        String motto_temp = ShareProfile.profile.getMotto();
        if (motto_temp.length() != 0) {
            motto.setText(motto_temp.trim());
        }

        /* 设置个人介绍 */
        TextView intro = (TextView) findViewById(R.id.profile_introduction_content);
        intro.setText(ShareProfile.profile.genIntroduction());
    }

    private void setZodiacPic(TextView view, int zodiac) {
        Drawable drawable;
        switch (zodiac) {
            case 0:
                drawable = getResources().getDrawable(R.drawable.monkey);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 1:
                drawable = getResources().getDrawable(R.drawable.chicken);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 2:
                drawable = getResources().getDrawable(R.drawable.dog);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 3:
                drawable = getResources().getDrawable(R.drawable.pig);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 4:
                drawable = getResources().getDrawable(R.drawable.mouse);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 5:
                drawable = getResources().getDrawable(R.drawable.cow);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 6:
                drawable = getResources().getDrawable(R.drawable.tiger);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 7:
                drawable = getResources().getDrawable(R.drawable.rabbit);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 8:
                drawable = getResources().getDrawable(R.drawable.dragon);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 9:
                drawable = getResources().getDrawable(R.drawable.snake);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 10:
                drawable = getResources().getDrawable(R.drawable.horse);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 11:
                drawable = getResources().getDrawable(R.drawable.goat);
                drawable.setBounds(-400,0,-340,60);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            default:
                break;
        }
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

    private void setIDTheme() {
        TextView id = (TextView) findViewById(R.id.profile_id);
        switch (ShareProfile.theme) {
            case 0:
                id.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                id.setTextColor(getResources().getColor(R.color.RedColorPrimary));
                break;
            case 2:
                id.setTextColor(getResources().getColor(R.color.YellowColorPrimary));
                break;
            case 3:
                id.setTextColor(getResources().getColor(R.color.GreenColorPrimary));
                break;
            case 4:
                id.setTextColor(getResources().getColor(R.color.PinkColorPrimary));
                break;
            case 5:
                id.setTextColor(getResources().getColor(R.color.BlueColorPrimary));
                break;
            default:
                break;
        }
    }

    private void setStarSignPic(TextView view, int starSign) {
        Drawable drawable;
        switch (starSign) {
            case 0:
                drawable = getResources().getDrawable(R.drawable.capricorn);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 1:
                drawable = getResources().getDrawable(R.drawable.aquarius);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 2:
                drawable = getResources().getDrawable(R.drawable.pisces);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 3:
                drawable = getResources().getDrawable(R.drawable.aries);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 4:
                drawable = getResources().getDrawable(R.drawable.taurus);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 5:
                drawable = getResources().getDrawable(R.drawable.gemini);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 6:
                drawable = getResources().getDrawable(R.drawable.cancer);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 7:
                drawable = getResources().getDrawable(R.drawable.leo);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 8:
                drawable = getResources().getDrawable(R.drawable.virgo);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 9:
                drawable = getResources().getDrawable(R.drawable.libra);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 10:
                drawable = getResources().getDrawable(R.drawable.horse);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            case 11:
                drawable = getResources().getDrawable(R.drawable.sagittarius);
                drawable.setBounds(-250,0,-195,55);
                view.setCompoundDrawables(null,null,drawable,null);
                break;
            default:
                break;
        }
    }

    void switchTheme() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle(getString(R.string.switch_theme_text));
        final String[] items = { getString(R.string.purple_theme),
                getString(R.string.red_theme),
                getString(R.string.yellow_theme),
                getString(R.string.green_theme),
                getString(R.string.pink_theme),
                getString(R.string.blue_theme) };
        final boolean[] checkedItems = new boolean[items.length];
        builder.setSingleChoiceItems(items, ShareProfile.theme, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                }
                checkedItems[which] = true;
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        ShareProfile.theme = i;
                        break;
                    }
                }
                dialog.dismiss();
                // 重启当前界面才能使重新设置的主题生效
                finish();
                startActivity(getIntent());
                Toast.makeText(ProfileActivity.this, getString(R.string.set_theme_success),
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_memu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile_edit_item:
                SwitchActivity.switchActivity(ProfileActivity.this,
                        EditProfileActivity.class, true);
                break;
            case R.id.profile_exit_item:
                SwitchActivity.switchActivity(ProfileActivity.this,
                        LoginActivity.class, true);
                Toast.makeText(ProfileActivity.this, getString(R.string.exit_login_success), Toast.LENGTH_SHORT).show();
                break;
            case R.id.switch_theme_item:
                switchTheme();
                break;
            case R.id.profile_about_item:
                new AboutDialog().showAboutDialog(ProfileActivity.this);// 弹出自定义的版权对话框
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(ProfileActivity.this, getString(R.string.exit_program),
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
        ScrollView layout = (ScrollView) ProfileActivity.this.findViewById(R.id.profile_background_scroll);
        InputStream is;
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
}
