package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.Bean.DataBean;
import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.View.MainContract;
import com.cn.ailanguage.primarychinese.network.Home.Constant;
import com.cn.ailanguage.primarychinese.network.Main.MainNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.MainPresenter;

import java.io.IOException;
import java.util.List;

public class WordHomeAdapter extends RecyclerView.Adapter<WordHomeAdapter.WordHomeViewHolder> {
    private List<DataBean.InfoBean> bookmore;
    private Context context;
    private WordBean wordBean;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public WordHomeAdapter(Context context, List<DataBean.InfoBean> words, WordBean wordBean) {
        this.bookmore = words;
        this.context = context;
        this.wordBean = wordBean;
    }

    @Override
    public WordHomeAdapter.WordHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_word_small, parent, false);
        return new WordHomeAdapter.WordHomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHomeAdapter.WordHomeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.num.setText(" 第" + String.valueOf(position + 1) + "课  ");
        holder.total.setText(" 共" + String.valueOf(wordBean.getData().get(position).getWords().size()) + "个字");
        //匹配标题，课文等
        int n = position;
        while (true && n<bookmore.size()) {
            if (!bookmore.get(n).getVoaid().equals(wordBean.getData().get(position).getVoaid())) {
                n++;
            } else {
                String name = bookmore.get(n).getTitle();
                if (name.length() > 7) {
                    holder.textName.setText(name.substring(0, 7) + "...");
                } else {
                    holder.textName.setText(name);
                }
                break;
            }
        }
        //绑定adapter
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 6); // 创建一个每行显示6个 item 的 GridLayoutManager
        holder.recyclerView.setLayoutManager(gridLayoutManager);
        WordHomeSmallAdapter adapter = new WordHomeSmallAdapter(context, wordBean.getData().get(position).getWords()); // 创建适配器，dataList 是数据列表
        holder.recyclerView.setAdapter(adapter); // 设置适配器;
        //拦截子级adapter的点击事件
        //点击出现弹窗
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.VOAIDNAME= String.valueOf(holder.textName.getText());
                Constant.WORDBEAN = wordBean.getData().get(position).getWords();
                //回调给wordActivity,出现三大模块弹窗
                mItemClickListener.onItemClick(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return wordBean.getData().size();
    }


    public class WordHomeViewHolder extends RecyclerView.ViewHolder {
        TextView num, total, textName;
        RecyclerView recyclerView;
        LinearLayout linearLayout;
        ImageView more;

        WordHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.num_text);
            total = itemView.findViewById(R.id.total_text);
            textName = itemView.findViewById(R.id.name_text);
            recyclerView = itemView.findViewById(R.id.recycle_word);
            linearLayout = itemView.findViewById(R.id.liney);
            more = itemView.findViewById(R.id.more);
            // 为需要响应点击事件的 View 设置点击监听器

        }
    }


}
