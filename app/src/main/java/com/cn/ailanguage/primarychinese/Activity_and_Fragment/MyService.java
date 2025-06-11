package com.cn.ailanguage.primarychinese.Activity_and_Fragment;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.cn.ailanguage.primarychinese.Bean.AllWordBean;
import com.cn.ailanguage.primarychinese.Bean.BookBean;
import com.cn.ailanguage.primarychinese.Bean.DataBean;
import com.cn.ailanguage.primarychinese.Bean.MainBean;
import com.cn.ailanguage.primarychinese.Bean.PomeBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.View.BookContract;
import com.cn.ailanguage.primarychinese.View.HomeContract;
import com.cn.ailanguage.primarychinese.View.MainContract;
import com.cn.ailanguage.primarychinese.network.Book.BookNetWorkManager;
import com.cn.ailanguage.primarychinese.network.Home.NetWorkManager;
import com.cn.ailanguage.primarychinese.network.Main.MainNetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.BookPresenter;
import com.cn.ailanguage.primarychinese.presenter.HomePresenter;
import com.cn.ailanguage.primarychinese.presenter.MainPresenter;
import com.cn.ailanguage.primarychinese.util.MD5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyService extends Service implements  BookContract.BookView, HomeContract.HomeView, MainContract.MainView{
    private Thread backgroundThread;
    BookPresenter bookPresenter;
    HomePresenter homePresenter;
    MainPresenter mainPresenter;
    List<DataBean.InfoBean> dataList = new ArrayList<>();
    List<MainBean.InfoBean> list;
    //Num是书籍，numVoaid是书籍内容
    private int num = 466, numVoaid = 0;
    List<String> voaidID=new ArrayList<>();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //更新icon
        BookNetWorkManager.getInstance().init();
        bookPresenter = new BookPresenter();
        bookPresenter.attchView(this);
        //更新数据列表
        NetWorkManager.getInstance().init();
        homePresenter = new HomePresenter();
        homePresenter.attchView(this);
        //更新课文内容
        MainNetWorkManager.getInstance().init();
        mainPresenter = new MainPresenter();
        mainPresenter.attchView(this);
        //初始化数据库
        if (!isDatabaseExists("bookicon.db")) {
            copyDatabasebookicon();
        }
        if (!isDatabaseExists("text.db")) {
            copyDatabasetext();
        }
        if (!isDatabaseExists("allbookline.db")) {
            copyDatabaseallbookline();
        }

        // 在这里执行后台操作
        backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {

                File dbFile = getDatabasePath("text.db");
                dbFile.setReadable(true);
                dbFile.setWritable(true);
                // 获取对数据库的写入权限
                // 删除表中的所有数据
                SQLiteDatabase db = SQLiteDatabase.openDatabase(String.valueOf(dbFile), null, SQLiteDatabase.OPEN_READWRITE);
                db.delete("tablebookline", null, null);

                Date startDate = new Date(0);
                Date currentDate = new Date();
                long days = TimeUnit.MILLISECONDS.toDays(currentDate.getTime() - startDate.getTime());
                String sign = MD5.md5("iyuba" + days + "series");
                bookPresenter.getHome("category", 338, sign);

            }
        });
        backgroundThread.start();

        return START_STICKY;
    }
    private void copyDatabasebookicon() {
        try {

            InputStream inputStream = getAssets().open("bookicon.db");
            String outFileName =getDatabasePath("bookicon.db").getPath();
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
    private void copyDatabasetext() {
        try {

            InputStream inputStream = getAssets().open("text.db");
            String outFileName =getDatabasePath("text.db").getPath();
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
    private void copyDatabaseallbookline() {
        try {

            InputStream inputStream = getAssets().open("allbookline.db");
            String outFileName =getDatabasePath("allbookline.db").getPath();
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
    private boolean isDatabaseExists(String dbName) {
        File dbFile = getApplicationContext().getDatabasePath(dbName);
        return dbFile.exists();
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

    //总书籍更新
    @Override
    public void getHome(BookBean bookBean) {
        Toast.makeText(getApplicationContext(), "开始更新书籍", Toast.LENGTH_SHORT).show();
        List<BookBean.DataBean> list = bookBean.getData();
        AddIcon(list);
        homePresenter.getHome(num, "home");
    }
    private void AddIcon(List<BookBean.DataBean> data) {
        //创建MyHelper类
        File dbFile = getDatabasePath("bookicon.db");
        dbFile.setReadable(true);
        dbFile.setWritable(true);
        // 获取对数据库的写入权限
        SQLiteDatabase db = SQLiteDatabase.openDatabase(String.valueOf(dbFile), null, SQLiteDatabase.OPEN_READWRITE);
        // 删除表中的所有数据
        db.delete("tablebookline", null, null);
        //创建ContentValues对象存放数据
        for (int i = 0; i < data.size(); i++) {
            ContentValues values = new ContentValues();
            //添加数据
            values.put("Category", data.get(i).getCategory());
            values.put("CreateTime", data.get(i).getCreateTime());
            values.put("isVideo", data.get(i).getIsVideo());
            values.put("pic", data.get(i).getPic());
            values.put("KeyWords", data.get(i).getKeyWords());
            values.put("version", data.get(i).getVersion());
            values.put("DescCn", data.get(i).getDescCn());
            values.put("SeriesName", data.get(i).getSeriesName());
            values.put("UpdateTime", data.get(i).getUpdateTime());
            values.put("HotFlg", data.get(i).getHotFlg());
            values.put("haveMicro", data.get(i).getHaveMicro());
            values.put("Id", data.get(i).getId());
            //将values数据插入到表(data)中
            db.insert("tablebookline", null, values);
        }
        db.close();
    }

    //更新书籍目录
    @Override
    public void getHome(DataBean dataBean) {
        dataList.addAll(dataBean.getInfo());
        if (num != 478) {
            num++;
            homePresenter.getHome(num, "home");
        } else {
            num++;
            homePresenter.getHome(num, "home");
            AddBook(dataList);
            CombineVoaid();
        }

    }

    private void AddBook(List<DataBean.InfoBean> data) {
        File dbFile = getDatabasePath("allbookline.db");
        dbFile.setReadable(true);
        dbFile.setWritable(true);
        // 获取对数据库的写入权限
        SQLiteDatabase db = SQLiteDatabase.openDatabase(String.valueOf(dbFile), null, SQLiteDatabase.OPEN_READWRITE);
        // 删除表中的所有数据
        db.delete("tablebookline", null, null);
        for (int i = 0; i < data.size(); i++) {
            ContentValues values = new ContentValues();
            //添加数据
            values.put("voaid", data.get(i).getVoaid());
            values.put("unitId", data.get(i).getUnitId());
            values.put("type", data.get(i).getType());
            values.put("Title", data.get(i).getTitle());
            values.put("DescCn", data.get(i).getDescCn());
            values.put("Title_cn", data.get(i).getTitle_cn());
            values.put("Category", data.get(i).getCategory());
            values.put("Sound", data.get(i).getSound());
            values.put("Pic", data.get(i).getPic());
            values.put("CreatTime", data.get(i).getCreatTime());
            values.put("flag", data.get(i).getFlag());
            values.put("Series", data.get(i).getSeries());
            //将values数据插入到表(data)中
            db.insert("tablebookline", null, values);
        }
        db.close();
    }

    //更新书籍内容
    @Override
    public void getHome(MainBean mainBean) {
//        Log.d("xwh77", String.valueOf(dataList.get(numVoaid).getVoaid()));
        if(numVoaid<voaidID.size()){
            AddText(mainBean.getInfo());
            numVoaid++;
            mainPresenter.getHome("detail", voaidID.get(numVoaid));

        }
        if(numVoaid==voaidID.size()-2){
            Toast.makeText(getApplicationContext(), "书籍更新完毕", Toast.LENGTH_SHORT).show();
        }
        Log.d("xwh888", voaidID.size()+"0");

    }

    private void AddText(List<MainBean.InfoBean> data) {

        File dbFile = getDatabasePath("text.db");
        dbFile.setReadable(true);
        dbFile.setWritable(true);
        // 获取对数据库的写入权限
        SQLiteDatabase db = SQLiteDatabase.openDatabase(String.valueOf(dbFile), null, SQLiteDatabase.OPEN_READWRITE);
        // 删除表中的所有数据
//        db.delete("tablebookline", null, null);
//        创建ContentValues对象存放数据
        for (int i = 0; i < data.size(); i++) {
            Log.d("xwh89", data.size() + ")");
            ContentValues values = new ContentValues();
            //添加数据
            values.put("voaid", data.get(i).getVoaid());
            values.put("ParaId", data.get(i).getParaId());
            values.put("IdIndex", data.get(i).getIdIndex());
            values.put("Timing", data.get(i).getTiming());
            values.put("EndTiming", data.get(i).getEndTiming());
            values.put("Sentence", data.get(i).getSentence());
            values.put("Position", data.get(i).getPosition());
            values.put("ImgPath", data.get(i).getImgPath());
            values.put("SentencePhonetic", data.get(i).getSentencePhonetic());
            //将values数据插入到表(data)中
            db.insert("tablebookline", null, values);
        }
        db.close();
    }
    private void CombineVoaid() {
        // 打开数据库连接
        SQLiteDatabase db = SQLiteDatabase.openDatabase(getDatabasePath("allbookline.db").getPath(), null, SQLiteDatabase.OPEN_READWRITE);
// 执行查询操作
        String query = "SELECT * FROM tablebookline";
//        String[] selectionArgs = {String.valueOf(voaid)};
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
        voaidID=new ArrayList<>();
        for (int i = 0; i < columnDataLists.get(0).size(); i++) {
            voaidID.add(columnDataLists.get(0).get(i));
        }

        cursor.close();
        db.close();
        mainPresenter.getHome("detail", voaidID.get(numVoaid));

    }
    @Override
    public void getAllWord(AllWordBean allWordBean) {

    }


    @Override
    public void getWord(WordBean wordBean) {

    }

    @Override
    public void getPome(PomeBean pomeBean) {

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       Toast.makeText(getApplicationContext(), "书籍更新完毕", Toast.LENGTH_SHORT).show();
        // 在服务销毁时停止线程
        if (backgroundThread != null) {
            backgroundThread.interrupt();
        }
    }
}
