package com.grg.mvvm;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grg.mvvm.databinding.ItemRepoBinding;
import com.grg.mvvm.model.Repository;
import com.grg.mvvm.viewmodel.ItemRepoViewModel;

import java.util.Collections;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {

    private List<Repository> repositories;

    public RepositoryAdapter() {
        this.repositories = Collections.emptyList();
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRepoBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_repo,
                parent, false);
        return new RepositoryViewHolder(binding);
    }

    public RepositoryAdapter(List<Repository> repositories) {
        this.repositories = repositories;
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }


    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        holder.bindRepository(repositories.get(position));
    }


    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {

        private ItemRepoBinding binding;

        public RepositoryViewHolder(ItemRepoBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }

        void bindRepository(Repository repository) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new ItemRepoViewModel(itemView.getContext(), repository));
            } else {
                binding.getViewModel().setRepository(repository);
            }
        }
    }
}
