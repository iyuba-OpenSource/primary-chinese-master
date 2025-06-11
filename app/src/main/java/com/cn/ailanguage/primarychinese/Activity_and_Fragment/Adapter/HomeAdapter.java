package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.OriDetail.OriDetailActivity;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.homeViewHolder> {


    private List<List<String>> dates;
    private Context context;
    private int mSelectedPosition = RecyclerView.NO_POSITION;
    public HomeAdapter(List<List<String>> dates, Context context) {
        this.dates = dates;
        this.context = context;
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_homeview, parent, false);
        return new homeViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull homeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        List<String> daily = dates.get(3);
        if(daily.get(position).length()>9){
            holder.spokendetailText1.setText(String.valueOf(daily.get(position)).substring(0, 9) + "...");
        }else{
            holder.spokendetailText1.setText(daily.get(position));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dates.size()!=0){
                    //获取给textfragment的数据
                    Constant.MUSIC_ING=position;
                    Constant.MUSIC_MAX=getItemCount();
                    Intent intent = new Intent(context, OriDetailActivity.class);
                    String message = "http://staticvip.iyuba.cn/sounds/voa"+dates.get(7).get(position);
                    com.cn.ailanguage.primarychinese.network.Main.Constant.MUSIC=message;
                    com.cn.ailanguage.primarychinese.network.Main.Constant.VOAID=dates.get(0).get(position);
                    com.cn.ailanguage.primarychinese.network.Main.Constant.TITLE= dates.get(3).get(position);
                    com.cn.ailanguage.primarychinese.network.Main.Constant.POMEAUTHOR=dates.get(5).get(position);
                    com.cn.ailanguage.primarychinese.network.Home.Constant.POEM= Integer.parseInt(dates.get(10).get(position));

                    context.startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "没有缓存数据", Toast.LENGTH_SHORT).show();

                }
                }
        });

    }
    @Override
    public int getItemCount() {
        return dates == null ? 0 : dates.get(0).size();
    }

    class homeViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {


        TextView spokendetailText1;


        public homeViewHolder(@NonNull View itemView) {
            super(itemView);

            spokendetailText1 = itemView.findViewById(R.id.text_view);
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
