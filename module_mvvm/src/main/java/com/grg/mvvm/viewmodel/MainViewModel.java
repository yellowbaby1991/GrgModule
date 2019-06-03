package com.grg.mvvm.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.lib.common.http.HttpHelper;
import com.lib.common.http.interfaces.ICallBack;

public class MainViewModel implements ViewModel{

    public ObservableInt progressVisibility;
    public ObservableInt searchButtonVisibility;
    public ObservableField<String> infoMessage;

    private String editTextUsernameValue;


    private Context context;

    public MainViewModel(Context context){
        this.context = context;
        progressVisibility = new ObservableInt(View.INVISIBLE);
        searchButtonVisibility = new ObservableInt(View.GONE);
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

    public void onClickSearch(View view){
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
        progressVisibility.set(View.INVISIBLE);
        searchButtonVisibility.set(View.GONE);
        HttpHelper.obtain().get("https://api.github.com/users/yellowbaby/repos", null, new ICallBack() {
            @Override
            public void onSuccess(String msg) {

            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    @Override
    public void destroy() {
        context = null;
    }
}
