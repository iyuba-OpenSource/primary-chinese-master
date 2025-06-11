package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.Core_Pack_Integral;

import static com.cn.ailanguage.primarychinese.Activity_and_Fragment.Top_page.MyApplication.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.PackAdapter;
import com.cn.ailanguage.primarychinese.Bean.HistoryBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.HistoryContract;
import com.cn.ailanguage.primarychinese.network.History.HistoryNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.presenter.HistoryPresenter;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class PackActivtiy extends AppCompatActivity implements HistoryContract.HistoryView{
    private ImageView back_person;
    private RecyclerView recyclerView;
    private TextView allPack;
    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        back_person=findViewById(R.id.back_person);
        allPack=findViewById(R.id.all_pack);


        //网络连接
        String uid,pages="1",pageCount="20",sign;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        uid=sharedPreferences.getString("uid","nothing");
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        sign = MD5.md5(uid+ "iyuba"+df.format(System.currentTimeMillis()));

        HistoryNetWorkManager.getInstance().init();
        HistoryPresenter historyPresenter = new HistoryPresenter();
        historyPresenter.attchView((HistoryContract.HistoryView) this);
        historyPresenter.getHome(uid,pages,pageCount,sign);

        //返回按钮
        back_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //绑定adapter
        recyclerView=findViewById(R.id.history_recycler);
        LinearLayoutManager linearLayoutManagerChinese = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManagerChinese);


        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }

    }

    @Override
    public void getHome(HistoryBean historyBean) {
        List<HistoryBean.DataBean> list=historyBean.getData();
        PackAdapter packAdapter = new PackAdapter(getContext(),list);
        recyclerView.setAdapter(packAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //给钱包赋值
        allPack.setText(StringUtils.leftPad(String.valueOf(Float.parseFloat(Constant.PACK) / 100), 2, "0"));

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
