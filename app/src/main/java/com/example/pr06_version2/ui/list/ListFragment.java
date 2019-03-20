package com.example.pr06_version2.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pr06_version2.R;
import com.example.pr06_version2.base.OnUserClickListener;
import com.example.pr06_version2.data.local.Database;
import com.example.pr06_version2.data.model.User;
import com.example.pr06_version2.databinding.FragmentListBinding;
import com.example.pr06_version2.ui.main.MainActivityViewModel;
import com.example.pr06_version2.ui.main.MainActivityViewModelFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;

public class ListFragment extends Fragment {

    private FragmentListBinding b;
    private MainActivityViewModel viewModel;
    private ListFragmentAdapter listAdapter;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        viewModel = ViewModelProviders.of(requireActivity(), new MainActivityViewModelFactory(Database.getInstance())).get(MainActivityViewModel.class);
        setupRecyclerView();
        observeUsers();
        setupViews();
    }

    private void setupRecyclerView() {
        listAdapter = new ListFragmentAdapter(new OnUserClickListener() {
            @Override
            public void onEditUser(User user) {
                viewModel.getAvatarLiveData().setValue(null);
                navController.navigate(ListFragmentDirections.actionListFragmentToDetailFragment().setUser(user));
            }

            @Override
            public void onDeleteUser(User user) {
                viewModel.deleteUser(user);
            }
        });
        b.listUsers.setAdapter(listAdapter);
        b.listUsers.setHasFixedSize(true);
        b.listUsers.setItemAnimator(new DefaultItemAnimator());
    }

    private void observeUsers() {
        viewModel.getUsers().observe(this, users -> {
            b.lblEmptyView.setVisibility(users.size() == 0 ? View.VISIBLE : View.INVISIBLE);
            listAdapter.submitList(users);
        });
    }

    private void setupViews() {
        b.lblEmptyView.setOnClickListener(v -> {
            viewModel.getAvatarLiveData().setValue(null);
            navController.navigate(R.id.action_listFragment_to_detailFragment);
        });
        b.fab.setOnClickListener(v -> {
            viewModel.getAvatarLiveData().setValue(null);
            navController.navigate(R.id.action_listFragment_to_detailFragment);
        });
    }

}
