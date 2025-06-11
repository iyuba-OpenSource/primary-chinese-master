package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.ailanguage.primarychinese.Bean.PomeBean;
import com.cn.ailanguage.primarychinese.Bean.WordBean;
import com.cn.ailanguage.primarychinese.R;
import com.cn.ailanguage.primarychinese.Activity_and_Fragment.Adapter.HomeAdapter;
import com.cn.ailanguage.primarychinese.Bean.DataBean;

import com.cn.ailanguage.primarychinese.View.HomeContract;
import com.cn.ailanguage.primarychinese.network.Home.Constant;
import com.cn.ailanguage.primarychinese.network.Home.NetWorkManager;
import com.cn.ailanguage.primarychinese.presenter.HomePresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment implements View.OnClickListener, HomeContract.HomeView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View rootView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    int flag = 1;
    private String mParam2, category = "466";
    public static List<List<String>> home_data;
    RecyclerView showLv;
    private TextView textView;
    private HomePresenter homePresenter;
    private ImageView pic;
    private ImageView book;


    LinearLayoutManager linearLayoutManager;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        textView = rootView.findViewById(R.id.text);

        SharedPreferences sharedPreferencesbook = getContext().getSharedPreferences("bookicon", Context.MODE_PRIVATE);
        String bookname = sharedPreferencesbook.getString("bookname", "小学语文一年级上");
        category = sharedPreferencesbook.getString("category", "466");
        textView.setText(bookname);
        //变色
        Window window = getActivity().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.homecolor));
        }

        book=rootView.findViewById(R.id.which_book);
        //点击选书
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BookIconActivity.class);
                startActivity(intent);
            }
        });
        //绑定adapter
        showLv = rootView.findViewById(R.id.homeRv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        showLv.setNestedScrollingEnabled(false);
        showLv.setLayoutManager(linearLayoutManager);
        //判断有没有联网
        boolean isConnected = isNetworkConnected(getContext());
//        boolean isConnected = isNetSystemUsable(getContext());

        if (isConnected) {
            // 如果已经联网
            NetWorkManager.getInstance().init();
            homePresenter = new HomePresenter();
            homePresenter.attchView(this);
            homePresenter.getHome(Integer.parseInt(category), "home");
        } else {
            Log.e("xwh090", "noNet data " );
            // 执行与无网络连接相关的操作
            Combine();
        }
        return rootView;
    }

    public static boolean isNetSystemUsable(Context context) {
        boolean isNetUsable = false;
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NetworkCapabilities networkCapabilities =
                    manager.getNetworkCapabilities(manager.getActiveNetwork());

            if (networkCapabilities != null) {
                isNetUsable = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            }
        }
        return true;
//        return isNetUsable;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
    private boolean isDatabaseExists(String dbName) {
        //判断之前是不是读取过这个数据库
        File dbFile = getContext().getApplicationContext().getDatabasePath(dbName);
        return dbFile.exists();
    }

    private void Combine() {
        //数据数据库，没有联网的时候
        if (!isDatabaseExists("allbookline.db")) {
            copyDatabase(category);
        }
        // 打开数据库连接
        SQLiteDatabase db = SQLiteDatabase.openDatabase(getContext().getDatabasePath("allbookline.db").getPath(), null, SQLiteDatabase.OPEN_READWRITE);
// 执行查询操作
        String query = "SELECT * FROM tablebookline WHERE series = ?";
        String[] selectionArgs = {String.valueOf(category)};
        Cursor cursor = db.rawQuery(query, selectionArgs);
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
        home_data = columnDataLists;
        cursor.close();
        db.close();
        HomeAdapter adapter = new HomeAdapter(columnDataLists, getContext());
        showLv.setAdapter(adapter);
    }

    private void copyDatabase(String name) {
        //读取数据
        try {
            InputStream inputStream = getContext().getAssets().open("allbookline.db");
            String outFileName = getContext().getDatabasePath("allbookline.db").getPath();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View v) {

    }

    //选哪本书，获得参数466等
    @Override
    public void onResume() {
        if (Constant.CHANGEBOOKHOME == 1) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("bookicon", Context.MODE_PRIVATE);
            category = sharedPreferences.getString("category", "466");
            String bookname = sharedPreferences.getString("bookname", "小学语文一年级上");
            boolean isConnected = isNetworkConnected(getContext());
            if (isConnected) {
                // 设备已联网
                homePresenter.getHome(Integer.parseInt(category), "home");
            } else {
                // 设备未联网
                Combine();
            }
            textView.setText(bookname);
            Constant.CHANGEBOOKHOME = 0;
        }
        super.onResume();
    }

    //书籍内容，目录
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getHome(DataBean dataBean) {
        List<DataBean.InfoBean> dataList = dataBean.getInfo();
        List<List<String>> listOfLists = dataList.stream()
                .map(infoBean -> {
                    List<String> innerList = new ArrayList<>();
                    innerList.add(infoBean.getVoaid()); // 假设 getInfoString() 是获取字符串的方法
                    innerList.add(infoBean.getUnitId());
                    innerList.add(infoBean.getType());
                    innerList.add(infoBean.getTitle());
                    innerList.add(infoBean.getDescCn());
                    innerList.add(infoBean.getTitle_cn());
                    innerList.add(infoBean.getCategory());
                    innerList.add(infoBean.getSound());
                    innerList.add(infoBean.getPic());
                    innerList.add(infoBean.getCreatTime());
                    innerList.add(infoBean.getFlag());
                    innerList.add(infoBean.getSeries());
                    // 如果 InfoBean 中还有其他需要添加到内部列表的字段，可以在这里继续添加
                    return innerList;
                })
                .collect(Collectors.toList());
        home_data = new ArrayList<>();
        List<List<String>> transposedList = transposeList(listOfLists);
        home_data.addAll(transposedList);
        HomeAdapter adapter = new HomeAdapter(home_data, getContext());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sound", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sound", "http://staticvip.iyuba.cn/sounds/voa" + dataList.get(0).getSound());
        editor.commit();
        Constant.VOAID = dataBean.getInfo().get(0).getVoaid();
        showLv.setAdapter(adapter);

    }

    //转置
    public static List<List<String>> transposeList(List<List<String>> originalList) {
        List<List<String>> transposedList = new ArrayList<>();

        // 获取原始列表的行数和列数
        int rows = originalList.size();
        int cols = originalList.get(0).size();

        // 遍历列，构建转置后的列表
        for (int i = 0; i < cols; i++) {
            List<String> newRow = new ArrayList<>();
            for (int j = 0; j < rows; j++) {
                newRow.add(originalList.get(j).get(i));
            }
            transposedList.add(newRow);
        }

        return transposedList;
    }

    @Override
    public void getWord(WordBean wordBean) {

    }

    @Override
    public void getPome(PomeBean pomeBean) {

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