package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home;

import static java.lang.Math.max;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.BookIconAdapter;
import com.cn.ailanguage.primarychinese.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FightIconActivity extends AppCompatActivity {

    private ImageView back_icon;
    TextView poemsen;
    TextView b11;
    TextView b12;
    TextView b13;
    TextView b14;
    TextView b21;
    TextView b22;
    TextView b23;
    TextView b24;
    TextView b31;
    TextView b32;
    TextView b33;
    TextView b34;
    TextView b41;
    TextView b42;
    TextView b43;
    TextView b44;
    TextView hint;

    String sentence;
    int lightGreen = Color.argb(200, 0, 255, 0); // 透明度为 150 的浅绿色
    int lightRed = Color.argb(200, 255, 0, 0); // 透明度为 200 的浅红色

    boolean haveMatched_1 = false;
    boolean haveMatched_2 = false;
    boolean haveMatched_3 = false;


    int cnt1;
    int cnt2;

    int cnt3;
    private LinearLayout row1;
    private LinearLayout row2;
    private int[] a = new int[1010];
    boolean[] st = new boolean[1010];
    int[] pos = {0}; // 使用数组以便在内部类中修改
    int[] head = {0, 0, 0};

    boolean canClick = true;
    boolean canhintClick = true;

    private LinearLayout row1_;
    private LinearLayout row2_;

    private static final String PREF_NAME = "hint_clicks";
    private static final String KEY_CLICK_COUNT = "click_count";

    private int clickCount = 0;

    private SharedPreferences sharedPreferences;

    boolean isFirst = true;

    boolean haveFinish1 = false;
    boolean haveFinish2 = false;
    boolean haveFinish3 = false;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight_icon);
        back_icon = findViewById(R.id.back_icon);

        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }

        // 初始化 SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // 从 SharedPreferences 中获取点击次数
        clickCount = sharedPreferences.getInt(KEY_CLICK_COUNT, 0);

        hint = findViewById(R.id.hint);
//        clickCount = 3;
        saveClickCount();
        updateHintText(); // 更新文本内容
        poemsen = findViewById(R.id.poemsen);

        //点击返回事件
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭界面跳转
                finish();
            }
        });

        cnt3 = 0;

        selectPoem();
        clickPoem();


    }


    // 点击诗句 展示
    private void onClickPoem(TextView v, int tag)
    {


        TextView[] textViews = new TextView[]{
                b11, b12, b13, b14,
                b21, b22, b23, b24,
                b31, b32, b33, b34,
                b41, b42, b43, b44
        };

        v.setBackgroundResource(R.drawable.custom_background_brown); // 文本不一致，设置自定义背景

        poemsen.setText(poemsen.getText().toString() + v.getText().toString());

        pos[0] ++ ;


        // 1 匹配第一句
        // 2 匹配第二句（两句）
        // 3 匹配第二句（三句） 上面有两句
        // 4 匹配第二句（三句） 上面有一句
        // 5 匹配第三句（三句） 上面有两句
        // 6 匹配第三句（三句） 上面有一句

        if (tag == 1 && pos[0] == cnt1)
        {

                // 将诗句写进框中
                for (int i = 0; i < cnt1; i ++ )
                {
                    TextView textView = (TextView) row1.getChildAt(i);
                    char character = sentence.charAt(i);

                    textView.setText(String.valueOf(character));
                }

                // 变绿
                for (TextView textView : textViews) {
                    if (isBrown(textView))
                    {
                        textView.setBackgroundResource(R.drawable.custom_background_green); // 文本不一致，设置自定义背景
                    }
                }

                // 消失
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        for (TextView textView : textViews) {
                            if (isGreen(textView))
                            {
                                textView.setTag(true);
                                textView.setVisibility(View.INVISIBLE);
                            }

                        }
                        poemsen.setText("");
                    }
                }, 1000);

            pos[0] = 0;
            isFirst = true;
            haveMatched_1 = false;
            haveMatched_2 = false;
            haveMatched_3 = false;
            haveFinish1 = true;
        }
        else if (tag == 2 && pos[0] == cnt2)
        {
                // 将诗句写进框中
                for (int i = 0; i < cnt2; i ++ )
                {
                    TextView textView = (TextView) row2.getChildAt(i);
                    char character = sentence.charAt(i + cnt1 + 1);

                    textView.setText(String.valueOf(character));
                }

                // 变绿
                for (TextView textView : textViews) {
                    if (isBrown(textView))
                    {
                        textView.setBackgroundResource(R.drawable.custom_background_green); // 文本不一致，设置自定义背景
                    }
                }

                // 消失
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        for (TextView textView : textViews) {
                            if (isGreen(textView))
                            {
                                textView.setTag(true);
                                textView.setVisibility(View.INVISIBLE);
                            }

                        }
                        poemsen.setText("");
                    }
                }, 1000);

            pos[0] = 0;
            isFirst = true;
            haveMatched_1 = false;
            haveMatched_2 = false;
            haveMatched_3 = false;
            haveFinish2 = true;

        }
        else if (tag == 3 && pos[0] == cnt2)
        {
            // 将诗句写进框中
            for (int i = 0; i < cnt2; i ++ )
            {
                TextView textView = (TextView) row1_.getChildAt(i);
                char character = sentence.charAt(i + cnt1 + 1);

                textView.setText(String.valueOf(character));
            }

            // 变绿
            for (TextView textView : textViews) {
                if (isBrown(textView))
                {
                    textView.setBackgroundResource(R.drawable.custom_background_green); // 文本不一致，设置自定义背景
                }
            }

            // 消失
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    for (TextView textView : textViews) {
                        if (isGreen(textView))
                        {
                            textView.setTag(true);
                            textView.setVisibility(View.INVISIBLE);
                        }

                    }
                    poemsen.setText("");
                }
            }, 1000);

            pos[0] = 0;
            isFirst = true;
            haveMatched_1 = false;
            haveMatched_2 = false;
            haveMatched_3 = false;
            haveFinish2 = true;

        }
        else if (tag == 4 && pos[0] == cnt2)
        {
            // 将诗句写进框中
            for (int i = 0; i < cnt2; i ++ )
            {
                TextView textView = (TextView) row2.getChildAt(i);
                char character = sentence.charAt(i + cnt1 + 1);

                textView.setText(String.valueOf(character));
            }

            // 变绿
            for (TextView textView : textViews) {
                if (isBrown(textView))
                {
                    textView.setBackgroundResource(R.drawable.custom_background_green); // 文本不一致，设置自定义背景
                }
            }

            // 消失
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    for (TextView textView : textViews) {
                        if (isGreen(textView))
                        {
                            textView.setTag(true);
                            textView.setVisibility(View.INVISIBLE);
                        }

                    }
                    poemsen.setText("");
                }
            }, 1000);

            pos[0] = 0;
            isFirst = true;
            haveMatched_1 = false;
            haveMatched_2 = false;
            haveMatched_3 = false;
            haveFinish2 = true;
        }
        else if (tag == 5 && pos[0] == cnt3)
        {
            // 将诗句写进框中
            for (int i = 0; i < cnt3; i ++ )
            {
                TextView textView = (TextView) row2.getChildAt(i);
                char character = sentence.charAt(i + cnt1 + cnt2 + 2);

                textView.setText(String.valueOf(character));
            }

            // 变绿
            for (TextView textView : textViews) {
                if (isBrown(textView))
                {
                    textView.setBackgroundResource(R.drawable.custom_background_green); // 文本不一致，设置自定义背景
                }
            }

            // 消失
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    for (TextView textView : textViews) {
                        if (isGreen(textView))
                        {
                            textView.setTag(true);
                            textView.setVisibility(View.INVISIBLE);
                        }

                    }
                    poemsen.setText("");
                }
            }, 1000);

            pos[0] = 0;
            isFirst = true;
            haveMatched_1 = false;
            haveMatched_2 = false;
            haveMatched_3 = false;
            haveFinish3 = true;
        }
        else if (tag == 6 && pos[0] == cnt3)
        {
            // 将诗句写进框中
            for (int i = 0; i < cnt3; i ++ )
            {
                TextView textView = (TextView) row2_.getChildAt(i);
                char character = sentence.charAt(i + cnt1 + cnt2 + 2);

                textView.setText(String.valueOf(character));
            }

            // 变绿
            for (TextView textView : textViews) {
                if (isBrown(textView))
                {
                    textView.setBackgroundResource(R.drawable.custom_background_green); // 文本不一致，设置自定义背景
                }
            }

            // 消失
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    for (TextView textView : textViews) {
                        if (isGreen(textView))
                        {
                            textView.setTag(true);
                            textView.setVisibility(View.INVISIBLE);
                        }

                    }
                    poemsen.setText("");
                }
            }, 1000);

            pos[0] = 0;
            isFirst = true;
            haveMatched_1 = false;
            haveMatched_2 = false;
            haveMatched_3 = false;
            haveFinish3 = true;
        }

        // 完成以后初始化
        if (haveFinish1 && haveFinish2 && (cnt3 == 0 || haveFinish3))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    initPoem();
                }
            }, 2500);
        }

    }
    private void clickPoem() {

        row1 = findViewById(R.id.row1);
        row2 = findViewById(R.id.row2);
        hint = findViewById(R.id.hint);
        TextView[] textViews = new TextView[]{
                b11, b12, b13, b14,
                b21, b22, b23, b24,
                b31, b32, b33, b34,
                b41, b42, b43, b44
        };

        for (TextView textView : textViews)
        {
            textView.setTag(false);
        }



        // 存储原始诗词文本
        String originalPoemText = sentence.replaceAll("[\\p{Punct}]", "");

        Log.e("qxy", "or: " + originalPoemText);


        // 设置点击事件
        for (TextView textView : textViews) {

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!canClick) return ;

                    if (v.getTag().equals(true)) return;

                    Log.e("qxy", "getTag: " + v.getTag() );

                    v.setTag(true); // 保证点击了 就不会再提示
                    // 获取当前点击的文本
                    String clickedText = ((TextView) v).getText().toString();

                    // 检查当前位置是否在原始诗词文本长度范围内

                    boolean isMatched_1 = false;
                    boolean isMatched_2 = false;
                    boolean isMatched_3 = false;
                    if (pos[0] < originalPoemText.length())
                    {
                        isMatched_1 = originalPoemText.charAt(pos[0]) == clickedText.charAt(0);
                    }
                    if (pos[0] + cnt1 < originalPoemText.length())
                    {
                        isMatched_2 = originalPoemText.charAt(pos[0] + cnt1) == clickedText.charAt(0);
                    }
                    if (cnt3 != 0 && pos[0] + cnt1 + cnt2 < originalPoemText.length())
                    {
                        isMatched_3 = originalPoemText.charAt(pos[0] + cnt1 + cnt2) == clickedText.charAt(0);
                    }

                    Log.e("qxy", "ceshi: match " + isMatched_1 + " " + isMatched_2 + " " + isMatched_3 + ":  " + isFirst + " " + haveFinish2);


                        // 1 匹配第一句
                        // 2 匹配第二句（两句）
                        // 3 匹配第二句（三句） 上面有两句
                        // 4 匹配第二句（三句） 上面有一句
                        // 5 匹配第三句（三句） 上面有两句
                        // 6 匹配第三句（三句） 上面有一句

                        // 1 匹配第一句
                        if (!haveFinish1 && isMatched_1 && (haveMatched_1 || isFirst))
                        {
                            Log.e("qxy", "ceshi: 1");
                            haveMatched_1 = true;
                            isFirst = false;
                            onClickPoem((TextView) v, 1);
                        }
                        // 2 匹配第二句（两句）
                        else if (cnt3 == 0 &&!haveFinish2 && isMatched_2 && (haveMatched_2 || isFirst))
                        {
                            Log.e("qxy", "ceshi: 2");
                            haveMatched_2 = true;
                            isFirst = false;
                            onClickPoem((TextView) v, 2);
                        }
                        else if (cnt3 != 0 && !haveFinish2 && isMatched_2 && (haveMatched_2 || isFirst))
                        {
                            isFirst = false;
                            // 3 匹配第二句（三句） 上面有两句
                            if (cnt1 + cnt2 <= 8)
                            {
                                haveMatched_2 = true;
                                onClickPoem((TextView) v, 3);
                            }
                            // 4 匹配第二句（三句） 上面有一句
                            else
                            {
                                haveMatched_2 = true;
                                onClickPoem((TextView) v, 4);
                            }


                        }

                        else if (cnt3 != 0 && !haveFinish3 && isMatched_3 && (haveMatched_3 || isFirst))
                        {
                            isFirst = false;
                            // 5 匹配第三句（三句） 上面有两句
                            if (cnt1 + cnt2 <= 8)
                            {
                                haveMatched_3 = true;
                                onClickPoem((TextView) v, 5);
                            }
                            // 6 匹配第三句（三句） 上面有一句
                            else
                            {
                                haveMatched_3 = true;
                                onClickPoem((TextView) v, 6);
                            }

                        }
                        else
                        {
                            Log.e("qxy", "ceshi: 3");

                            canClick = false;
                            v.setBackgroundResource(R.drawable.custom_background_red);

                            // 变红
                            for (TextView textView : textViews) {

                                if (isBrown(textView))
                                {
                                    textView.setBackgroundResource(R.drawable.custom_background_red);
                                }
                            }

                            // 恢复
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    canClick = true;
                                    for (TextView textView : textViews) {
                                        textView.setTag(false);
                                        textView.setBackgroundResource(R.drawable.custom_background);
                                    }

                                    pos[0] = 0;
                                    isFirst = true;
                                    haveMatched_1 = false;
                                    haveMatched_2 = false;
                                    haveMatched_3 = false;

                                    poemsen.setText("");
                                }
                            }, 1000);
                        }

                        // 更新位置
                    }

            });
        }

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClick) return ;
                if (!canhintClick) return ;
                TextView[] textViews = new TextView[]{
                        b11, b12, b13, b14,
                        b21, b22, b23, b24,
                        b31, b32, b33, b34,
                        b41, b42, b43, b44
                };


                canClick = false;


                // 检查点击次数是否已经用完
                if (clickCount > 0) {
                    // 点击次数未用完时执行提示逻辑
                    Toast.makeText(FightIconActivity.this, "提示", Toast.LENGTH_SHORT).show();
                    clickCount--; // 点击次数减一
                    saveClickCount(); // 保存点击次数
                    updateHintText(); // 更新文本内容
                    displayCorrect();

                } else {
                    // 点击次数已用完时，禁用按钮并提示用户
                    Toast.makeText(FightIconActivity.this, "每天只能点击三次！", Toast.LENGTH_SHORT).show();
                    canClick = true;
                    canhintClick = false;
                }






            }
        });
    }


    // 保存点击次数到 SharedPreferences
    private void saveClickCount() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_CLICK_COUNT, clickCount);
        editor.apply();
    }

    // 更新提示文本内容
    private void updateHintText() {
        hint.setText("提示 " + clickCount);
    }

    // 在每天重置点击次数
    private void resetClickCount() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_CLICK_COUNT, 3); // 重置为初始值 3
        editor.apply();
        clickCount = 3; // 重置内存中的点击次数
        canClick = true; // 重新启用点击
        updateHintText(); // 更新文本内容
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 检查是否是新的一天，如果是，则重置点击次数
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_YEAR);
        int lastDay = sharedPreferences.getInt("last_day", -1);
        if (today != lastDay) {
            resetClickCount();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("last_day", today);
            editor.apply();
        }
    }


    private void displayCorrect() {
        TextView[] textViews = new TextView[]{
                b11, b12, b13, b14,
                b21, b22, b23, b24,
                b31, b32, b33, b34,
                b41, b42, b43, b44
        };

        String originalPoemText = sentence.replaceAll("[\\p{Punct}]", "");


        if (!haveFinish1 && (haveMatched_1 || (!haveMatched_2 && !haveMatched_3)) )
        {

            // 变绿
            for (int i = pos[0]; i < cnt1; i ++ )
            {
                for (TextView textView : textViews) {
                    Boolean tag = (Boolean) textView.getTag();
                    if (!tag && String.valueOf(originalPoemText.charAt(i)).equals(textView.getText().toString())) {
                        textView.setTag(true);
                        final TextView tempTextView = textView; // 创建一个副本以便在延迟执行时引用

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tempTextView.setBackgroundResource(R.drawable.custom_background_green);
                            }
                        }, 500 *(i - pos[0] + 1));
                        break;
                    }
                }
            }

        }
        else if (!haveFinish2 && (haveMatched_2 || (!haveMatched_1 && !haveMatched_3)) )
        {

            // 变绿
            for (int i = pos[0]; i < cnt2; i ++ )
            {
                for (TextView textView : textViews) {
                    Boolean tag = (Boolean) textView.getTag();
                    if (!tag && String.valueOf(originalPoemText.charAt(cnt1 + i)).equals(textView.getText().toString())) {
                        textView.setTag(true);
                        final TextView tempTextView = textView; // 创建一个副本以便在延迟执行时引用

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tempTextView.setBackgroundResource(R.drawable.custom_background_green);
                            }
                        }, 500 *(i - pos[0] + 1));
                        break;
                    }
                }
            }


        }
        else if (cnt3 != 0 && !haveFinish3 && (haveMatched_3 || (!haveMatched_1 && !haveMatched_2)) )
        {
            // 变绿
            for (int i = pos[0]; i < cnt3; i ++ )
            {
                for (TextView textView : textViews) {
                    Boolean tag = (Boolean) textView.getTag();
                    if (!tag && String.valueOf(originalPoemText.charAt(cnt1 + cnt2 + i)).equals(textView.getText().toString())) {
                        textView.setTag(true);
                        final TextView tempTextView = textView; // 创建一个副本以便在延迟执行时引用

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tempTextView.setBackgroundResource(R.drawable.custom_background_green);
                            }
                        }, 500 *(i - pos[0] + 1));
                        break;
                    }
                }
            }
        }

        // 变回
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                for (TextView textView : textViews)
                {
                    if (isGreen(textView))
                    {
                        textView.setBackgroundResource(R.drawable.custom_background); // 文本不一致，设置自定义背景
                        textView.setTag(false);
                    }
                    canClick = true;

                }

            }
        }, 500*(max(cnt1, max(cnt2, cnt3)) - pos[0] + 2));

    }

    private boolean isGreen(TextView textView) {
// 获取textView当前的背景资源ID
        int backgroundResource = textView.getBackground().getConstantState().hashCode();

// 获取自定义背景drawable的资源ID
        int customBackgroundResource = getResources().getDrawable(R.drawable.custom_background_green).getConstantState().hashCode();

// 判断两个资源ID是否相同
        if (backgroundResource == customBackgroundResource) {
            return true;
            // textView的背景是自定义背景
            // 在这里执行你的逻辑
        } else {
            return false;
            // textView的背景不是自定义背景
            // 在这里执行你的逻辑
        }

    }

    private boolean isBrown(TextView textView) {
// 获取textView当前的背景资源ID
        int backgroundResource = textView.getBackground().getConstantState().hashCode();

// 获取自定义背景drawable的资源ID
        int customBackgroundResource = getResources().getDrawable(R.drawable.custom_background_brown).getConstantState().hashCode();

// 判断两个资源ID是否相同
        if (backgroundResource == customBackgroundResource) {
            return true;
            // textView的背景是自定义背景
            // 在这里执行你的逻辑
        } else {
            return false;
            // textView的背景不是自定义背景
            // 在这里执行你的逻辑
        }

    }

    void initPoem(){
        pos[0] = 0;
        isFirst = true;
        haveFinish1 = false;
        haveFinish2 = false;
        haveFinish3 = false;
        row1 = findViewById(R.id.row1);
        row2 = findViewById(R.id.row2);
        TextView[] textViews = new TextView[]{
                b11, b12, b13, b14,
                b21, b22, b23, b24,
                b31, b32, b33, b34,
                b41, b42, b43, b44
        };

        for (int i = 0; i < row1.getChildCount(); i++) {

            TextView textView = (TextView) row1.getChildAt(i);
            if (i <= cnt1) {
                textView.setVisibility(View.VISIBLE);
                textView.setText("");//
            } else {
                textView.setVisibility(View.GONE);
            }
        }

        if (cnt3 == 0)
        {
            for (int i = 0; i < row2.getChildCount(); i++) {
                TextView textView = (TextView) row2.getChildAt(i);
                if (i <= cnt2) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("");
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
        }
        else
        {
            if (cnt1 + cnt2 <= 8 && cnt3 != 0)
            {
                for (int i = 0; i < row1_.getChildCount(); i++) {
                    TextView textView = (TextView) row1_.getChildAt(i);
                    if (i <= cnt2) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("");
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                }

                for (int i = 0; i < row2.getChildCount(); i++) {
                    TextView textView = (TextView) row2.getChildAt(i);
                    if (i <= cnt3) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("");
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                }
            }
            else
            {
                for (int i = 0; i < row2.getChildCount(); i++) {
                    TextView textView = (TextView) row2.getChildAt(i);
                    if (i <= cnt2) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("");
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                }

                for (int i = 0; i < row2_.getChildCount(); i++) {
                    TextView textView = (TextView) row2_.getChildAt(i);
                    if (i <= cnt3) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("");
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                }
            }
        }





        for (TextView textView : textViews) {
            textView.setBackgroundResource(R.drawable.custom_background);
            textView.setText("");

        }
        canClick = true;
        selectPoem();
        clickPoem();

    }

    private void copyDatabase() {
        try {

            InputStream inputStream = getAssets().open("poem.db");
            String outFileName = getDatabasePath("poem.db").getPath();
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断有没有数据库
    private boolean isDatabaseExists(String dbName) {
        File dbFile = getApplicationContext().getDatabasePath(dbName);
        return dbFile.exists();
    }
    private void selectPoem() {
        if (!isDatabaseExists("poem.db")) {
            copyDatabase();
        }
        // 打开数据库连接
        SQLiteDatabase db = SQLiteDatabase.openDatabase(getDatabasePath("poem.db").getPath(), null, SQLiteDatabase.OPEN_READWRITE);
// 执行查询操作
        String query = "SELECT * FROM sentence_table1 ORDER BY RANDOM() LIMIT 1";
//        String query = "SELECT * FROM sentence_table1";
        Cursor cursor = db.rawQuery(query, null);
        int columnCount = cursor.getColumnCount();

        List<List<String>> columnDataLists = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            columnDataLists.add(new ArrayList<>());
        }
        while (cursor.moveToNext()) {
            for (int i = 0; i < columnCount; i++) {
                String columnValue = cursor.getString(i);
                columnDataLists.get(i).add(columnValue);
            }
        }

        sentence = columnDataLists.get(1).get(0);
        cursor.close();
        db.close();

        Log.e("qxy", "sen: " + sentence );
//        sentence = "游蕲水清泉寺，寺临兰溪，溪水西流。";
//        sentence = "天苍苍，野茫茫，风吹草低见牛羊。";
//        sentence = "敕勒川，阴山下。";
//        sentence = "两个黄鹂鸣翠柳，一行白鹭上青天。";

        displayPoem(sentence);

    }


    void displayPoem_(String randomPoemText)
    {
        char[] characters = randomPoemText.toCharArray();
        List<Character> charList = new ArrayList<>();


        cnt1 = 0; // 总字数
        int punCnt = 0;
        cnt2 = 0;
        cnt3 = 0;
        for (char c : characters) {
            if (punCnt == 0 && isChineseCharacter(c) && !isPunctuation(c)) {
                charList.add(c);
                cnt1 ++ ;
            }
            else if (punCnt == 1 && isChineseCharacter(c) && !isPunctuation(c)) {
                charList.add(c);//  比如
                cnt2++;

            }
            else if (punCnt == 2 && isChineseCharacter(c) && !isPunctuation(c)) {
                charList.add(c);
                cnt3++;

            }
            else
            {
                punCnt ++ ;
            }
        }

        if (cnt3 != 0 && cnt1 + cnt2 > 8)
        {
            displaySplit(false, charList);
        }
        else
        {
            displaySplit(true, charList);
        }

        Log.e("qxy", "punCnt: " + " " + cnt1 + " " + cnt2 + " " + cnt3);

    }
// 是叫动态规划 动态更新 减少了时间复杂度 属于最最简单的上升序列动态规划  哪个研究生面试会让你手撕算法题 还听你逼逼赖赖老半天

    void displaySplit(boolean isHead, List<Character> charList)
    {

        List<TextView> textViews = new ArrayList<>();
        textViews.add(b11);
        textViews.add(b12);
        textViews.add(b13);
        textViews.add(b14);
        textViews.add(b21);
        textViews.add(b22);
        textViews.add(b23);
        textViews.add(b24);
        textViews.add(b31);
        textViews.add(b32);
        textViews.add(b33);
        textViews.add(b34);
        textViews.add(b41);
        textViews.add(b42);
        textViews.add(b43);
        textViews.add(b44);

        // 随机打乱 TextView 的顺序
//        Collections.shuffle(textViews);

        fillPoem(charList);

        for (int i = 0; i < charList.size(); i ++ )
        {
            TextView textView = textViews.get(a[i]);
            char character = charList.get(i);
            textView.setText(String.valueOf(character));
            textView.setVisibility(View.VISIBLE);
        }

        // 设置文本和可见性
        for (int i = 0; i < textViews.size(); i++) {

            if (!st[i])
            {
                textViews.get(i).setVisibility(View.INVISIBLE);
            }
        }




        if (isHead)
        {

            row1 = findViewById(R.id.row1);
            row1_ = findViewById(R.id.row1_);
            row2 = findViewById(R.id.row2);
            row1_.setVisibility(View.VISIBLE);
// Set visibility of TextViews in row1
            for (int i = 0; i < row1.getChildCount(); i++) {
                TextView textView = (TextView) row1.getChildAt(i);
                if (i < cnt1) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < row1_.getChildCount(); i++) {
                TextView textView = (TextView) row1_.getChildAt(i);
                if (i < cnt2) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
// Set visibility of TextViews in row2
            for (int i = 0; i < row2.getChildCount(); i++) {
                TextView textView = (TextView) row2.getChildAt(i);
                if (i < cnt3) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
        }
        else {
            row1 = findViewById(R.id.row1);
            row2 = findViewById(R.id.row2);
            row2_ = findViewById(R.id.row2_);
            row2_.setVisibility(View.VISIBLE);

// Set visibility of TextViews in row1
            for (int i = 0; i < row1.getChildCount(); i++) {
                TextView textView = (TextView) row1.getChildAt(i);
                if (i < cnt1) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < row2.getChildCount(); i++) {
                TextView textView = (TextView) row2.getChildAt(i);
                if (i < cnt2) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
// Set visibility of TextViews in row2
            for (int i = 0; i < row2_.getChildCount(); i++) {
                TextView textView = (TextView) row2_.getChildAt(i);
                if (i < cnt3) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void displayPoem(String randomPoemText) {

        b11 = findViewById(R.id.b11);
        b12 = findViewById(R.id.b12);
        b13 = findViewById(R.id.b13);
        b14 = findViewById(R.id.b14);
        b21 = findViewById(R.id.b21);
        b22 = findViewById(R.id.b22);
        b23 = findViewById(R.id.b23);
        b24 = findViewById(R.id.b24);
        b31 = findViewById(R.id.b31);
        b32 = findViewById(R.id.b32);
        b33 = findViewById(R.id.b33);
        b34 = findViewById(R.id.b34);
        b41 = findViewById(R.id.b41);
        b42 = findViewById(R.id.b42);
        b43 = findViewById(R.id.b43);
        b44 = findViewById(R.id.b44);

        hint = findViewById(R.id.hint);




        char[] characters = randomPoemText.toCharArray();
        List<Character> charList = new ArrayList<>();



        int punCnt = 0;
        for (char c : characters)
        {
            if (isPunctuation(c))
            {
                punCnt ++ ;
            }
        }


        Log.e("qxy", "punCnt: " +  punCnt);

        if (punCnt == 3)
        {
            displayPoem_(randomPoemText);
            return ;
        }

        cnt1 = 0;
        cnt2 = 0;
        for (char c : characters) {
            if (isChineseCharacter(c) && !isPunctuation(c)) {
                charList.add(c);
                cnt2 ++ ;
            }
            else if (cnt1 == 0)
            {
                cnt1 = cnt2;
            }
        }

        cnt2 = cnt2 - cnt1;

        Log.e("qxy", "cnt" + cnt1 + " " + cnt2);

        List<TextView> textViews = new ArrayList<>();
        textViews.add(b11);
        textViews.add(b12);
        textViews.add(b13);
        textViews.add(b14);
        textViews.add(b21);
        textViews.add(b22);
        textViews.add(b23);
        textViews.add(b24);
        textViews.add(b31);
        textViews.add(b32);
        textViews.add(b33);
        textViews.add(b34);
        textViews.add(b41);
        textViews.add(b42);
        textViews.add(b43);
        textViews.add(b44);

        // 随机打乱 TextView 的顺序
//        Collections.shuffle(textViews);

        fillPoem(charList);

        for (int i = 0; i < charList.size(); i ++ )
        {
            TextView textView = textViews.get(a[i]);
            char character = charList.get(i);
            textView.setText(String.valueOf(character));
            textView.setVisibility(View.VISIBLE);
        }

        // 设置文本和可见性
        for (int i = 0; i < textViews.size(); i++) {

            if (!st[i])
            {
                textViews.get(i).setVisibility(View.INVISIBLE);
            }
        }
////
//        // 隐藏多余的 TextView
//        for (int i = 0; i < textViews.size(); i++) {
//
//            if (a[i] == -1)
//        }

        row1 = findViewById(R.id.row1);
        row2 = findViewById(R.id.row2);

// Set visibility of TextViews in row1
        for (int i = 0; i < row1.getChildCount(); i++) {
            TextView textView = (TextView) row1.getChildAt(i);
            if (i < cnt1) {
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }
        }

// Set visibility of TextViews in row2
        for (int i = 0; i < row2.getChildCount(); i++) {
            TextView textView = (TextView) row2.getChildAt(i);
            if (i < cnt2) {
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    }


    private void fillPoem(List<Character> charList)
    {
        // 定义上下左右和对角线方向的偏移量
        int[] dx = {0, 0, -1, 1, -1, -1, 1, 1};
        int[] dy = {-1, 1, 0, 0, -1, 1, -1, 1};
        Random random = new Random();
        int cur;
        int pos;
        int n = charList.size();
        int m = 0;
        while (true)
        {
            Arrays.fill(a, -1);
            Arrays.fill(st, false);
            // a[第几个字母] = 位置 st[位置] = true

            cur = 0;
            m = 0;

            // 确定第一个位置
            pos = random.nextInt(n);
            while (st[pos]) pos = random.nextInt(n);
            a[cur] = pos;
            st[pos] = true;
            cur ++ ;

            // 确定后面的位置
            while (cur < n && m <= 100)
            {
                m = 0;
                while (true)
                {
                    int k = random.nextInt(8);  // 随机选择一个方向
                    int ni = a[cur - 1] / 4 + dx[k];
                    int nj = a[cur - 1] % 4 + dy[k];
                    if (m ++ > 100) break;
                    // 检查下一个位置是否越界，或者是否已经放置了字符
                    if (ni >= 0 && ni < 4 && nj >= 0 && nj < 4 && !st[ni * 4 + nj]) {
                        // 将汉字字符放入下一个位置
                        a[cur] = ni * 4 + nj;
                        st[ni * 4 + nj] = true;
                        break;
                    }
                }
                cur ++ ;
            }
            if (m > 100) continue;
            if (cur == n) break;
        }

    }

    private boolean isChineseCharacter(char c) {
        return c >= '\u4e00' && c <= '\u9fa5';
    }

    private boolean isPunctuation(char c) {
        // 定义标点符号的 Unicode 范围
        return (c >= '\u3000' && c <= '\u303F') ||
                (c >= '\u2000' && c <= '\u206F') ||
                (c >= '\uFF00' && c <= '\uFFEF') ||
                (c >= '\uFF1A' && c <= '\uFF1F') ||
                (c >= '\uFF01' && c <= '\uFF0F');
    }
}
