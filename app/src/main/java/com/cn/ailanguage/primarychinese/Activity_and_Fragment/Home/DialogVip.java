package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.LoginActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need.BuyActivity;

public class DialogVip {
    private Context context;
    private String constring,postring,nestring;
    public DialogVip(Context context, String a, String b, String c){
        this.context=context;
        constring=a;
        postring=b;
        nestring=c;

    }
    public void ShowDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示")
                .setMessage(constring);  // 设置对话框标题

// 添加确定按钮
        builder.setPositiveButton(postring, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 在点击确定按钮后执行的操作
                // 可以在这里处理用户的操作或关闭对话框
                Intent intent=new Intent(context, BuyActivity.class);
                context.startActivity(intent);
                dialogInterface.dismiss();  // 关闭对话框
            }
        });

        builder.setNegativeButton(nestring, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                ((Activity) context).finish();
            }
        });

        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
