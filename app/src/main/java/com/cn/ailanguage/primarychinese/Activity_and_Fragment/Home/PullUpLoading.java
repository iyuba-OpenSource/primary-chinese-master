package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.network.Main.Constant;

public abstract class PullUpLoading extends  RecyclerView.OnScrollListener{

    private LinearLayoutManager mLinearLayoutManager;
    private int totalItemCount;//已经加载出来的数量
    private int visibleItemCount;//在屏幕上可见的item

    //   int page = 1;
    public int lastVisibleItemPosition;
    private boolean isLoading  = false;//控制不要重复加载更多
    private int previousTotal;//用来存储上一个total

    public PullUpLoading(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = mLinearLayoutManager.getItemCount();
        visibleItemCount = mLinearLayoutManager.getChildCount();
        lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();


        if (visibleItemCount > 0  && !isLoading
                && lastVisibleItemPosition >= totalItemCount - 1//最后一个item可见
                && totalItemCount >= visibleItemCount && Constant.ITEMNUME>=10) {//数据不足一屏幕不触发加载更多
            onLoadMore();
            isLoading = true;
        }
    }
    public boolean isLoading() {
        return isLoading;
    }
    public void setLoading(boolean loading) {
        isLoading = loading;
    }
    public abstract void onLoadMore();
}

