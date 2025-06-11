package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.OriDetail.LeaderSmallActivity;
import com.cn.ailanguage.primarychinese.Bean.LeaderBean;
import com.cn.ailanguage.primarychinese.R;

import java.util.List;

public class LeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<LeaderBean.DataBean> datas;
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM1 = 1;

    public LeaderAdapter(Context context, List<LeaderBean.DataBean> datas) {
        this.datas = datas;
        this.context = context;
    }
    public List<LeaderBean.DataBean> getDatas() {
        return datas;
    }

    public void setDatas(List<LeaderBean.DataBean> datas) {
        this.datas = datas;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER; // 头部视图类型
        } else {
            return TYPE_ITEM1; // 类型为 Item2 的项目视图类型
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            // 创建头部视图持有者，第一个视图
            View view = inflater.inflate(R.layout.leader_for, parent, false);
            return new ViewHolder(view);
        } else {
            // 创建类型为 Item2 的项目视图持有者，第二个视图
            View view = inflater.inflate(R.layout.leader_adapter, parent, false);
            return new Item2ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (holder instanceof ViewHolder) {
            // 绑定头部数据到第一个视图上

            ((ViewHolder) holder).name.setText(datas.get(position).getName());
            ((ViewHolder) holder).textView_fraction.setText("总分数:" + String.valueOf(datas.get(position).getScores()));
            Glide.with(context)
                    .load(datas.get(position).getImgSrc()) // 加载图片的地址
                    .circleCrop()
                    .placeholder(R.mipmap.person) // 占位图，图片正在加载时显示
                    .error(R.mipmap.person) // 错误图，图片加载失败时显示
                    .into(((ViewHolder) holder).avatar); // 将图片设置到ImageView中
            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LeaderSmallActivity.class);
                    intent.putExtra("uid", String.valueOf(datas.get(position).getUid()));
                    intent.putExtra("name", datas.get(position).getName());
                    intent.putExtra("image", datas.get(position).getImgSrc());
//                intent.putString("data", data);
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof Item2ViewHolder) {
            // 绑定类型为 Item2 的项目数据到第二个视图上

            ((Item2ViewHolder) holder).which.setText(String.valueOf(datas.get(position).getRanking()));
            ((Item2ViewHolder) holder).name.setText(datas.get(position).getName());
            ((Item2ViewHolder) holder).textView_fraction.setText("总分数:" + String.valueOf(datas.get(position).getScores()));
            Glide.with(context)
                    .load(datas.get(position).getImgSrc()) // 加载图片的地址
                    .circleCrop()
                    .placeholder(R.mipmap.person) // 占位图，图片正在加载时显示
                    .error(R.mipmap.person) // 错误图，图片加载失败时显示
                    .into(((Item2ViewHolder) holder).avatar); // 将图片设置到ImageView中
            ((Item2ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LeaderSmallActivity.class);
                    intent.putExtra("uid", String.valueOf(datas.get(position).getUid()));
                    intent.putExtra("name", datas.get(position).getName());
                    intent.putExtra("image", datas.get(position).getImgSrc());
//                intent.putString("data", data);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_fraction, name, which;
        ImageView avatar;

        public Item2ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.touxiang);
            textView_fraction = itemView.findViewById(R.id.sentence_fraction);
            name = itemView.findViewById(R.id.name);
            which = itemView.findViewById(R.id.which);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView textView_fraction, name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.leader_user_image);
            textView_fraction = itemView.findViewById(R.id.sentence_fraction);
            name = itemView.findViewById(R.id.name);
        }

    }

}
