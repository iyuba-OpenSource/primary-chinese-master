package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home;

import static com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home.HomeFragment.transposeList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.ailanguage.primarychinese.util.MD5;
import com.cn.ailanguage.primarychinese.Bean.AllWordBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.BookIconAdapter;
import com.cn.ailanguage.primarychinese.Bean.BookBean;

import com.cn.ailanguage.primarychinese.SQLBase.MySqlHelpter;
import com.cn.ailanguage.primarychinese.View.BookContract;
import com.cn.ailanguage.primarychinese.network.Book.BookNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Home.Constant;
import com.cn.ailanguage.primarychinese.presenter.BookPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BookIconActivity extends AppCompatActivity implements View.OnClickListener, BookIconAdapter.OnItemClickListener, BookContract.BookView {
    private ImageView back_icon;
    private RecyclerView show;
    private MySqlHelpter mySQLiteOpenHelper;

    private SQLiteDatabase db;

    private BookPresenter bookPresenter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_icon_activity);

        Log.e("qxy", "connect: "  );

        back_icon = findViewById(R.id.back_icon);

//数据库连接
        mySQLiteOpenHelper = new MySqlHelpter(this);
        db = mySQLiteOpenHelper.getWritableDatabase();
        //绑定布局，一行3本
        show = findViewById(R.id.recycler_icon);
        show.setLayoutManager(new GridLayoutManager(this, 3));

        // 将系统状态栏设为指定的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.homecolor));
        }
        //点击返回事件
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭界面跳转
                finish();
            }
        });
        //
        //boolean isConnected = isNetSystemUsable(this);
        boolean isConnected = isNetworkConnected(this);
        if (isConnected) {
            Log.e("qxy", "connect: " + isConnected );
            //联网，获取书本图片名字
            BookNetWorkManager.getInstance().init();
            bookPresenter = new BookPresenter();
            bookPresenter.attchView(this);
            Date startDate = new Date(0);
            Date currentDate = new Date();
            long days = TimeUnit.MILLISECONDS.toDays(currentDate.getTime() - startDate.getTime());
            String sign = MD5.md5("iyuba" + days + "series");
            bookPresenter.getHome("category", 338, sign);
        } else {
            // 设备未联网，从数据库中获取 我试过了 就是我把高的放平台
            Combine();
        }

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }


    private void Combine() {
        if (!isDatabaseExists("bookicon.db")) {
            copyDatabase();
        }
        // 打开数据库连接
        SQLiteDatabase db = SQLiteDatabase.openDatabase(getDatabasePath("bookicon.db").getPath(), null, SQLiteDatabase.OPEN_READWRITE);
// 执行查询操作
        String query = "SELECT * FROM tablebookline";
        Cursor cursor = db.rawQuery(query, null);
        int columnCount = cursor.getColumnCount();

        List<List<String>> columnDataLists = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            columnDataLists.add(new ArrayList<>());
        }
        while (cursor.moveToNext()) {
            for (int i = 0; i < columnCount; i++) {
                String columnValue = cursor.getString(i);
                columnDataLists.get(i).add(columnValue);
            }
        }
        cursor.close();
        db.close();
        BookIconAdapter adapters = new BookIconAdapter(columnDataLists, this, this);
        show.setAdapter(adapters);
    }
    //读取数据库数据
    private void copyDatabase() {
        try {

            InputStream inputStream = getAssets().open("bookicon.db");
            String outFileName = getDatabasePath("bookicon.db").getPath();
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //判断有没有数据库
    private boolean isDatabaseExists(String dbName) {
        File dbFile = getApplicationContext().getDatabasePath(dbName);
        return dbFile.exists();
    }

    @Override
    public void onItemClick(String data, String name) {
        SharedPreferences sharedPreferences = getSharedPreferences("bookicon", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("category", data);
        editor.putString("bookname", name);
        //书本变了，首页数据，古诗和生词tab都要变
        Constant.CHANGEBOOK = 1;
        Constant.CHANGEBOOKHOME = 1;
        Constant.CHANGEBOOKPOME = 1;
        editor.apply();
        boolean isConnected = isNetworkConnected(this);
        if (isConnected) {
            //获取新的全部生词
            bookPresenter.getAllWord(data, "all");
        }
        back_icon.performClick();
    }


    @Override
    public void onClick(View v) {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getHome(BookBean bookBean) {
        //采用下面的数据处理是为了和没有联网的adapter适配，他们是一个adapter
        List<BookBean.DataBean> list = bookBean.getData();
        List<List<String>> listOfLists = list.stream()
                .map(infoBean -> {
                    List<String> innerList = new ArrayList<>();
                    innerList.add(infoBean.getCategory()); // 假设 getInfoString() 是获取字符串的方法
                    innerList.add(infoBean.getCreateTime());
                    innerList.add(infoBean.getIsVideo());
                    innerList.add(infoBean.getPic());
                    innerList.add(infoBean.getKeyWords());
                    innerList.add(infoBean.getVersion());
                    innerList.add(infoBean.getDescCn());
                    innerList.add(infoBean.getSeriesName());
                    innerList.add(infoBean.getUpdateTime());
                    innerList.add(infoBean.getHotFlg());
                    innerList.add(infoBean.getHaveMicro());
                    innerList.add(infoBean.getId());
                    // 如果 InfoBean 中还有其他需要添加到内部列表的字段，可以在这里继续添加
                    return innerList;
                })
                .collect(Collectors.toList());
        List<List<String>> transposedList = transposeList(listOfLists);
        BookIconAdapter adapters = new BookIconAdapter(transposedList, this, this);
        show.setAdapter(adapters);
    }


    @Override
    public void getAllWord(AllWordBean allWordBean) {

    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

    }
}
