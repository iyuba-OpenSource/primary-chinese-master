package com.cn.ailanguage.primarychinese.Activity_and_Fragment.OriDetail;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.ReadAdapter;
import com.cn.ailanguage.primarychinese.Bean.MainBean;

import com.cn.ailanguage.primarychinese.View.MainContract;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.network.Main.MainNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class ReadFragment extends Fragment implements View.OnClickListener, MainContract.MainView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private MainPresenter mainPresenter;
    private String mParam2;
    private View rootView;
    private List<String> imagList=new ArrayList<>(),unimagList=new ArrayList<>(),positionEndTime=new ArrayList<>(),positionTime=new ArrayList<>();
    List<MainBean.InfoBean> list;
    ArrayList<View> viewContainter = new ArrayList<View>();

    private ViewPager viewPager;
    private ReadAdapter adapter;
    private TextView page;
    long startTime,endTime;
    public ReadFragment() {
        // Required empty public constructor
    }

    public static ReadFragment newInstance(String param1, String param2) {
        ReadFragment fragment = new ReadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startTime = System.currentTimeMillis();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_read, container, false);

        viewPager=rootView.findViewById(R.id.viewpager);
        page=rootView.findViewById(R.id.page);

        //网络连接
        MainNetWorkManager.getInstance().init();
        mainPresenter = new MainPresenter();
        mainPresenter.attchView(this);
        mainPresenter.getHome("detail", Constant.VOAID);
        return rootView;
    }


    @Override
    public void getHome(MainBean mainBean) {
        list = mainBean.getInfo();
        //得到背景图片集合
        for(int i=0;i<list.size();i++){
            positionTime.add(list.get(i).getTiming());
            positionEndTime.add(list.get(i).getEndTiming());
            unimagList.add(list.get(i).getImgPath());

        }

        //对imagList去重
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>(unimagList);
        imagList = new ArrayList<>(linkedHashSet);

        //添加相应数量的view
        for(int i=0;i<imagList.size();i++){
            View view3 = LayoutInflater.from(getContext()).inflate(R.layout.item_image, null);
            viewContainter.add(view3);
        }
        //适配器绑定与控制pageview滑动状态
        adapter = new ReadAdapter(getContext(),viewContainter,imagList,list);
        Log.d("xwh88", list.get(0).getPosition());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 在滑动过程中的逻辑处理
                if (positionOffset > 0.5) {
                    viewPager.setCurrentItem(position + 1); // 切换到下一个界面
                }
            }

            @Override
            public void onPageSelected(int position) {
                // 页面切换完成后的逻辑处理
                int pageCount = adapter.getCount();
                page.setText(String.valueOf(position+1)+"/"+pageCount);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 页面滑动状态改变时的逻辑处理
            }
        });
        //如果初始化没有1/1
        int pageCount = adapter.getCount();
        page.setText("1"+"/"+pageCount);
    }
    @Override
    public void onDestroy() {
        viewContainter.clear();
        if(adapter!=null && adapter.getMediaPlayer()!=null){
            MediaPlayer mediaPlayer = adapter.getMediaPlayer();
            mediaPlayer.stop();
        }
        super.onDestroy();
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
    public void onPause() {
        super.onPause();
        try {
            MediaPlayer mediaPlayer=adapter.getMediaPlayer();
            mediaPlayer = adapter.getMediaPlayer();
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //当hide这个界面时，音频停止
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // 当前Fragment被隐藏时，停止音乐播放
            if(adapter!=null) {
                adapter.notifyDataSetChanged();
                MediaPlayer mediaPlayer = adapter.getMediaPlayer();
                if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                    mediaPlayer.pause();
                }
            }
        }
    }
    //传递给MusicActivity，当按住返回键时，停止播放音频
    public void reals(){

        if (adapter!=null){
            adapter.realss();
        }
    }



}