package com.grg.idcard.viewmodel;

import android.content.Context;

import com.lib.common.base.BaseViewModel;

public class IdCardViewModel implements BaseViewModel{

    private Context context;

    public IdCardViewModel(Context context){
        this.context = context;
    }

    @Override
    public void destroy() {

    }
}
