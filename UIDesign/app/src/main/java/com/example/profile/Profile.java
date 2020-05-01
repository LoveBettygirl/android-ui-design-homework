package com.example.profile;

import java.text.SimpleDateFormat;
import java.util.*;

public class Profile {
    public static final String[] star_sign = {"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座",
            "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座"};
    public static final String zodiac = "猴鸡狗猪鼠牛虎兔龙蛇马羊";
    public static final String[] provinces = {"北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林",
            "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东",
            "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆",
            "台湾", "香港", "澳门"};
    public static final String[] jobs = {"学生", "从政", "领域大佬", "普通上班族", "程序员",
            "老板", "搬砖工人", "待在家", "其他"};
    public static final String[] hobbies = {"旅行和摄影", "文学和艺术", "体育运动", "美食", "影视和音乐",
            "休闲娱乐", "社交", "学习", "宅家", "睡觉"};

    public final String USERID = "1711425"; // 用户名
    private String password = "123456"; // 密码
    private String nickname; // 昵称
    private LoginFrom from; // 登录方式
    private Sex sex; // 性别
    private String motto; // 个性签名
    private int location; // 所在地（索引）
    private int job; // 职业身份（索引）
    private String email; // 电子邮箱
    private String phone; // 手机号
    private int birthYear; // 出生年
    private int birthMonth; // 出生月份
    private int birthDay; // 出生日
    private int likes; // 点赞数
    private boolean[] hobby; // 数组内某个元素为true则表示拥有相应的兴趣爱好

    public Profile() {
        this.password = "123456";
        this.nickname = "User";
        this.sex = Sex.FEMALE;
        this.location = -1;
        this.job = -1;
        this.birthDay = 1;
        this.birthMonth = 1;
        this.birthYear = 1970;
        this.motto = "";
        this.email = "";
        this.phone = "";
        this.likes  = 233;
        this.hobby = new boolean[hobbies.length];
    }

    public int getHobbyCount() {
        int count = 0;
        for (boolean b : hobby) {
            if (b)
                count++;
        }
        return count;
    }

    public void addHobby(int src) {
        hobby[src] = true;
    }

    public void deleteHobby(int src) {
        hobby[src] = false;
    }

    public void setBirthday(int year, int month, int day) {
        this.birthYear = year;
        this.birthMonth = month;
        this.birthDay = day;
    }

    public String getBirthdayString() {
        if (!hasBirthday())
            return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(birthYear, birthMonth - 1, birthDay);
        Date date = c.getTime();
        return dateFormat.format(date);
    }

    public boolean[] getHobby() {
        return hobby;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public void setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public boolean hasBirthday() {
        return birthYear > 0 && birthMonth > 0 && birthDay > 0;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getLocation() {
        return location;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LoginFrom getFrom() {
        return from;
    }

    public void setFrom(LoginFrom from) {
        this.from = from;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void copyProfile(Profile temp) {
        this.nickname = temp.nickname;
        this.sex = temp.sex;
        this.location = temp.location;
        this.job = temp.job;
        this.birthDay = temp.birthDay;
        this.birthMonth = temp.birthMonth;
        this.birthYear = temp.birthYear;
        this.motto = temp.motto;
        this.email = temp.email;
        this.phone = temp.phone;
        this.hobby = temp.hobby;
        this.hobby = new boolean[hobbies.length];
        System.arraycopy(temp.hobby, 0, this.hobby, 0, temp.hobby.length);
        this.likes  = temp.likes;
    }

    public int getAge() {
        Calendar c = Calendar.getInstance();
        int todayYear = c.get(Calendar.YEAR);
        int todayMonth = c.get(Calendar.MONTH) + 1;
        int todayDay = c.get(Calendar.DAY_OF_MONTH);
        int age = todayYear - birthYear;
        if (age < 0)
            throw new IllegalArgumentException("Illegal birthday");
        if (todayMonth <= birthMonth) {
            if (todayMonth == birthMonth) {
                if (todayDay < birthDay) age--;//当前日期在生日之前，年龄减一
            }
            else {
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }

    public int getStarSign() {
        switch (birthMonth) {
            case 1:
                if (birthDay >= 1 && birthDay <=19)
                    return 0;
                else
                    return 1;
            case 2:
                if (birthDay >= 1 && birthDay <=18)
                    return 1;
                else
                    return 2;
            case 3:
                if (birthDay >= 1 && birthDay <=20)
                    return 2;
                else
                    return 3;
            case 4:
                if (birthDay >= 1 && birthDay <=19)
                    return 3;
                else
                    return 4;
            case 5:
                if (birthDay >= 1 && birthDay <=20)
                    return 4;
                else
                    return 5;
            case 6:
                if (birthDay >= 1 && birthDay <=21)
                    return 5;
                else
                    return 6;
            case 7:
                if (birthDay >= 1 && birthDay <=22)
                    return 6;
                else
                    return 7;
            case 8:
                if (birthDay >= 1 && birthDay <=22)
                    return 7;
                else
                    return 8;
            case 9:
                if (birthDay >= 1 && birthDay <=22)
                    return 8;
                else
                    return 9;
            case 10:
                if (birthDay >= 1 && birthDay <=23)
                    return 9;
                else
                    return 10;
            case 11:
                if (birthDay >= 1 && birthDay <=22)
                    return 10;
                else
                    return 11;
            case 12:
                if (birthDay >= 1 && birthDay <=21)
                    return 11;
                else
                    return 0;
            default:
                return -1;
        }
    }

    public int getZodiac() {
        return birthYear % 12;//找一个很小的年份作为基准年，公元0年是猴年
    }

    public String genIntroduction() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("我是%s，是", nickname));
        if (location != -1) {
            s.append(String.format("来自%s的", provinces[location]));
        }
        if (hasBirthday()) {
            s.append(star_sign[getStarSign()]);
        }
        if (sex == Sex.FEMALE) {
            s.append("小姐姐一枚。");
        }
        else if (sex == Sex.MALE) {
            s.append("小哥哥一枚。");
        }
        if (job != -1) {
            s.append("目前");
            switch (job) {
                case 0:
                    s.append("在学校学习。");
                    break;
                case 1:
                    s.append("从事政治工作。");
                    break;
                case 2:
                    s.append("是自己擅长领域的大佬。");
                    break;
                case 3:
                    s.append("是一个普通上班族。");
                    break;
                case 4:
                    if (sex == Sex.FEMALE) {
                        s.append("是一个程序媛。");
                    }
                    else if (sex == Sex.MALE) {
                        s.append("是一个程序猿。");
                    }
                    break;
                case 5:
                    s.append("是一个大老板。");
                    break;
                case 6:
                    s.append("正在搬砖中。");
                    break;
                case 7:
                    s.append("宅在家。");
                    break;
                case 8:
                    s.append("是不方便透露具体身份的神秘人。");
                    break;
                default:
                    break;
            }
        }
        int hobbyCount;
        if ((hobbyCount = getHobbyCount()) > 0) {
            if (hobbyCount == 1) {
                String str = "";
                for (int i = 0; i < hobbies.length; i++) {
                    if (hobby[i]) {
                        str = hobbies[i];
                        break;
                    }
                }
                s.append(String.format("唯%s不可辜负。", str));
            }
            else {
                s.append("我的兴趣爱好有：");
                int count = 0;
                for (int i = 0; i < hobbies.length; i++) {
                    if (hobby[i]) {
                        s.append(hobbies[i]);
                        count++;
                        if (count == hobbyCount) {
                            s.append("。");
                        }
                        else {
                            s.append("、");
                        }
                    }
                }
            }
        }
        return s.toString();
    }
}
