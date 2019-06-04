package com.grg.main.viewmodel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.view.View;

import com.grg.main.BuildConfig;
import com.lib.common.base.BaseViewModel;

public class HomeViewModel implements BaseViewModel{

    private Context context;

    public ObservableInt idCardVisibility;
    public ObservableInt faceVisibility;
    public ObservableInt mvvmVisibility;

    public HomeViewModel(Context context){
        this.context = context;
        faceVisibility = new ObservableInt(BuildConfig.isNeedFace ? View.VISIBLE : View.GONE);
        idCardVisibility = new ObservableInt(BuildConfig.isNeedCard ? View.VISIBLE : View.GONE);
        mvvmVisibility = new ObservableInt(BuildConfig.isNeedMvvm ? View.VISIBLE : View.GONE);
    }


    @Override
    public void destroy() {
        context= null;
    }
}
