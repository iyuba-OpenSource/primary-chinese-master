package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Pome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.Pome.PomeAdapter;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.BookIconActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.Dialog;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.FightIconActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.LoginActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.BuyActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Top_page.MainFragmentActivity;
import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.Bean.PomeBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.Bean.DataBean;

import com.cn.ailanguage.primarychinese.View.HomeContract;
import com.cn.ailanguage.primarychinese.View.MainContract;
import com.cn.ailanguage.primarychinese.network.Home.Constant;
import com.cn.ailanguage.primarychinese.network.Home.NetWorkManager;
import com.cn.ailanguage.primarychinese.network.Main.MainNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.HomePresenter;
import com.cn.ailanguage.primarychinese.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class PomeFragment extends Fragment implements View.OnClickListener, HomeContract.HomeView{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View rootView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static List<PomeBean.InfoBean> pome_data;
    RecyclerView showLv;

    private HomePresenter homePresenter;
    LinearLayoutManager linearLayoutManager;
    private TextView name;
    private ImageView book;
    private TextView fight;

    public PomeFragment() {
    }

    public static PomeFragment newInstance(String param1, String param2) {
        PomeFragment fragment = new PomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pome2, container, false);
        name=rootView.findViewById(R.id.top_banner);
        book=rootView.findViewById(R.id.which_book);
        fight=rootView.findViewById(R.id.fight);
        SharedPreferences sharedPreferencesbook = getContext().getSharedPreferences("bookicon", Context.MODE_PRIVATE);
        String bookname=sharedPreferencesbook.getString("bookname", "小学语文一年级上");
        String category = sharedPreferencesbook.getString("category", "466");
        name.setText(bookname);
        //绑定adapter
        showLv = rootView.findViewById(R.id.homeRv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        showLv.setNestedScrollingEnabled(false);
        showLv.setLayoutManager(linearLayoutManager);


        //书内容的网络连接
        NetWorkManager.getInstance().init();
        homePresenter = new HomePresenter();
        homePresenter.attchView(this);
        homePresenter.getPome(category, "home","2");

        //点击选书
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BookIconActivity.class);
                startActivity(intent);
            }
        });
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "nothing");
                String uid = sharedPreferences.getString("uid", "nothing");
                if (uid.equals("nothing")) {
                    Dialog dialog = new Dialog(getContext(), "请先登录", "去登录", "取消");
                    dialog.ShowDialog();

                }
                else {
                    Intent intent = new Intent(getContext(), FightIconActivity.class);
                    startActivity(intent);

                }



            }
        });

        return rootView;
    }

    //书籍内容，目录
    @Override
    public void getHome(DataBean dataBean) {
    }

    @Override
    public void getWord(WordBean wordBean) {

    }

    @Override
    public void getPome(PomeBean pomeBean) {
        //获取古诗列表
        List<PomeBean.InfoBean> dataList = pomeBean.getInfo();
        pome_data=new ArrayList<>();
        SharedPreferences sharedPreferencese = getContext().getSharedPreferences("bookicon", Context.MODE_PRIVATE);
        String category = sharedPreferencese.getString("category", "466");
        for(int i=0;i<dataList.size();i++){
            if(dataList.get(i).getSeries().equals(category)){
                pome_data.add(dataList.get(i));
            }
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sound", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sound", "http://staticvip.iyuba.cn/sounds/voa" + dataList.get(0).getSound());
        editor.commit();
        Constant.VOAID=pomeBean.getInfo().get(0).getVoaid();
        PomeAdapter adapter = new PomeAdapter(pome_data, getContext());
        showLv.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View v) {

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

    //选哪本书，获得参数466等
    @Override
    public void onResume() {
        super.onResume();
        if (Constant.CHANGEBOOKPOME == 1) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("bookicon", Context.MODE_PRIVATE);
            String category = sharedPreferences.getString("category", "466");
            String bookname = sharedPreferences.getString("bookname", "小学语文一年级上");

            homePresenter.getPome(category, "home","2");
            name.setText(bookname);
            Constant.CHANGEBOOKPOME = 0;
        }
    }
}