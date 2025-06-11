package com.cn.ailanguage.primarychinese.Activity_and_Fragment.OriDetail;


import static com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.HomeFragment.home_data;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.Dialog;
import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.MainContract;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.network.Main.MainNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.InitPresenter;
import com.cn.ailanguage.primarychinese.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class OriDetailActivity extends AppCompatActivity implements MainContract.MainView{

    private boolean isTextVisible = false;
    private boolean isReadVisible = false;
    private boolean isWordVisible = false;
    private boolean isLeaderVisible = false;
    private boolean isShadowingVisible = false;
    private boolean isBeisongVisivle = false;
    ReadFragment readfragment;
    TextFragment textfragment;
    WordFragment wordfragment;
    ShadowingFragment shadowingfragment;
    BeisongFragment beisongFragment;
    MainPresenter mainPresenter;
    LeaderFragment leaderfragment;
    private ImageView imageback, more;
    private TextView title;
    LinearLayout wordLine, readLine, bookLine, shadowingLine, leaderLine,beisongLine, banner;
    private InitPresenter initPresenter;

    private View text_get, read_get, words_get, shadowing_get, leader_get,beisong_get;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        //初始化
        more = findViewById(R.id.more);
        text_get = findViewById(R.id.text_get);
        read_get = findViewById(R.id.read_get);
        title = findViewById(R.id.title);
        imageback = findViewById(R.id.back);
        words_get = findViewById(R.id.words_get);
        wordLine = findViewById(R.id.word_line);
        shadowing_get = findViewById(R.id.shadoing_get);
        leader_get = findViewById(R.id.leader_get);
        shadowingLine = findViewById(R.id.shadowing_book_line);
        leaderLine = findViewById(R.id.leader_book_line);
        bookLine = findViewById(R.id.content_book_line);
        beisongLine=findViewById(R.id.beisong_line);
        beisong_get=findViewById(R.id.beisong_get);



        //更换标题
        if (Constant.TITLE.length() > 9) {
            title.setText(Constant.TITLE.substring(0, 9) + "...");
        } else {
            title.setText(Constant.TITLE);
        }

        //网络连接
        MainNetWorkManager.getInstance().init();
        mainPresenter = new MainPresenter();
        mainPresenter.attchView(this);
        mainPresenter.getHome("detail", Constant.VOAID);

        //只有一年级和二年级才会展示点读
        readLine = findViewById(R.id.read_line);
        SharedPreferences sharedPreferences = getSharedPreferences("bookicon", Context.MODE_PRIVATE);
        String category = sharedPreferences.getString("category", "466");
        if (!(category.equals("466") || category.equals("467") || category.equals("468") || category.equals("469"))) {
            readLine.setVisibility(View.GONE);
        }
        //只有古诗才会展示背诵功能
        if(com.cn.ailanguage.primarychinese.network.Home.Constant.POEM!=2 && com.cn.ailanguage.primarychinese.network.Home.Constant.POEM!=3){
            beisongLine.setVisibility(View.GONE);
        }else{
            beisongLine.setVisibility(View.VISIBLE);

        }
        //点击更多more按钮，展开大小和隐藏拼音功能
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        //返回键
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }
//    点击书本按钮
        bookLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("qxy", "onClick: " + Constant.flag );
                more.setVisibility(View.VISIBLE);
                isTextVisible = true;
                isReadVisible = false;
                isLeaderVisible = false;
                isWordVisible = false;
                isShadowingVisible = false;
                isBeisongVisivle=false;
                beisong_get.setBackgroundColor(getResources().getColor(R.color.main1));
                text_get.setBackgroundColor(getResources().getColor(R.color.homecolor));
                read_get.setBackgroundColor(getResources().getColor(R.color.main1));
                leader_get.setBackgroundColor(getResources().getColor(R.color.main1));
                words_get.setBackgroundColor(getResources().getColor(R.color.main1));
                shadowing_get.setBackgroundColor(getResources().getColor(R.color.main1));
                replaceFragment();

            }
        });
        //点击排行榜
        leaderLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.setVisibility(View.INVISIBLE);
                isLeaderVisible = true;
                isTextVisible = false;
                isReadVisible = false;
                isWordVisible = false;
                isShadowingVisible = false;
                isBeisongVisivle=false;
                beisong_get.setBackgroundColor(getResources().getColor(R.color.main1));
                text_get.setBackgroundColor(getResources().getColor(R.color.main1));
                read_get.setBackgroundColor(getResources().getColor(R.color.main1));
                leader_get.setBackgroundColor(getResources().getColor(R.color.homecolor));
                words_get.setBackgroundColor(getResources().getColor(R.color.main1));
                shadowing_get.setBackgroundColor(getResources().getColor(R.color.main1));

                replaceFragment();
            }
        });

//    点击点读按钮
        readLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.setVisibility(View.INVISIBLE);
                isTextVisible = false;
                isReadVisible = true;
                isLeaderVisible = false;
                isWordVisible = false;
                isShadowingVisible = false;
                isBeisongVisivle=false;
                beisong_get.setBackgroundColor(getResources().getColor(R.color.main1));
                read_get.setBackgroundColor(getResources().getColor(R.color.homecolor));
                text_get.setBackgroundColor(getResources().getColor(R.color.main1));
                leader_get.setBackgroundColor(getResources().getColor(R.color.main1));
                words_get.setBackgroundColor(getResources().getColor(R.color.main1));
                shadowing_get.setBackgroundColor(getResources().getColor(R.color.main1));
                replaceFragment();
            }
        });
//点击生词按钮
        wordLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.setVisibility(View.INVISIBLE);
                isTextVisible = false;
                isReadVisible = false;
                isLeaderVisible = false;
                isWordVisible = true;
                isShadowingVisible = false;
                isBeisongVisivle=false;
                beisong_get.setBackgroundColor(getResources().getColor(R.color.main1));
                read_get.setBackgroundColor(getResources().getColor(R.color.main1));
                text_get.setBackgroundColor(getResources().getColor(R.color.main1));
                leader_get.setBackgroundColor(getResources().getColor(R.color.main1));
                words_get.setBackgroundColor(getResources().getColor(R.color.homecolor));
                shadowing_get.setBackgroundColor(getResources().getColor(R.color.main1));
                replaceFragment();
            }
        });
        //点击跟读按钮
        shadowingLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.setVisibility(View.INVISIBLE);
                isTextVisible = false;
                isReadVisible = false;
                isLeaderVisible = false;
                isWordVisible = false;
                isShadowingVisible = true;
                isBeisongVisivle=false;
                beisong_get.setBackgroundColor(getResources().getColor(R.color.main1));
                read_get.setBackgroundColor(getResources().getColor(R.color.main1));
                text_get.setBackgroundColor(getResources().getColor(R.color.main1));
                leader_get.setBackgroundColor(getResources().getColor(R.color.main1));
                words_get.setBackgroundColor(getResources().getColor(R.color.main1));
                shadowing_get.setBackgroundColor(getResources().getColor(R.color.homecolor));
                replaceFragment();
            }
        });
//点击背诵按钮
        beisongLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more.setVisibility(View.INVISIBLE);
                isTextVisible = false;
                isReadVisible = false;
                isLeaderVisible = false;
                isWordVisible = false;
                isShadowingVisible = false;
                isBeisongVisivle=true;
                shadowing_get.setBackgroundColor(getResources().getColor(R.color.main1));
                read_get.setBackgroundColor(getResources().getColor(R.color.main1));
                text_get.setBackgroundColor(getResources().getColor(R.color.main1));
                leader_get.setBackgroundColor(getResources().getColor(R.color.main1));
                words_get.setBackgroundColor(getResources().getColor(R.color.main1));
                beisong_get.setBackgroundColor(getResources().getColor(R.color.homecolor));
                replaceFragment();
            }
        });
        bookLine.performClick();
    }


    public void replaceFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        textfragment = (TextFragment) fragmentManager.findFragmentByTag("text");
        readfragment = (ReadFragment) fragmentManager.findFragmentByTag("read");
        wordfragment = (WordFragment) fragmentManager.findFragmentByTag("words");
        shadowingfragment = (ShadowingFragment) fragmentManager.findFragmentByTag("shadowing");
        leaderfragment = (LeaderFragment) fragmentManager.findFragmentByTag("leader");
        beisongFragment=(BeisongFragment) fragmentManager.findFragmentByTag("beisong");
        //只有当模块被选择的时候才会add这个模块的fragment，加快加载速度
        if (readfragment == null && isReadVisible) {
            readfragment = ReadFragment.newInstance(null, null);
        } else{
            //如果顺序播放或者乱序播放，并且上一个界面播放完成；或者滚动自动跳转到下一个界面的时候，加一个新的fragment进去，消除上次的fragment
            if (((Constant.flag == 1 || Constant.flag == 3) && Constant.COMPETE == 1) || Constant.SCROLL==1) {
                if (readfragment!=null) {
                    transaction.remove(readfragment);
                    readfragment = ReadFragment.newInstance(null, null);
                }
            }
        }

        if (beisongFragment == null && isBeisongVisivle) {
            beisongFragment = BeisongFragment.newInstance(null, null);
        } else{
            if (((Constant.flag == 1 || Constant.flag == 3) && Constant.COMPETE == 1) || Constant.SCROLL==1) {
                if (beisongFragment!=null) {
                    transaction.remove(beisongFragment);
                    beisongFragment = BeisongFragment.newInstance(null, null);
                }
            }
        }

        if (textfragment == null && isTextVisible) {
            textfragment = TextFragment.newInstance(null, null);
        }else{
            if (((Constant.flag == 1 || Constant.flag == 3) && Constant.COMPETE == 1) || Constant.SCROLL==1) {
                if (textfragment!=null) {
                    transaction.remove(textfragment);
                    textfragment = TextFragment.newInstance(null, null);
                }

            }
        }

        if (leaderfragment == null && isLeaderVisible) {
            leaderfragment = LeaderFragment.newInstance(null, null);
        }
        if (((Constant.flag == 1 || Constant.flag == 3) && Constant.COMPETE == 1) || Constant.SCROLL==1) {

            if (leaderfragment != null) {
                transaction.remove(leaderfragment);
                leaderfragment = LeaderFragment.newInstance(null, null);
            }
            if (leaderfragment!=null && Constant.BORE == 1) {
                leaderfragment.refreshUI();
                Constant.BORE = 0;
            }
        }


        if (wordfragment == null && isWordVisible) {
            wordfragment = WordFragment.newInstance(null, null);
        }
        if (((Constant.flag == 1 || Constant.flag == 3) && Constant.COMPETE == 1) || Constant.SCROLL==1) {
            if(wordfragment!=null){
                transaction.remove(wordfragment);
                wordfragment = WordFragment.newInstance(null, null);
            }

        }
        if (shadowingfragment == null && isShadowingVisible) {
            shadowingfragment = ShadowingFragment.newInstance(null, null);
        }
        if (((Constant.flag == 1 || Constant.flag == 3) && Constant.COMPETE == 1) || Constant.SCROLL==1) {

            if(shadowingfragment!=null){
                transaction.remove(shadowingfragment);
                shadowingfragment = ShadowingFragment.newInstance(null, null);
            }

        }

        if (!textfragment.isAdded() && isTextVisible) {
            transaction.add(R.id.child_fragement, textfragment, "text");
        }
        if (isReadVisible && !readfragment.isAdded()) {
            transaction.add(R.id.child_fragement, readfragment, "read");
        }
        if (isLeaderVisible && !leaderfragment.isAdded()) {
            transaction.add(R.id.child_fragement, leaderfragment, "leader");
        }
        if (isWordVisible && !wordfragment.isAdded()) {
            transaction.add(R.id.child_fragement, wordfragment, "words");
        }
        if (isShadowingVisible && !shadowingfragment.isAdded()) {
            transaction.add(R.id.child_fragement, shadowingfragment, "shadowing");
        }
        if (isBeisongVisivle && !beisongFragment.isAdded()) {
            transaction.add(R.id.child_fragement, beisongFragment, "beisong");
        }
        if (isReadVisible) {
            // readFragment可见
            if (textfragment != null && textfragment.isVisible()) {
                // 如果 textfragment 不为空且可见，则暂停或停止 MediaPlayer 播放
                textfragment.onPause();
            }

            for (Fragment fragment : fragmentManager.getFragments()) {
                // 如果不是 ReadFragment，则隐藏该 Fragment
                if (fragment != readfragment) {
                    transaction.hide(fragment);
                }
            }
            transaction.show(readfragment);
        } else if (isWordVisible) {
            if (wordfragment != null && wordfragment.isVisible()) {
                // 如果 textfragment 不为空且可见，则暂停或停止 MediaPlayer 播放
                wordfragment.onPause();
            }
            for (Fragment fragment : fragmentManager.getFragments()) {
                if (fragment != wordfragment) {
                    transaction.hide(fragment);
                }
            }
            transaction.show(wordfragment);

        }else if (isBeisongVisivle) {
            if (beisongFragment != null && beisongFragment.isVisible()) {
                // 如果 textfragment 不为空且可见，则暂停或停止 MediaPlayer 播放
//                beisongFragment.onPause();
            }
            for (Fragment fragment : fragmentManager.getFragments()) {
                // 如果不是 ReadFragment，则隐藏该 Fragment
                if (fragment != beisongFragment) {
                    transaction.hide(fragment);
                }
            }
            transaction.show(beisongFragment);

        } else if (isShadowingVisible) {
            if (shadowingfragment != null && shadowingfragment.isVisible()) {
                shadowingfragment.onPause();
            }
            for (Fragment fragment : fragmentManager.getFragments()) {
                if (fragment != shadowingfragment) {
                    transaction.hide(fragment);
                }
            }
            transaction.show(shadowingfragment);
        } else if (isLeaderVisible) {
            for (Fragment fragment : fragmentManager.getFragments()) {
                if (fragment != leaderfragment) {
                    transaction.hide(fragment);
                }
            }
            transaction.show(leaderfragment);
        } else {

            if (isTextVisible) {
                // textFragment可见
                if (readfragment != null && readfragment.isVisible()) {
                    readfragment.onPause();
                }

                for (Fragment fragment : fragmentManager.getFragments()) {
                    if (fragment != textfragment) {
                        transaction.hide(fragment);
                    }
                }
                transaction.show(textfragment);
            }
        }
        Constant.COMPETE = 0;
        Constant.SCROLL=0;

        onResume();
        transaction.commit();
    }

    //这里是实现了自动更新
    @Override
    protected void onResume() {

        // TODO Auto-generated method stub
        super.onResume();

        SharedPreferences sharedPreferencese = getSharedPreferences("bookicon", Context.MODE_PRIVATE);
        String category = sharedPreferencese.getString("category", "466");
        if (!(category.equals("466") || category.equals("467") || category.equals("468") || category.equals("469"))) {
            readLine.setVisibility(View.GONE);
        }
        //只有古诗才会展示背诵功能
        if(com.cn.ailanguage.primarychinese.network.Home.Constant.POEM!=2 && com.cn.ailanguage.primarychinese.network.Home.Constant.POEM!=3){
            beisongLine.setVisibility(View.GONE);
        }else{
            beisongLine.setVisibility(View.VISIBLE);
        }
        //展不展示生词
        if (Constant.WORD == 0) {
            wordLine.setVisibility(View.GONE);
        } else {
            wordLine.setVisibility(View.VISIBLE);
        }


        //点了顺序播放乱序播放或者，自动播放
        if (Constant.COMPETE == 1) {
            Log.e("qxy", "onResume: "+ Constant.flag );
            if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_ING < com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_MAX - 1 && Constant.flag == 1) {
                //顺序播放
                com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_ING++;
                int position = com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_ING;
                Constant.VOAID = home_data.get(0).get(position);
                Constant.TITLE = home_data.get(3).get(position);
                com.cn.ailanguage.primarychinese.network.Home.Constant.POEM= Integer.parseInt(home_data.get(10).get(position));
                Constant.MUSIC = "http://staticvip.iyuba.cn/sounds/voa" + home_data.get(7).get(position);
                //模拟点击
                bookLine.performClick();
            }
            else if (Constant.flag == 2)
            {
                int position = com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_ING;
                Constant.VOAID = home_data.get(0).get(position);
                Constant.TITLE = home_data.get(3).get(position);
                com.cn.ailanguage.primarychinese.network.Home.Constant.POEM= Integer.parseInt(home_data.get(10).get(position));
                Constant.MUSIC = "http://staticvip.iyuba.cn/sounds/voa" + home_data.get(7).get(position);
                bookLine.performClick();

            }
            else if (Constant.flag == 3) {
                Random r = new Random();
                int position = r.nextInt(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_MAX - 1);
                Constant.VOAID = home_data.get(0).get(position);
                Constant.TITLE = home_data.get(3).get(position);
                com.cn.ailanguage.primarychinese.network.Home.Constant.POEM= Integer.parseInt(home_data.get(10).get(position));
                Constant.MUSIC = "http://staticvip.iyuba.cn/sounds/voa" + home_data.get(7).get(position);
                bookLine.performClick();
            }


        }else if(Constant.SCROLL==1){
            if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_ING < com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_MAX - 1) {
                //顺序播放
                Toast.makeText(this, "即将进入下一篇", Toast.LENGTH_SHORT).show();

                try {
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_ING++;
                int position = com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.MUSIC_ING;
                Constant.VOAID = home_data.get(0).get(position);
                Constant.TITLE = home_data.get(3).get(position);
                com.cn.ailanguage.primarychinese.network.Home.Constant.POEM= Integer.parseInt(home_data.get(10).get(position));
                Constant.MUSIC = "http://staticvip.iyuba.cn/sounds/voa" + home_data.get(7).get(position);
                //模拟点击
                bookLine.performClick();
            }else{
                Toast.makeText(this, "后面没有了", Toast.LENGTH_SHORT).show();

            }
        }
        if (Constant.TITLE.length() > 9) {
            title.setText(Constant.TITLE.substring(0, 9) + "...");
        } else {
            title.setText(Constant.TITLE);
        }


    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.menu_muisc, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        // 获取屏幕宽度
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        // 设置弹窗位置在图片下方

        dialog.getWindow().setGravity(Gravity.RIGHT | Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        dialog.getWindow().getAttributes().y = more.getHeight() + 0; // 设置垂直偏移量

        // 设置背景透明度，让后面的页面不变暗
        dialog.getWindow().setDimAmount(0f);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnConfirm = dialogView.findViewById(R.id.word_size);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnCancel = dialogView.findViewById(R.id.show_pinyin);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击字体大小
                if (Constant.USERNAME.equals("nothing")) {
                    Dialog dialog = new Dialog(OriDetailActivity.this, "请先登录", "去登录", "取消");
                    dialog.ShowDialog();
                } else {
                    showCustomDialogSmall();
                    dialog.dismiss(); // 关闭弹窗;
                }

            }
        });
        //字体设置
        if (Constant.SHOW == 0) {
            btnCancel.setText("显示拼音");
        } else {
            btnCancel.setText("隐藏拼音");
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 处理“隐藏拼音”按钮的点击事件
                if (Constant.SHOW == 0) {
                    Constant.SHOW = 1;
                } else {
                    Constant.SHOW = 0;
                }
                Constant.canChange = true;

                dialog.dismiss(); // 关闭弹窗
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(550, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void showCustomDialogSmall() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.menu_wordsize, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        // 设置背景透明度，让后面的页面不变暗
        dialog.getWindow().setDimAmount(0f);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button big = dialogView.findViewById(R.id.big_word);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button normal = dialogView.findViewById(R.id.normal_word);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button small = dialogView.findViewById(R.id.small_word);
        //更变一下字体颜色，指明现在是哪个颜色
        if (Constant.BIGCOLOR == 1) {
            big.setTextColor(Color.parseColor("#F18E1B"));
        } else {
            big.setTextColor(Color.BLACK);
        }
        if (Constant.NORMALCOLOR == 1) {
            normal.setTextColor(Color.parseColor("#F18E1B"));
        } else {
            normal.setTextColor(Color.BLACK);
        }
        if (Constant.SMALLCOLOR == 1) {
            small.setTextColor(Color.parseColor("#F18E1B"));
        } else {
            small.setTextColor(Color.BLACK);
        }
        big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 点击“大”按钮字体大小
                //改变颜色
                Constant.SMALLCOLOR = 0;
                Constant.BIGCOLOR = 1;
                //改变原文大小
                Constant.NORMALCOLOR = 0;
                Constant.PINYINSIZE = 20;
                Constant.CHINESESIZE = 25;
                //改变原文2大小
                Constant.CHINESENSIZE2 = 19;
                Constant.PINYINSIZE2 = 12;
                //改变生词大小
                Constant.WORDPINYINSIZE = 20;
                Constant.WORDCHINESENSIZE = 40;
                Constant.canChange = true;

                dialog.dismiss(); // 关闭弹窗
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击“大”按钮字体大小
                //改变颜色
                Constant.SMALLCOLOR = 0;
                Constant.BIGCOLOR = 0;
                Constant.NORMALCOLOR = 1;
                //改变原文大小
                Constant.PINYINSIZE = 15;
                Constant.CHINESESIZE = 20;
                //改变原文2大小
                Constant.CHINESENSIZE2 = 19;
                Constant.PINYINSIZE2 = 12;
                //改变生词大小
                Constant.WORDPINYINSIZE = 15;
                Constant.WORDCHINESENSIZE = 35;
                Constant.canChange = true;

                dialog.dismiss(); // 关闭弹窗
            }
        });
        small.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                // 点击“小”按钮的点击事件
                //改变颜色
                Constant.SMALLCOLOR = 1;
                Constant.BIGCOLOR = 0;
                Constant.NORMALCOLOR = 0;
                //改变原文
                Constant.PINYINSIZE = 10;
                Constant.CHINESESIZE = 15;
                //改变原文2大小
                Constant.CHINESENSIZE2 = 15;
                Constant.PINYINSIZE2 = 8;
                //改变生词大小
                Constant.WORDPINYINSIZE = 10;
                Constant.WORDCHINESENSIZE = 30;
                Constant.canChange = true;

                dialog.dismiss(); // 关闭弹窗
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(550, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textfragment.textreals();
        if (readfragment != null) {
            readfragment.reals();
        }
        if (shadowingfragment != null) {
            shadowingfragment.shadowingreals();
        }
    }


    @Override
    public void getHome(MainBean mainBean) {
        List<MainBean.InfoBean> daily = mainBean.getInfo();
        List<String> ChineseList = new ArrayList<>();
        for (int i = 0; i < daily.size(); i++) {
            ChineseList.add(daily.get(i).getSentence());
        }
        Constant.SENTENCE = daily;
        if (mainBean.getWords().size() != 0) {
            Constant.WORD = 1;
        } else {
            Constant.WORD = 0;
        }
        //要不要隐藏生词
        if (Constant.WORD == 0) {
            wordLine.setVisibility(View.GONE);
        } else {
            wordLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

    }

}


