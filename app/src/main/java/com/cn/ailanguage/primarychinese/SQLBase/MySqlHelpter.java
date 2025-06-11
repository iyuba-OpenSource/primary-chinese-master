package com.cn.ailanguage.primarychinese.SQLBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MySqlHelpter extends SQLiteOpenHelper {


    Context mContext;
    String name;
    public MySqlHelpter(@Nullable Context context) {
        super(context, "homebook1", null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER = "create table tablebookline ( " +
                "voaid text," +
                "unitId text," +
                "type text," +
                "Title text," +
                "DescCn text,"+
                "Title_cn text,"+
                "Category text,"+
                "Sound text,"+
                "Pic text,"+
                "CreatTime text,"+
                "flag text,"+
                "Series text)";
        db.execSQL(CREATE_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
