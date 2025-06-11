package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Word.Three_icon;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.Dialog;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.Three.OneAdapter;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.FinishActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Word.WordFragment;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.network.Home.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TowActivity extends AppCompatActivity {
    private OneAdapter oneAdapter;
    private RecyclerView recyclerView;
    private Button button_up;
    private TextView wordSum, word, bookname;
    private ImageView back;
    private int num = 0;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_and_tow);
        bookname = findViewById(R.id.book_name);
        button_up = findViewById(R.id.button_word_up);
        word = findViewById(R.id.sound_word);
        back = findViewById(R.id.back_word);
        wordSum = findViewById(R.id.num_word);
        //返回键
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //书名
        bookname.setText(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VOAIDNAME);

        //绑定布局
        recyclerView = findViewById(R.id.recycle_yin);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }
        Choice(Constant.WORDBEAN.get(num));
        //为“下一题”设置点击
        button_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button_up.getText().equals("下一题")) {
                    num = num + 1;
                    Choice(Constant.WORDBEAN.get(num));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TowActivity.this);
                    builder.setMessage("你一共答对了"+
                                    String.valueOf(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.RIGHT)+"道题")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // 点击确认后执行的操作
                                    finish();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

// 将按钮居中
                    Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                    params.gravity = Gravity.CENTER;
                    positiveButton.setLayoutParams(params);
                }
            }
        });
    }

    private void Choice(WordBean.DataBean.WordsBean wordBean) {
        Set<String> set = new HashSet<>();
        set.add(wordBean.getPhonetic());
        int length = com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.WORDALL.size();
        while (true) {
            Random random = new Random();
            int i = random.nextInt(length - 1);
            if( com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.WORDALL.get(i).getPhonetic()!=null && !com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.WORDALL.get(i).getPhonetic().equals(wordBean.getPhonetic())){
                set.add(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.WORDALL.get(i).getPhonetic());
            }
            if (set.size() == 4) {
                break;
            }
        }
        List<String> list = new ArrayList<>();
        for (String a : set) {
            list.add(a);
        }
        //打乱顺序
        Collections.shuffle(list);
        int which = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(wordBean.getPhonetic())) {
                which = i;
                break;
            }
        }
        oneAdapter = new OneAdapter(this, list, wordBean, which);
        oneAdapter.setCallBack(new OneAdapter.CallBack() {
            @Override
            public void clickItem(int position) throws IOException {
                oneAdapter.setIschoose(position);
                oneAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(oneAdapter);
        oneAdapter.notifyDataSetChanged();
        wordSum.setText(String.valueOf(num + 1) + "/" + Constant.WORDBEAN.size());
        word.setText(wordBean.getWord());
        if (num == Constant.WORDBEAN.size() - 1) {
            button_up.setText("全部完成");
        }
    }


    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                android.widget.Toast.makeText(TowActivity.this, str, android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在这里执行一些清理操作
    }
}
