package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person;

import static com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.BookIconActivity.isNetworkConnected;
import static java.lang.Long.parseLong;
import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.MyService;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.BookIconActivity;
import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.BuyActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.AboutActivity;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.Bean.UidBean;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.PrivacyActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.TermActivity;
import com.cn.ailanguage.primarychinese.View.UidContract;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.network.Uid.UidNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.UidPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonFragment extends Fragment implements UidContract.UidView, QuickLoginActivity.OnClickActivityListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;
    private ImageView image, vip;
    private LinearLayout phone;
    private View rootView;
    private LinearLayout backline, privacy_policy, about_us, term_of_use, not_login, vip_buy, book, reflash_line,jubao;
    private Button login;
    private int pack1, flag = 1;
    private TextView back, hide_username;
    private String uid, imagePic;
    private UidPresenter uidPresenter;



    public PersonFragment() {
        // Required empty public constructor
    }

    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
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
    private boolean isServiceBound = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_person2, container, false);
        image = rootView.findViewById(R.id.image);
        phone = rootView.findViewById(R.id.phone);
        login = rootView.findViewById(R.id.login);
        hide_username = rootView.findViewById(R.id.hide_username);
        term_of_use = rootView.findViewById(R.id.term_of_use);
        privacy_policy = rootView.findViewById(R.id.privacy_policy);
        back = rootView.findViewById(R.id.back);
        backline = rootView.findViewById(R.id.backline);
        about_us = rootView.findViewById(R.id.about_us);
        not_login = rootView.findViewById(R.id.not_login);
        vip_buy = rootView.findViewById(R.id.vipbuy);
        vip = rootView.findViewById(R.id.vip);
        book = rootView.findViewById(R.id.book);
        reflash_line = rootView.findViewById(R.id.reflash);
        jubao=rootView.findViewById(R.id.jubao_line);
        //个人信息更新
        UidNetWorkManager.getInstance().init();
        uidPresenter = new UidPresenter();
        uidPresenter.attchView(this);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "nothing");
        imagePic = sharedPreferences.getString("image", "nothing");



        if (!uid.equals("nothing")) {
            String sign1 = MD5.md5("20001" + uid + "iyubaV2");
            uidPresenter.getHome("android", "json", 20001, Integer.parseInt(uid), Integer.parseInt(uid), 292, sign1);

        }

        //点击打电话
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个 Intent 对象，用于跳转到电话界面
                Intent intent = new Intent(Intent.ACTION_DIAL);
                // 设置电话号码
                intent.setData(Uri.parse("tel:4008881905"));
                // 启动电话界面
                startActivity(intent);
            }
        });
        //举报
        jubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String qqNumber = "572828703"; // 替换成目标QQ号码
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNumber));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(requireActivity(), "您还没有安装QQ，请先安装软件", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //返回
        backline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("uid", String.valueOf("nothing"));
                editor.commit();
                Constant.USERNAME = "nothing";
                decoration(Constant.USERNAME);
            }
        });
        //更新书籍
        reflash_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isConnected = isNetworkConnected(getContext());
                if (isConnected) {
                    // 设备已联网
                    // 点击按钮后执行数据库更新操作
                    Intent serviceIntent = new Intent(getActivity(), MyService.class);
                    getActivity().startService(serviceIntent);
                } else {
                    // 设备未联网
                    Toast.makeText(getContext(), "请先联网", Toast.LENGTH_SHORT).show();

                }

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.SecVerify.equals("true")) {
                    Intent intent = new Intent(getActivity(), OneClickLoginActivity.class);
                    startActivity(intent);
                } else if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.SecVerify.equals("false")) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    toast("出错啦~，请重新进入app");
                }
            }
        });
        //会员中心点击
        vip_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                String uid = sharedPreferences.getString("uid", "nothing");
                if (uid.equals("nothing")) {
                    //还未登录
                    if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.SecVerify.equals("true")) {
                        Intent intent = new Intent(getActivity(), OneClickLoginActivity.class);
                        startActivity(intent);
                    } else if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.SecVerify.equals("false")) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        toast("出错啦~，请重新进入app");
                    }
                } else {
                    //登录过
                    Intent intent = new Intent(getActivity(), BuyActivity.class);
                    startActivity(intent);
                }

            }
        });
        //点击选书
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BookIconActivity.class);
                startActivity(intent);
            }
        });


        //隐私协议和用户政策点击事件,关于
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivacyActivity.class);
                startActivity(intent);
            }
        });
        term_of_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), TermActivity.class);
                startActivity(intent1);
            }
        });
        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent2);
            }
        });
        //初始化界面
        Startlogin();

        return rootView;
    }

    private void Startlogin() {
        //开始界面加载布局
        //改变界面颜色
        Window window = requireActivity().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.homecolor));
        }
        Glide.with(getActivity())
                .load(R.mipmap.person)  // 替换为实际的本地图片资源ID
                .circleCrop()
                .apply(RequestOptions.circleCropTransform())
                .into(image);
        not_login.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
        vip.setVisibility(View.GONE);
        hide_username.setVisibility(View.GONE); // 隐藏文本框，但占用布局空间
        onResume();
    }





    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "nothing");
        imagePic = sharedPreferences.getString("image", "nothing");
        if (Constant.TURN == 1 && !uid.equals("nothing")) {
            //如果登录完成的话，更新这个界面的布局
            String sign1 = MD5.md5("20001" + uid + "iyubaV2");
            uidPresenter.getHome("android", "json", 20001, Integer.parseInt(uid), Integer.parseInt(uid), 292, sign1);
            Constant.TURN = 0;
        }
        decoration(Constant.USERNAME);

    }

    @Override
    public void getHome(UidBean uidBean) {
        //需要login记录一下imagesrc和uid
        long days = parseLong(uidBean.getExpireTime());
        com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIMESMALL = days;
        Date date = new Date(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIMESMALL * 1000);
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTime = sdf.format(date);
        int result = date.compareTo(currentDate);
        if (result > 0) {
            // 会员在有效期内
            com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME = formattedTime;
        } else {
            com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME = "0";
        }
        Constant.PACK = String.valueOf(uidBean.getMoney());
        Constant.INTERAL = uidBean.getCredits();
        Constant.CORN = String.valueOf(uidBean.getAmount());
        Constant.USERNAME = uidBean.getUsername();
        decoration(Constant.USERNAME);
    }

    //判断选择那种布局
    public void decoration(String name) {
        //会员图标
        if (name.equals("nothing")) {
            //隐藏控件
            login.setVisibility(View.VISIBLE);
            not_login.setVisibility(View.GONE);
            vip.setVisibility(View.GONE);
            hide_username.setVisibility(View.GONE);
            Glide.with(getActivity())
                    .load(R.mipmap.person)
                    .circleCrop()// 替换为实际的本地图片资源ID
                    .apply(RequestOptions.circleCropTransform())
                    .into(image);

        } else {
            not_login.setVisibility(View.VISIBLE);
            login.setVisibility(View.INVISIBLE);
//            Log.d("xwh888", "decoration: "+com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME);
            if (com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VIPTIME.equals("0")) {
                //判断是不是vip
                vip.setVisibility(View.GONE);
            } else {
                vip.setVisibility(View.VISIBLE);
            }
            hide_username.setVisibility(View.VISIBLE); // 隐藏文本框，但占用布局空间
            hide_username.setText(name);
            privacy_policy.setVisibility(View.VISIBLE);
            term_of_use.setVisibility(View.VISIBLE);
            // 使用Glide加载图片
            Glide.with(this)
                    .load(imagePic) // 加载图片的地址
                    .circleCrop()
                    .placeholder(R.mipmap.person) // 占位图，图片正在加载时显示
                    .error(R.mipmap.person) // 错误图，图片加载失败时显示
                    .into(image); // 将图片设置到ImageView中
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

    @Override
    public void onPause() {
        flag = 1;
        super.onPause();
        Log.d("xwh25", Constant.USERNAME);
        decoration(Constant.USERNAME);

    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onClickActivity() {
        Log.d("xwh78", Constant.USERNAME);
        decoration(Constant.USERNAME);
    }
}
