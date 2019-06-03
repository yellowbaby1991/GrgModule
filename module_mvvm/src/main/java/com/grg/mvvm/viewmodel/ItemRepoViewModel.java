package com.grg.mvvm.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.grg.mvvm.RepositoryActivity;
import com.grg.mvvm.model.Repository;

public class ItemRepoViewModel extends BaseObservable implements ViewModel {

    private Context context;
    private Repository repository;

    public ItemRepoViewModel(Context context, Repository repository) {
        this.context = context;
        this.repository = repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
        notifyChange();
    }

    public String getName() {
        return repository.name;
    }

    public String getDescription() {
        return repository.description;
    }

    public String getForks() {
        return repository.forks + " Forks";
    }

    public String getWatchers() {
        return repository.watchers + "  Watchers";
    }

    public String getStars() {
        return repository.watchers + "  Stars";
    }

    public void onItemClick(View view) {
        context.startActivity(RepositoryActivity.newIntent(context, repository));
    }

    @Override
    public void destroy() {

    }
}
