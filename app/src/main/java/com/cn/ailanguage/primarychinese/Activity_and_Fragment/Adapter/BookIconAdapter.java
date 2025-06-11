package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.Bean.BookBean;


import java.util.List;

public class BookIconAdapter extends RecyclerView.Adapter<BookIconAdapter.BookViewHolder> {

    private List<List<String>> dates;
    private Context context;
public interface OnItemClickListener {
    void onItemClick(String data, String seriesName);
}

    private OnItemClickListener listener;
    public BookIconAdapter(List<List<String>> dates, Context context,OnItemClickListener listener) {
        this.dates = dates;
        this.context = context;
        this.listener = listener;
    }

    public List<List<String>> getDates() {
        return dates;
    }

    public void setDates(List<List<String>>  dates) {
        this.dates = dates;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.small_book_icon_activity, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookIconAdapter.BookViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //图片和名字，小学语文和封面
        Glide.with(context).load(dates.get(3).get(position)).into(holder.book_icon_image);
        holder.book_icon_title.setText(dates.get(7).get(position).substring(4));

        //点击图片
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    //回调接口,没用到
                    listener.onItemClick(dates.get(11).get(position),dates.get(7).get(position));
                }
                SharedPreferences sharedPreferences = context.getSharedPreferences("bookicon", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("seriesname", dates.get(7).get(position));
                editor.putString("pic",dates.get(11).get(position));
                editor.commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return dates == null ? 0 : dates.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView book_icon_title;
        ImageView book_icon_image;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            book_icon_title = itemView.findViewById(R.id.book_icon_title);
            book_icon_image=itemView.findViewById(R.id.book_icon_image);
        }
    }
}
