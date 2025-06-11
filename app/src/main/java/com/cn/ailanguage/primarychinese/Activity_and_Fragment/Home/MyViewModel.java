package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private MutableLiveData<Integer> scrollPosition = new MutableLiveData<>();

    public void setScrollPosition(int position) {
        scrollPosition.setValue(position);
    }

    public LiveData<Integer> getScrollPosition() {
        return scrollPosition;
    }
}
