package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.Pome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Pome.SmallPomeActivity;
import com.cn.ailanguage.primarychinese.Bean.PomeBean;
import com.cn.ailanguage.primarychinese.R;

import java.util.List;

public class PomeAdapter extends RecyclerView.Adapter<PomeAdapter.homeViewHolder> {


    private List<PomeBean.InfoBean> dates;
    private Context context;
    private int mSelectedPosition = RecyclerView.NO_POSITION;

    public PomeAdapter(List<PomeBean.InfoBean> dates, Context context) {
        this.dates = dates;
        this.context = context;
    }

    public List<PomeBean.InfoBean> getDates() {
        return dates;
    }

    public void setDates(List<PomeBean.InfoBean> dates) {
        this.dates = dates;
    }

    public Context getContext() {
        return context;
    }



    public void setContext(Context context) {
        this.context = context;
    }

    public void setSelectedPosition(int position) {
        // 设置选中位置并刷新列表
        mSelectedPosition = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public homeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_pome, parent, false);
        return new homeViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull homeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PomeBean.InfoBean daily = dates.get(position);

        holder.spokendetailText1.setText(daily.getTitle());
        if (!Constant.SENTENCE.equals("")) {
            holder.context.setText(Constant.SENTENCE);
        }
        String sentence = daily.getContent();

        holder.context.setText(sentence.substring(0,15)+"...");
        holder.author.setText("作者 : "+daily.getTitle_cn());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取pomefragment的数据
                Constant.MUSIC_ING = position;
                Constant.MUSIC_MAX = getItemCount();
                Intent intent = new Intent(context, SmallPomeActivity.class);
                String message = "http://staticvip.iyuba.cn/sounds/voa" + daily.getSound();
                com.cn.ailanguage.primarychinese.network.Main.Constant.MUSIC = message;
                com.cn.ailanguage.primarychinese.network.Main.Constant.VOAID = daily.getVoaid();
                com.cn.ailanguage.primarychinese.network.Main.Constant.TITLE = daily.getTitle();
                com.cn.ailanguage.primarychinese.network.Main.Constant.POMEAUTHOR = daily.getTitle_cn();
                com.cn.ailanguage.primarychinese.network.Home.Constant.POEM = Integer.parseInt(dates.get(position).getFlag());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dates == null ? 0 : dates.size();
    }


    class homeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView spokendetailText1, author, context;


        public homeViewHolder(@NonNull View itemView) {
            super(itemView);

            spokendetailText1 = itemView.findViewById(R.id.text_view);
            author = itemView.findViewById(R.id.author_pome);
            context = itemView.findViewById(R.id.context_pome);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // 处理点击事件
                setSelectedPosition(position);
            }

        }
    }

}
