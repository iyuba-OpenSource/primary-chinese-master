package com.cn.ailanguage.primarychinese.Activity_and_Fragment.OriDetail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.GridSpacingItemDecoration;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.WordAdapter;
import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.MainContract;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.network.Main.MainNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.MainPresenter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordFragment extends Fragment implements View.OnClickListener, MainContract.MainView{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Handler handler = new Handler();
    private View rootView;
    WordAdapter adapter;
    private RecyclerView recycleWords;

    public WordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordFragment newInstance(String param1, String param2) {
        WordFragment fragment = new WordFragment();
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_word, container, false);
        recycleWords=rootView.findViewById(R.id.words);
        //绑定布局
        LinearLayoutManager linearLayoutManagerChinese = new LinearLayoutManager(getContext());
        recycleWords.setLayoutManager(linearLayoutManagerChinese);

        //网络连接
        MainPresenter mainPresenter;
        MainNetWorkManager.getInstance().init();
        mainPresenter = new MainPresenter();
        mainPresenter.attchView(this);
        mainPresenter.getHome("detail", Constant.VOAID);
        return rootView;
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

    @Override
    public void getHome(MainBean mainBean) {
        List<MainBean.WordsBean> daily=mainBean.getWords();
        //绑定适配器
        adapter=new WordAdapter(getContext(),daily);
        recycleWords.setAdapter(adapter);
        //一行有多少个字
        int itemSize=4;
        if(daily.size()%3==0){
            itemSize=3;
        }
        if(daily.size()%4==0){
            itemSize=4;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 500);
            }
        }, 0);
        //网格布局
        GridLayoutManager gri=new GridLayoutManager(getContext(), itemSize);
        recycleWords.setLayoutManager(gri);
        recycleWords.addItemDecoration(new GridSpacingItemDecoration(1));


    }
    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放MediaPlayer资源
        handler.removeCallbacksAndMessages(null);
    }
}