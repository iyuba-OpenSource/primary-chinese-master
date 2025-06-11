package com.cn.ailanguage.primarychinese.Activity_and_Fragment.OriDetail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.Dialog;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.PullUpLoading;
import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.LeaderAdapter;
import com.cn.ailanguage.primarychinese.Bean.EvaBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderBean;
import com.cn.ailanguage.primarychinese.Bean.LeaderUserBean;
import com.cn.ailanguage.primarychinese.Bean.MusicBean;
import com.cn.ailanguage.primarychinese.Bean.ShadowingBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.ShadowingContract;
import com.cn.ailanguage.primarychinese.network.Main.Constant;
import com.cn.ailanguage.primarychinese.network.Shadowing.ShadowingNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.ShadowingPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderFragment extends Fragment implements ShadowingContract.ShadowingView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private int sum = 0;
    LeaderAdapter leaderAdapter;
    RecyclerView recyclerView;
    private PullUpLoading pullUpLoading;
    TextView live, name, which, fraction, loading;
    LinearLayout linearLayout;
    ImageView touxiang;
    String uid, sign, nameuser, image;
    ShadowingPresenter shadowingPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout loading_loadend;
    private ProgressBar progressBar;

//    private ShadowingPresenter shadowingPresenter;

    public LeaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderFragment newInstance(String param1, String param2) {
        LeaderFragment fragment = new LeaderFragment();
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "nothing");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        sign = MD5.md5(uid + "primarychinese" + Constant.VOAID + "0" + "10" + formattedDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_leader, container, false);
        recyclerView = rootView.findViewById(R.id.recye_in);
        live = rootView.findViewById(R.id.live);
        name = rootView.findViewById(R.id.name_now);
        which = rootView.findViewById(R.id.which_now);
        touxiang = rootView.findViewById(R.id.touxiang_now);
        fraction = rootView.findViewById(R.id.sentence_fraction_now);
        linearLayout = rootView.findViewById(R.id.user_line);
        loading = rootView.findViewById(R.id.loading);
        loading_loadend = rootView.findViewById(R.id.loading_loadend);
        progressBar = rootView.findViewById(R.id.progress);
        //绑定
        LinearLayoutManager linearLayoutManagerChinese = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManagerChinese);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "nothing");
        if (uid.equals("nothing")) {
            Dialog dialog = new Dialog(getContext(), "请先登录", "去登录", "取消");
            dialog.ShowDialog();
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences6 = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                String uidf = sharedPreferences6.getString("uid", "nothing");
                Intent intent = new Intent(getContext(), LeaderSmallActivity.class);
                intent.putExtra("uid", uidf);
                intent.putExtra("name", nameuser);
                intent.putExtra("image", image);
                startActivity(intent);
            }
        });
        ShadowingNetWorkManager.getInstance().init();
        shadowingPresenter = new ShadowingPresenter();
        shadowingPresenter.attchView(this);
//        Log.d("xwh89", "onCreateView: 1");
        shadowingPresenter.getLeader(uid, "0", "10", "0", "primarychinese", Constant.VOAID, sign);
        Glide.with(this)
                .load(R.mipmap.person) // 加载图片的地址
                .circleCrop()
                .placeholder(R.mipmap.person) // 占位图，图片正在加载时显示
                .error(R.mipmap.person) // 错误图，图片加载失败时显示
                .into(touxiang); // 将图片设置到ImageView中

        //拉下刷新


        pullUpLoading = new PullUpLoading(linearLayoutManagerChinese) {
            @Override
            public void onLoadMore() {
                loading_loadend.setVisibility(View.VISIBLE);
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (leaderAdapter == null) {
                            return;
                        }
//                        Log.d("xwh89", "onCreateView: 2");
//                        Log.d("xwh90", Constant.oriendboaid);
                        //由登录界面返回
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                        String uid1 = sharedPreferences.getString("uid", "nothing");
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = sdf.format(date);
                        String sign1 = MD5.md5(uid1 + "primarychinese" + Constant.VOAID + "0" + "10" + formattedDate);
                        shadowingPresenter.getLeader(uid1, "0", "10", Constant.oriendboaid, "primarychinese", Constant.VOAID, sign1);
                        loading_loadend.setVisibility(View.GONE);


                    }
                }, 1000);
            }
        };
        recyclerView.addOnScrollListener(pullUpLoading);
        return rootView;
    }


    @Override
    public void getLeader(LeaderBean leaderBean) {
        nameuser = leaderBean.getMyname();
        image = leaderBean.getMyimgSrc();

        if (leaderBean.getMessage().equals("Success")) {

            Glide.with(this)
                    .load(leaderBean.getMyimgSrc()) // 加载图片的地址
                    .circleCrop()
                    .placeholder(R.mipmap.person) // 占位图，图片正在加载时显示
                    .error(R.mipmap.person) // 错误图，图片加载失败时显示
                    .into(touxiang); // 将图片设置到ImageView中
            name.setText(leaderBean.getMyname());
            fraction.setText("总分数:" + String.valueOf(leaderBean.getMyscores()));
            which.setText(String.valueOf(leaderBean.getMyranking()));


            //绑定adapter
            if (leaderBean.getData().size() == 0 && leaderAdapter == null) {
                linearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                live.setVisibility(View.VISIBLE);
                live.setText("暂无排行情况");
            } else {
                //有数据
                linearLayout.setVisibility(View.VISIBLE);
                live.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                List<LeaderBean.DataBean> list = leaderBean.getData();
                if (pullUpLoading.isLoading() && list.size() > 0) {   //执行上拉刷新


                    if (leaderAdapter == null) {
                        if (list.size() >= 10) {
                            Constant.oriendboaid = String.valueOf(list.get(9).getRanking());
                        } else {
                            Constant.oriendboaid = String.valueOf(list.get(list.size() - 1).getRanking());
                        }
                        leaderAdapter = new LeaderAdapter(getContext(), leaderBean.getData());
                        recyclerView.setAdapter(leaderAdapter);
                    } else {
                        if (list.size() >= 10) {
                            Constant.oriendboaid = String.valueOf(list.get(9).getRanking());
                        } else {
                            Constant.oriendboaid = String.valueOf(list.get(list.size() - 1).getRanking());
                        }
                        List<LeaderBean.DataBean> dataDTOS = leaderAdapter.getDatas();
                        dataDTOS.addAll(list);   //添加进去 ,并且刷新
                        leaderAdapter.notifyDataSetChanged();
                    }

                } else {
                    //没有上拉

                    if (list.size() >= 10) {
                        Constant.oriendboaid = String.valueOf(list.get(9).getRanking());
                    } else {
                        Constant.oriendboaid = String.valueOf(list.get(list.size() - 1).getRanking());
                    }
                    if (leaderAdapter == null) {
                        leaderAdapter = new LeaderAdapter(getContext(), leaderBean.getData());
                        recyclerView.setAdapter(leaderAdapter);
                    }
                    leaderAdapter.notifyDataSetChanged();

                }
                pullUpLoading.setLoading(false);
            }

        } else {
            live.setVisibility(View.VISIBLE);
            live.setText("排行榜加载失败");
            Log.d("xwh89", Constant.oriendboaid);

            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }
        Constant.ITEMNUME = leaderBean.getData().size();
    }

    public void refreshUI() {
        SharedPreferences sharedPreference5 = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
        String uid1 = sharedPreference5.getString("uid", "nothing");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        String sign1 = MD5.md5(uid1 + "primarychinese" + Constant.VOAID + "0" + "10" + formattedDate);
        Constant.oriendboaid = "0";
        shadowingPresenter.getLeader(uid, "0", "10", "0", "primarychinese", Constant.VOAID, sign1);
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (Constant.UPLOAD == 1) {
                SharedPreferences sharedPreference = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
                String uid1 = sharedPreference.getString("uid", "nothing");
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(date);
                String sign1 = MD5.md5(uid1 + "primarychinese" + Constant.VOAID + "0" + "10" + formattedDate);
                Log.d("xwh89", "onCreateView: 4");

                shadowingPresenter.getLeader(uid1, "0", "10", "0", "primarychinese", Constant.VOAID, sign1);
                Constant.UPLOAD = 0;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("xwh00", "run: " + com.cn.ailanguage.primarychinese.network.Main.Constant.TURN);
        if (com.cn.ailanguage.primarychinese.network.Main.Constant.TURN == 1) {
            //如果是登录界面跳转过来的话，就需要重新获取个人数据
            SharedPreferences sharedPreference = getActivity().getSharedPreferences("ceshi", Context.MODE_PRIVATE);
            String uid1 = sharedPreference.getString("uid", "nothing");
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(date);
            String sign1 = MD5.md5(uid1 + "primarychinese" + Constant.VOAID + "0" + "10" + formattedDate);
            Constant.oriendboaid = "0";
            shadowingPresenter.getLeader(uid1, "0", "10", Constant.oriendboaid, "primarychinese", Constant.VOAID, sign1);
            com.cn.ailanguage.primarychinese.network.Main.Constant.TURN = 0;

        }

    }


    @Override
    public void getLeaderUser(LeaderUserBean leaderuserBean) {

    }

    //下面都是用不到的
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
    public void getHome(ShadowingBean shadowingBean) {

    }

    @Override
    public void getEvaluating(EvaBean evaBean) {

    }

    @Override
    public void getMusic(MusicBean musicBean) {

    }


}