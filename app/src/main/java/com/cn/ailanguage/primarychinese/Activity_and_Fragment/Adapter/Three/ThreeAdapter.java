package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.Three;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.R;

import java.io.IOException;
import java.util.List;

public class ThreeAdapter extends RecyclerView.Adapter<ThreeAdapter.ThreeViewHolder> {
    private WordBean.DataBean.WordsBean words;
    private Context context;
    List<String> list;
    private int ischoose = -1;

    public int isIschoose() {
        return ischoose;
    }

    public void setIschoose(int ischoose) {
        this.ischoose = ischoose;
    }

    public ThreeAdapter.CallBack getCallBack(ThreeAdapter.CallBack callBack) {
        return ThreeAdapter.callBack;
    }

    public void setCallBack(ThreeAdapter.CallBack callBack) {
        this.callBack = callBack;
    }

    private static ThreeAdapter.CallBack callBack;     //最后得callback接口
    private MediaPlayer mediaPlayer;
    private int which, white_item, flag = 1;

    public ThreeAdapter(Context context, List<String> list, WordBean.DataBean.WordsBean words, int which) {
        this.context = context;
        this.list = list;
        this.words = words;
        this.which = which;
    }

    @Override
    public ThreeAdapter.ThreeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.three_adapetr, parent, false);
        return new ThreeAdapter.ThreeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreeAdapter.ThreeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.word.setText(list.get(position));
        if(flag==0){
            if (position == ischoose && position == which) {
                //选到了正确的
                holder.word.setBackgroundResource(R.drawable.word_adapter_right);
                Constant.RIGHT++;
            }
            else{
                //选到了错误的
                if(position==which){
                    holder.word.setBackgroundResource(R.drawable.word_adapter_right);
                }
                if(position==ischoose){
                    holder.word.setBackgroundResource(R.drawable.word_adapter_wrong);
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ThreeViewHolder extends RecyclerView.ViewHolder {
        TextView word;

        ThreeViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.word);
            word.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //代表点击过item了
                    if(flag==1){
                        if (callBack != null) {
                            try {

                                callBack.clickItem(getAdapterPosition());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        flag=0;
                    }

                }
            });
        }


    }

    public interface CallBack {
        void clickItem(int position) throws IOException;
    }

}
