package com.grg.mvvm.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.grg.mvvm.model.Repository;
import com.grg.mvvm.model.User;
import com.lib.common.base.BaseViewModel;
import com.lib.common.http.HttpHelper;
import com.lib.common.http.interfaces.ICallBack;

import java.util.List;

/**
 * ViewModel for the RepositoryActivity
 */
public class RepositoryViewModel implements BaseViewModel {

    private static final String TAG = "RepositoryViewModel";

    private Repository repository;
    private Context context;

    public ObservableField<String> ownerName;
    public ObservableField<String> ownerEmail;
    public ObservableField<String> ownerLocation;
    public ObservableInt ownerEmailVisibility;
    public ObservableInt ownerLocationVisibility;
    public ObservableInt ownerLayoutVisibility;

    public RepositoryViewModel(Context context, final Repository repository) {
        this.repository = repository;
        this.context = context;
        this.ownerName = new ObservableField<>();
        this.ownerEmail = new ObservableField<>();
        this.ownerLocation = new ObservableField<>();
        this.ownerLayoutVisibility = new ObservableInt(View.INVISIBLE);
        this.ownerEmailVisibility = new ObservableInt(View.VISIBLE);
        this.ownerLocationVisibility = new ObservableInt(View.VISIBLE);
        loadFullUser(repository.owner.url);
    }

    public String getDescription() {
        return repository.description;
    }

    public String getHomepage() {
        return repository.homepage;
    }

    public int getHomepageVisibility() {
        return repository.hasHomepage() ? View.VISIBLE : View.GONE;
    }

    public String getLanguage() {
        return "Language:" +  repository.language;
    }

    public int getLanguageVisibility() {
        return repository.hasLanguage() ? View.VISIBLE : View.GONE;
    }

    public int getForkVisibility() {
        return repository.isFork() ? View.VISIBLE : View.GONE;
    }

    public String getOwnerAvatarUrl() {
        return repository.owner.avatar_url;
    }

    @Override
    public void destroy() {
        this.context = null;
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }

    private void loadFullUser(String url) {
        HttpHelper.obtain().get(url, null, new ICallBack() {
            @Override
            public void onSuccess(String msg) {
                User user = JSON.parseObject(msg,User.class);
                ownerName.set(user.name);
                ownerEmail.set(user.email);
                ownerLocation.set(user.location);
                ownerEmailVisibility.set(user.hasEmail() ? View.VISIBLE : View.GONE);
                ownerLocationVisibility.set(user.hasLocation() ? View.VISIBLE : View.GONE);
                ownerLayoutVisibility.set(View.VISIBLE);
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }
}
