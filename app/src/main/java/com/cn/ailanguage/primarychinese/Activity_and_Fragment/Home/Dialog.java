package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.LoginActivity;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.OneClickLoginActivity;

public class Dialog {
    private Context context;
    private String constring,postring,nestring;
    public Dialog(Context context,String a,String b,String c){
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
                if(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.SecVerify.equals("true")){
                    Intent intent = new Intent(context, OneClickLoginActivity.class);
                    context.startActivity(intent);
                }else if(com.cn.ailanguage.primarychinese.Activity_and_Fragment.Constant.SecVerify.equals("false")){
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "出错啦~，请重新进入app", Toast.LENGTH_SHORT).show();
                }
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
