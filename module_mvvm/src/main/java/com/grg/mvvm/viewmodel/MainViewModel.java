package com.grg.mvvm.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.grg.mvvm.model.Repository;
import com.lib.common.http.HttpHelper;
import com.lib.common.http.interfaces.ICallBack;

import java.util.List;

public class MainViewModel implements ViewModel {

    private static final String TAG = "MainViewModel";

    public ObservableInt progressVisibility;
    public ObservableInt searchButtonVisibility;
    public ObservableInt infoMessageVisibility;
    public ObservableInt recyclerViewVisibility;
    public ObservableField<String> infoMessage;

    private String editTextUsernameValue;
    private DataListener dataListener;


    private Context context;

    public MainViewModel(Context context,DataListener dataListener) {
        this.context = context;
        this.dataListener = dataListener;
        progressVisibility = new ObservableInt(View.INVISIBLE);
        infoMessageVisibility = new ObservableInt(View.VISIBLE);
        searchButtonVisibility = new ObservableInt(View.GONE);
        recyclerViewVisibility = new ObservableInt(View.GONE);
        infoMessage = new ObservableField<>("Enter a GitHub username above to see its repositories");
    }

    public TextWatcher getUsernameEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                editTextUsernameValue = charSequence.toString();
                searchButtonVisibility.set(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    public void onClickSearch(View view) {
        loadGithubRepos(editTextUsernameValue);
    }

    public boolean onSearchAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String username = view.getText().toString();
            if (username.length() > 0) loadGithubRepos(username);
            return true;
        }
        return false;
    }

    private void loadGithubRepos(String userName) {
        progressVisibility.set(View.VISIBLE);
        searchButtonVisibility.set(View.GONE);
        infoMessageVisibility.set(View.GONE);
        recyclerViewVisibility.set(View.GONE);
        HttpHelper.obtain().get("https://api.github.com/users/" + userName + "/repos", null, new ICallBack() {
            @Override
            public void onSuccess(String msg) {
                List<Repository> repositorys = JSON.parseArray(msg, Repository.class);
                if (repositorys.isEmpty()) {
                    infoMessage.set("This account doesn\'t have any public repository");
                    infoMessageVisibility.set(View.VISIBLE);
                } else {
                    dataListener.onRepositoriesChanged(repositorys);
                }
                progressVisibility.set(View.INVISIBLE);
                recyclerViewVisibility.set(View.VISIBLE);
            }

            @Override
            public void onFailed(String msg) {
                Log.e(TAG, "Error loading GitHub repos " + msg);
                infoMessage.set("Oops, Octocat doesn\'t know that username");
                infoMessageVisibility.set(View.VISIBLE);
                progressVisibility.set(View.INVISIBLE);
                recyclerViewVisibility.set(View.INVISIBLE);
            }
        });
    }

    @Override
    public void destroy() {
        context = null;
    }

    public interface DataListener {
        void onRepositoriesChanged(List<Repository> repositories);
    }

}
