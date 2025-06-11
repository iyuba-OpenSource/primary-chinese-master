package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Person.option_need;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareData {
    private SharedPreferences sharedPreferences;
    public ShareData(Context context){
        sharedPreferences=context.getSharedPreferences("ShareData",Context.MODE_PRIVATE);}
    public void saveData(String key,String value){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String loadData(String key,String defaultValue){
        return sharedPreferences.getString(key,defaultValue);
    }
}
