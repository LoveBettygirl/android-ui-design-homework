package com.example.uidesign;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profile.Profile;
import com.example.profile.Sex;
import com.example.profile.ShareProfile;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.uidesign.BuildConfig.DEBUG;

public class EditProfileActivity extends AppCompatActivity {

    private static final int nicknameCharMaxNum = 20;
    private static final int phoneCharMaxNum = 15;
    private static final int phoneCharMinNum = 3;
    private static final int mottoCharMaxNum = 50;
    private int sum = 0;
    private int hasError = 0;
    private static String emailRegex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Profile tempProfile = new Profile();
    private RandomListener rand = new RandomListener(EditProfileActivity.this);

    private void setEditProgress(int index) {
        final ProgressBar progress = (ProgressBar) findViewById(R.id.profile_progress);
        final TextView progressText = (TextView) findViewById(R.id.profile_progress_text);
        sum += index;
        if (sum < 0 || sum > 100) { // 设置进度条的值不能超出范围
            throw new IndexOutOfBoundsException("Progress out of bounds!");
        }
        progress.setProgress(sum);
        progressText.setText(String.format(getString(R.string.progress_display), sum));
        if (sum == 100) {
            Drawable drawable = getResources().getDrawable(R.drawable.happy);
            drawable.setBounds(0,0,55,55);
            progressText.setCompoundDrawables(null, null, drawable, null);
        }
        else {
            progressText.setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDiyTheme();
        setContentView(R.layout.activity_edit_profile);
        setTitle(getText(R.string.edit_profile_page_title));

        /* 左上角返回按钮设置 */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 设置显示返回按钮
        getSupportActionBar().setHomeButtonEnabled(true); // 设置返回按钮允许按下

        /* 生成临时资料，保存时将临时资料中的内容copy过去 */
        tempProfile.copyProfile(ShareProfile.profile);

        /* 进度条相关设置 */
        final ProgressBar progress = (ProgressBar) findViewById(R.id.profile_progress);
        final TextView progressText = (TextView) findViewById(R.id.profile_progress_text);
        progress.setOnClickListener(rand);
        progressText.setOnClickListener(rand);

        /* 昵称设置 */
        final EditText nicknameEdit = (EditText) findViewById(R.id.nickname_edit);
        final TextView nicknameText = (TextView) findViewById(R.id.nickname_text);
        final TextView nicknameTips = (TextView) findViewById(R.id.nickname_tips);
        nicknameEdit.setText(tempProfile.getNickname());
        nicknameEdit.setSelection(tempProfile.getNickname().length()); // 设置默认光标位置
        final int nicknameTipsColor = nicknameTips.getCurrentTextColor(); // 获取原来的文字颜色
        final int nicknameTextColor = nicknameText.getCurrentTextColor(); // 获取原来的文字颜色
        nicknameTips.setText(String.format(getString(R.string.nickname_tips_content),
                nicknameCharMaxNum - nicknameEdit.getText().length()));
        if (nicknameEdit.getText().toString().length() > 0) {
            setEditProgress(11);
        }
        else {
            nicknameTips.setTextColor(getResources().getColor(R.color.errorColor));
            nicknameText.setTextColor(getResources().getColor(R.color.errorColor));
            hasError++;
        }
        nicknameEdit.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;// 监听前的文本
            private int editStart;// 光标开始位置
            private int editEnd;// 光标结束位置

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
                if (s.length() == 0) {
                    setEditProgress(11);
                    nicknameTips.setTextColor(nicknameTipsColor);
                    nicknameText.setTextColor(nicknameTextColor);
                    hasError--;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nicknameTips.setText(String.format(getString(R.string.nickname_tips_content),
                        nicknameCharMaxNum - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = nicknameEdit.getSelectionStart();
                editEnd = nicknameEdit.getSelectionEnd();
                if (temp.length() == 0) {
                    nicknameTips.setText(getString(R.string.nickname_cannot_empty));
                    nicknameTips.setTextColor(getResources().getColor(R.color.errorColor));
                    nicknameText.setTextColor(getResources().getColor(R.color.errorColor));
                    setEditProgress(-11);
                    hasError++;
                }
                else if (temp.length() > nicknameCharMaxNum) {
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    nicknameEdit.setText(s);
                    nicknameEdit.setSelection(tempSelection);
                }
                tempProfile.setNickname(nicknameEdit.getText().toString());
            }
        });
        nicknameText.setOnClickListener(rand);
        nicknameTips.setOnClickListener(rand);

        final TextView mustText = (TextView) findViewById(R.id.must_hint_text);
        int mustTextColor = mustText.getCurrentTextColor();
        mustText.setOnClickListener(rand);

        /* 手机号设置 */
        final EditText phoneEdit = (EditText) findViewById(R.id.phone_number_edit);
        final TextView phoneText = (TextView) findViewById(R.id.phone_number_text);
        final TextView phoneTips = (TextView) findViewById(R.id.phone_number_tips);
        phoneEdit.setText(tempProfile.getPhone());
        phoneEdit.setSelection(tempProfile.getPhone().length()); // 设置默认光标位置
        final int phoneTipsColor = phoneTips.getCurrentTextColor(); // 获取原来的文字颜色
        final int phoneTextColor = phoneText.getCurrentTextColor(); // 获取原来的文字颜色
        if (tempProfile.getPhone().length() > 0 && tempProfile.getPhone().length() < phoneCharMinNum) {
            phoneTips.setText(String.format(getString(R.string.phone_tips_content_less),
                    phoneCharMaxNum - phoneEdit.getText().length()));
            phoneTips.setTextColor(getResources().getColor(R.color.errorColor));
            phoneText.setTextColor(getResources().getColor(R.color.errorColor));
            hasError++;
        }
        else if (tempProfile.getPhone().length() == 0 || tempProfile.getPhone().length() >= phoneCharMinNum) {
            phoneTips.setText(String.format(getString(R.string.phone_tips_content_more),
                    phoneCharMaxNum - phoneEdit.getText().length()));
            if(tempProfile.getPhone().length() != 0) {
                setEditProgress(11);
            }
        }
        phoneEdit.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;// 监听前的文本
            private int editStart;// 光标开始位置
            private int editEnd;// 光标结束位置
            private int tempNum;// 文本框内容改变前的文字长度

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
                tempNum = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phoneEdit.getText().length() > 0 && phoneEdit.getText().length() < phoneCharMinNum) {
                    phoneTips.setText(String.format(getString(R.string.phone_tips_content_less),
                            phoneCharMaxNum - phoneEdit.getText().length()));
                }
                else {
                    phoneTips.setText(String.format(getString(R.string.phone_tips_content_more),
                            phoneCharMaxNum - phoneEdit.getText().length()));
                }
                mustText.setTextColor(getResources().getColor(R.color.yesColor));
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = phoneEdit.getSelectionStart();
                editEnd = phoneEdit.getSelectionEnd();
                if (phoneEdit.getText().length() > 0  && phoneEdit.getText().length() < phoneCharMinNum) {
                    phoneTips.setTextColor(getResources().getColor(R.color.errorColor));
                    phoneText.setTextColor(getResources().getColor(R.color.errorColor));
                    if(phoneEdit.getText().length() == phoneCharMinNum - 1 && tempNum == phoneCharMinNum) {
                        setEditProgress(-11);
                        hasError++;
                    }
                    if (tempNum == 0) {
                        hasError++;
                    }
                }
                else if (temp.length() > phoneCharMaxNum) {
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    phoneEdit.setText(s);
                    phoneEdit.setSelection(tempSelection);
                }
                else if (temp.length() == 0) {
                    phoneTips.setTextColor(phoneTipsColor);
                    phoneText.setTextColor(phoneTextColor);
                    hasError--;
                }
                else if (temp.length() == phoneCharMinNum) {
                    if(tempNum == phoneCharMinNum - 1) {
                        setEditProgress(11);
                        phoneTips.setTextColor(phoneTipsColor);
                        phoneText.setTextColor(phoneTextColor);
                        hasError--;
                    }
                }
                tempProfile.setPhone(phoneEdit.getText().toString());
            }
        });
        phoneText.setOnClickListener(rand);
        phoneTips.setOnClickListener(rand);

        /* 邮箱设置 */
        final EditText emailEdit = (EditText) findViewById(R.id.email_edit);
        final TextView emailText = (TextView) findViewById(R.id.email_text);
        final TextView emailTips = (TextView) findViewById(R.id.email_tips);
        emailEdit.setText(tempProfile.getEmail());
        emailEdit.setSelection(tempProfile.getEmail().length()); // 设置默认光标位置
        final int emailTipsColor = emailTips.getCurrentTextColor(); // 获取原来的文字颜色
        final int emailTextColor = emailText.getCurrentTextColor(); // 获取原来的文字颜色
        if (tempProfile.getEmail().length() == 0) {
            emailTips.setText(getString(R.string.email_tips_content_empty));
        }
        else {
            if (tempProfile.getEmail().matches(emailRegex)) {
                emailTips.setText(getString(R.string.email_tips_content_matched));
                setEditProgress(11);
            }
            else {
                emailTips.setText(getString(R.string.email_tips_content_unmatched));
                emailTips.setTextColor(getResources().getColor(R.color.errorColor));
                emailText.setTextColor(getResources().getColor(R.color.errorColor));
                hasError++;
            }
        }
        emailEdit.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;// 监听前的文本
            private boolean tempMatched;// 文本框内容改变前是否匹配emailRegex
            private int tempNum;// 文本框内容改变前的文字长度

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
                tempNum = s.toString().length();
                tempMatched = s.toString().matches(emailRegex);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (emailEdit.getText().toString().length() == 0) {
                    emailTips.setText(getString(R.string.email_tips_content_empty));
                    emailTips.setTextColor(emailTipsColor);
                    emailText.setTextColor(emailTextColor);
                }
                else {
                    if (emailEdit.getText().toString().matches(emailRegex)) {
                        emailTips.setText(getString(R.string.email_tips_content_matched));
                        emailTips.setTextColor(emailTipsColor);
                        emailText.setTextColor(emailTextColor);
                    }
                    else {
                        emailTips.setText(getString(R.string.email_tips_content_unmatched));
                        emailTips.setTextColor(getResources().getColor(R.color.errorColor));
                        emailText.setTextColor(getResources().getColor(R.color.errorColor));
                    }
                }
                mustText.setTextColor(getResources().getColor(R.color.yesColor));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (emailEdit.getText().toString().length() != 0)  {
                    if (tempNum == 0) {
                        if (!emailEdit.getText().toString().matches(emailRegex)) {
                            hasError++;
                        }
                        else {
                            setEditProgress(11);
                        }
                    }
                    else if (!emailEdit.getText().toString().matches(emailRegex) && tempMatched) {
                        setEditProgress(-11);
                        hasError++;
                    }
                    else if(emailEdit.getText().toString().matches(emailRegex) && !tempMatched) {
                        setEditProgress(11);
                        hasError--;
                    }
                }
                else {
                    if (tempNum > 0 && !tempMatched) {
                        hasError--;
                    }
                }
                tempProfile.setEmail(emailEdit.getText().toString());
            }
        });
        emailText.setOnClickListener(rand);
        emailTips.setOnClickListener(rand);

        /* 性别设置 */
        RadioGroup sex = (RadioGroup) findViewById(R.id.sex_group);
        final TextView sexText = (TextView) findViewById(R.id.sex_text);
        Drawable femaleDrawable = getResources().getDrawable(R.drawable.femaleicon);
        Drawable maleDrawable = getResources().getDrawable(R.drawable.maleicon);
        femaleDrawable.setBounds(5,0,55,50);
        maleDrawable.setBounds(5,0,55,50);
        RadioButton female = (RadioButton) findViewById(R.id.sex_female);
        RadioButton male = (RadioButton) findViewById(R.id.sex_male);
        female.setCompoundDrawables(null, null ,femaleDrawable, null);
        male.setCompoundDrawables(null, null ,maleDrawable, null);
        setEditProgress(11);
        if (tempProfile.getSex() == Sex.FEMALE) {
            female.setChecked(true);
            sexText.setTextColor(getResources().getColor(R.color.femaleColor));
        }
        else {
            male.setChecked(true);
            sexText.setTextColor(getResources().getColor(R.color.maleColor));
        }
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.sex_female) {
                    tempProfile.setSex(Sex.FEMALE);
                    sexText.setTextColor(getResources().getColor(R.color.femaleColor));
                }
                else {
                    tempProfile.setSex(Sex.MALE);
                    sexText.setTextColor(getResources().getColor(R.color.maleColor));
                }
            }
        });
        sexText.setOnClickListener(rand);

        /* 生日设置 */
        final Button birthday = (Button) findViewById(R.id.birthday_set);
        final TextView birthdayText = (TextView) findViewById(R.id.birthday_text);
        final int birthdayColor = birthdayText.getCurrentTextColor();
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        if (tempProfile.hasBirthday()) {
            birthday.setText(tempProfile.getBirthdayString());
            c.set(tempProfile.getBirthYear(),
                    tempProfile.getBirthMonth() - 1,
                    tempProfile.getBirthDay());
            setEditProgress(11);
        }
        else {
            birthday.setText(dateFormat.format(date));
        }
        final DatePickerDialog pickerDialog = new DatePickerDialog(this,
                DatePickerDialog.THEME_DEVICE_DEFAULT_DARK, null,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        final DatePicker picker = pickerDialog.getDatePicker();
        picker.setMaxDate(date.getTime());//设置最大日期，避免用户在设置生日的时候超过当前日期
        /* 注意Date.getTime()和Calender.getTimeInMillis()返回的都是从1970-01-01开始的毫秒数
        通过setMinDate()能设置的最小日期也只能是1970-01-01
        Calendar cMin = Calendar.getInstance();
        cMin.set(1, 0, 1);
        picker.setMinDate(c.getTimeInMillis());//设置最小日期
         */
        pickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        birthdayText.setTextColor(birthdayColor);
                    }
                });
        pickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = picker.getYear();
                        int monthOfYear = picker.getMonth();
                        int dayOfMonth = picker.getDayOfMonth();
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        if (!tempProfile.hasBirthday()) {
                            setEditProgress(11);
                        }
                        tempProfile.setBirthday(year, monthOfYear + 1, dayOfMonth);
                        Date dat = cal.getTime();
                        birthday.setText(dateFormat.format(dat));
                        birthdayText.setTextColor(getResources().getColor(R.color.yesColor));
                    }
                });
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDialog.show();
            }
        });
        birthdayText.setOnClickListener(rand);

        /* 所在地设置 */
        Spinner location = (Spinner) findViewById(R.id.select_province);
        final TextView locationText = (TextView) findViewById(R.id.province_text);
        final int locationColor = locationText.getCurrentTextColor();
        location.setSelection(tempProfile.getLocation() + 1);
        if (tempProfile.getLocation() != -1) {
            setEditProgress(11);
            locationText.setTextColor(getResources().getColor(R.color.yesColor));
        }
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    if (tempProfile.getLocation() == -1) {
                        setEditProgress(11);
                        locationText.setTextColor(getResources().getColor(R.color.yesColor));
                    }
                }
                else {
                    if (tempProfile.getLocation() != -1) {
                        setEditProgress(-11);
                        locationText.setTextColor(locationColor);
                    }
                }
                tempProfile.setLocation(position - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        locationText.setOnClickListener(rand);

        /* 职业设置 */
        Spinner job = (Spinner) findViewById(R.id.select_job);
        final TextView jobText = (TextView) findViewById(R.id.job_text);
        final int jobColor = jobText.getCurrentTextColor();
        job.setSelection(tempProfile.getJob() + 1);
        if (tempProfile.getJob() != -1) {
            setEditProgress(11);
            jobText.setTextColor(getResources().getColor(R.color.yesColor));
        }
        job.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    if (tempProfile.getJob() == -1) {
                        setEditProgress(11);
                        jobText.setTextColor(getResources().getColor(R.color.yesColor));
                    }
                }
                else {
                    if (tempProfile.getJob() != -1) {
                        setEditProgress(-11);
                        jobText.setTextColor(jobColor);
                    }
                }
                tempProfile.setJob(position - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        jobText.setOnClickListener(rand);

        /* 兴趣爱好设置 */
        final TextView hobbyDis = (TextView) findViewById(R.id.hobby_text);
        final CheckBox[] hobbies = new CheckBox[Profile.hobbies.length];
        hobbies[0] = (CheckBox) findViewById(R.id.travel_button);
        hobbies[1] = (CheckBox) findViewById(R.id.art_button);
        hobbies[2] = (CheckBox) findViewById(R.id.sports_button);
        hobbies[3] = (CheckBox) findViewById(R.id.food_button);
        hobbies[4] = (CheckBox) findViewById(R.id.movie_music_button);
        hobbies[5] = (CheckBox) findViewById(R.id.play_button);
        hobbies[6] = (CheckBox) findViewById(R.id.chat_button);
        hobbies[7] = (CheckBox) findViewById(R.id.study_button);
        hobbies[8] = (CheckBox) findViewById(R.id.stay_home_button);
        hobbies[9] = (CheckBox) findViewById(R.id.sleep_button);
        boolean[] profileHobby = tempProfile.getHobby();
        for (int i = 0; i < profileHobby.length; i++) {
            if (profileHobby[i])
                hobbies[i].setChecked(true);
            else
                hobbies[i].setChecked(false);
        }
        final int hobbyCount = tempProfile.getHobbyCount();
        if (hobbyCount != 0)
            setEditProgress(11);
        hobbyDis.setText(String.format(getString(R.string.hobby_count_display), hobbyCount));
        CompoundButton.OnCheckedChangeListener hobbyListener = new CompoundButton.OnCheckedChangeListener() {
            private int prevHobbyCount = hobbyCount; // 记录上次选中的数目
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0;i < hobbies.length; i++) {
                    if (hobbies[i].isChecked())
                        tempProfile.addHobby(i);
                    else
                        tempProfile.deleteHobby(i);
                }
                int currHobbyCount = tempProfile.getHobbyCount();
                if (currHobbyCount == 0) {
                    if (prevHobbyCount > 0)
                        setEditProgress(-11);
                }
                else {
                    if (prevHobbyCount == 0)
                        setEditProgress(11);
                }
                hobbyDis.setText(String.format(getString(R.string.hobby_count_display), currHobbyCount));
                prevHobbyCount = currHobbyCount;
            }
        };
        for (CheckBox hobby : hobbies) {
            hobby.setOnCheckedChangeListener(hobbyListener);
        }
        hobbyDis.setOnClickListener(rand);

        /* 个性签名设置 */
        final TextView mottoTips = (TextView) findViewById(R.id.motto_text);
        final EditText mottoEdit = (EditText) findViewById(R.id.motto_edit);
        mottoEdit.setText(tempProfile.getMotto());
        mottoEdit.setSelection(tempProfile.getMotto().length()); // 设置默认光标位置
        final int mottoTipsColor = mottoTips.getCurrentTextColor(); // 获取原来的文字颜色
        if (tempProfile.getMotto().length() <= mottoCharMaxNum) {
            if (tempProfile.getMotto().length() > 0) {
                setEditProgress(12);
            }
            mottoTips.setText(String.format(getString(R.string.motto_display_less),
                    tempProfile.getMotto().length()));
        }
        else {
            mottoTips.setText(String.format(getString(R.string.motto_display_more),
                    tempProfile.getMotto().length()));
            mottoTips.setTextColor(getResources().getColor(R.color.errorColor));
            hasError++;
        }
        mottoEdit.setOnKeyListener(new View.OnKeyListener() {
            int prevCount = tempProfile.getMotto().length();
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String str = mottoEdit.getText().toString();
                int currCount = str.length();
                if (str.length() == 0) {
                    if (prevCount > 0) {
                        setEditProgress(-12);
                    }
                    mottoTips.setText(String.format(getString(R.string.motto_display_less),
                            currCount));
                    mottoTips.setTextColor(mottoTipsColor);
                }
                else if (str.length() <= mottoCharMaxNum) {
                    if (prevCount == 0 || prevCount > mottoCharMaxNum) {
                        setEditProgress(12);
                        if (prevCount > mottoCharMaxNum) {
                            hasError--;
                        }
                    }
                    mottoTips.setText(String.format(getString(R.string.motto_display_less),
                            currCount));
                    mottoTips.setTextColor(mottoTipsColor);
                }
                else {
                    if (prevCount <= mottoCharMaxNum) {
                        setEditProgress(-12);
                        hasError++;
                    }
                    mottoTips.setText(String.format(getString(R.string.motto_display_more),
                            currCount));
                    mottoTips.setTextColor(getResources().getColor(R.color.errorColor));
                }
                tempProfile.setMotto(str);
                prevCount = currCount;
                return false;
            }
        });
        mottoTips.setOnClickListener(rand);
    }

    private void save() {
        if (hasError == 0) {
            ShareProfile.profile.copyProfile(tempProfile);
            SwitchActivity.switchActivity(EditProfileActivity.this,
                    ProfileActivity.class, true);
            Toast.makeText(EditProfileActivity.this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(EditProfileActivity.this, getString(R.string.save_fail),
                    Toast.LENGTH_SHORT).show();
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

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle(getString(R.string.give_up));
        builder.setMessage(getString(R.string.give_up_q));
        builder.setPositiveButton(getText(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SwitchActivity.switchActivity(EditProfileActivity.this,
                        ProfileActivity.class, true);
            }
        });
        builder.setNegativeButton(getText(R.string.cancel), null);
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // 注意是 android.R.id.home 不是 R.id.home
                showSaveDialog();
                break;
            case R.id.save_button:
                save();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScrollView layout = (ScrollView) EditProfileActivity.this.findViewById(R.id.edit_profile_background_scroll);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showSaveDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
