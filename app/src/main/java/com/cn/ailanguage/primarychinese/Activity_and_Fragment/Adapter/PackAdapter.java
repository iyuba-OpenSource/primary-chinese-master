package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.Bean.HistoryBean;
import com.cn.ailanguage.primarychinese.R;

import java.util.List;

public class PackAdapter extends RecyclerView.Adapter<PackAdapter.packViewHolder>{
    Context context;
    List<HistoryBean.DataBean> list;
//    @NonNull
    public PackAdapter(Context context,List<HistoryBean.DataBean> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public PackAdapter.packViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.small_pack, parent, false);
        return new PackAdapter.packViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull packViewHolder holder, int position) {
        holder.type.setText(list.get(position).getType());
        holder.amount.setText("+"+Float.parseFloat(list.get(position).getScore())/100);
        holder.time.setText(list.get(position).getTime().substring(0, list.get(position).getTime().length() - 5));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class packViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView amount;
        TextView type;
        public packViewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.time);
            amount=itemView.findViewById(R.id.amount);
            type=itemView.findViewById(R.id.type);
        }
    }
}
