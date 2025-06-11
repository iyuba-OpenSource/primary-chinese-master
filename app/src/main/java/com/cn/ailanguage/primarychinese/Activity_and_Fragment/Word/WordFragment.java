package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Word;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.BookIconActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.WordHomeAdapter;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Word.Three_icon.OneActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Word.Three_icon.ThreeActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Word.Three_icon.TowActivity;
import com.cn.ailanguage.primarychinese.Bean.AllWordBean;
import com.cn.ailanguage.primarychinese.Bean.BookBean;
import com.cn.ailanguage.primarychinese.Bean.DataBean;
import com.cn.ailanguage.primarychinese.Bean.PomeBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.BookContract;
import com.cn.ailanguage.primarychinese.View.HomeContract;
import com.cn.ailanguage.primarychinese.network.Book.BookNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Home.Constant;
import com.cn.ailanguage.primarychinese.network.Home.NetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.BookPresenter;
import com.cn.ailanguage.primarychinese.presenter.HomePresenter;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordFragment extends Fragment implements HomeContract.HomeView, WordHomeAdapter.OnItemClickListener , BookContract.BookView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private RecyclerView recyclerView;
    private TextView textView, textWord;
    private String category;
    private List<DataBean.InfoBean> date;
    private ImageView imageView, pencil;
    private PopupWindow mPopupWindow;
    private BookPresenter bookPresenter;

    HomePresenter homePresenter;
    private LinearLayout tingliear, hanyinliear, yinhanliear, pingxieliear;

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
        rootView = inflater.inflate(R.layout.fragment_word2, container, false);
        recyclerView = rootView.findViewById(R.id.recycle_word);
        textView = rootView.findViewById(R.id.name_book);
        imageView = rootView.findViewById(R.id.which_book);
        textWord = rootView.findViewById(R.id.word_text);

        pencil = rootView.findViewById(R.id.pencil);
        //书内容的网络连接
        SharedPreferences sharedPreferencesbook = getContext().getSharedPreferences("bookicon", Context.MODE_PRIVATE);
        String bookname = sharedPreferencesbook.getString("bookname", "小学语文一年级上");
        category = sharedPreferencesbook.getString("category", "466");
        textView.setText(bookname.substring(4));
// 获得书本名字等
        NetWorkManager.getInstance().init();
        homePresenter = new HomePresenter();
        homePresenter.attchView(this);
        homePresenter.getHome(Integer.parseInt(category), "home");
//获得这本书的所有生词
        BookNetWorkManager.getInstance().init();
        bookPresenter = new BookPresenter();
        bookPresenter.attchView(this);
        bookPresenter.getAllWord(category,"all");
        //点击选书
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BookIconActivity.class);
                startActivity(intent);
            }
        });
//        点击笔
        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "功能开发中", Toast.LENGTH_SHORT).show();
            }
        });
        //弹窗
        View popupView = getLayoutInflater().inflate(R.layout.pop_window, null);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置背景变暗
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
        //设置pop出现的大小
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置 PopupWindow 的内容和属性，四个选项
        tingliear = popupView.findViewById(R.id.tingli);
        hanyinliear = popupView.findViewById(R.id.hanyin);
        yinhanliear = popupView.findViewById(R.id.yinhan);
        pingxieliear = popupView.findViewById(R.id.pingxie);
        yinhanliear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), OneActivity.class);
                startActivity(intent);
                com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.RIGHT=0;
                mPopupWindow.dismiss(); // 关闭弹窗
            }
        });
        hanyinliear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), TowActivity.class);
                startActivity(intent);
                com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.RIGHT=0;
                mPopupWindow.dismiss(); // 关闭弹窗
            }
        });
        tingliear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ThreeActivity.class);
                startActivity(intent);
                com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.RIGHT=0;
                mPopupWindow.dismiss(); // 关闭弹窗
            }
        });


        return rootView;
    }

    // 将背景变暗的方法
    private void darkenBackground(float dimAmount) {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = dimAmount;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(layoutParams);
    }


    @Override
    public void getPome(PomeBean pomeBean) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //如果选书了，就需要改变目录
        if (Constant.CHANGEBOOK == 1) {
            SharedPreferences sharedPreferencesbook1 = getContext().getSharedPreferences("bookicon", Context.MODE_PRIVATE);
            String bookname = sharedPreferencesbook1.getString("bookname", "小学语文一年级上");
            category = sharedPreferencesbook1.getString("category", "466");
            textView.setText(bookname.substring(4));
            NetWorkManager.getInstance().init();
            homePresenter = new HomePresenter();
            homePresenter.attchView(this);
            homePresenter.getHome(Integer.parseInt(category), "home");
            Constant.CHANGEBOOK = 0;
        }
    }

    @Override
    public void getHome(DataBean dataBean) {
        date = dataBean.getInfo();
        //判断有没有生词，没有就隐藏这一课
        if (dataBean.getInfo().size() == 0) {
            textWord.setVisibility(View.VISIBLE);
            Constant.LIVEINGWORD = 0;
        } else {
            Constant.LIVEINGWORD = 1;
            textWord.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManagerChinese = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManagerChinese);
            homePresenter.getWord(category);
        }
    }

    @Override
    public void getWord(WordBean wordBean) {
        //获取这一课的生词
        WordHomeAdapter adapter = new WordHomeAdapter(getContext(), date, wordBean); // 创建适配器，dataList 是数据列表
        recyclerView.setAdapter(adapter); // 设置适配器
        adapter.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onItemClick(int position) {
        // 显示 PopupWindow
        View parentView = rootView.findViewById(R.id.liney);
        darkenBackground(0.5f); // 将背景变暗
        mPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void getHome(BookBean bookBean) {

    }

    @Override
    public void getAllWord(AllWordBean allWordBean) {
        //获取全部课文的生词，方便后续四大模块的正确选项和错误选项的填写

        com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.WORDALL=allWordBean.getData();
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