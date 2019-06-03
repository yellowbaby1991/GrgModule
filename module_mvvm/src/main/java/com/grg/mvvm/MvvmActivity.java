package com.grg.mvvm;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.grg.mvvm.databinding.ActivityMvvmBinding;
import com.grg.mvvm.model.Repository;
import com.grg.mvvm.model.User;
import com.grg.mvvm.viewmodel.MainViewModel;

import java.util.List;
import java.util.Random;

@Route(path = "/mvvm/activity")
public class MvvmActivity extends AppCompatActivity implements MainViewModel.DataListener {

    private ActivityMvvmBinding mBinding;

    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);
        mMainViewModel =new MainViewModel(this,this);
        mBinding.setViewModel(mMainViewModel);
        setSupportActionBar(mBinding.toolbar);
        setupRecyclerView(mBinding.reposRecyclerView);
        ARouter.getInstance().inject(this);

    }

    private void setupRecyclerView(RecyclerView reposRecyclerView) {
        RepositoryAdapter repositoryAdapter = new RepositoryAdapter();
        reposRecyclerView.setAdapter(repositoryAdapter);
        reposRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onRepositoriesChanged(List<Repository> repositories) {
        RepositoryAdapter repositoryAdapter = (RepositoryAdapter) mBinding.reposRecyclerView.getAdapter();
        repositoryAdapter.setRepositories(repositories);
        repositoryAdapter.notifyDataSetChanged();
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mBinding.editTextUsername.getWindowToken(), 0);
    }
}
