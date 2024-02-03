package com.craftsman.management.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.craftsman.management.app.R;
import com.craftsman.management.app.databinding.FragmentHomeBinding;
import com.craftsman.management.app.models.User;
import com.craftsman.management.app.persenters.category.CategoriesCallback;
import com.craftsman.management.app.persenters.category.CategoriesPresenter;
import com.craftsman.management.app.persenters.service.ServicesCallback;
import com.craftsman.management.app.persenters.service.ServicesPresenter;
import com.craftsman.management.app.persenters.user.UsersCallback;
import com.craftsman.management.app.persenters.user.UsersPresenter;
import com.craftsman.management.app.utilities.ToastUtils;
import com.craftsman.management.app.utilities.helpers.StorageHelper;

public class HomeFragment extends Fragment implements UsersCallback, CategoriesCallback, ServicesCallback {
    private FragmentHomeBinding binding;
    private UsersPresenter usersPresenter;
    private ServicesPresenter servicesPresenter;
    private CategoriesPresenter categoriesPresenter;
    private User currentUser;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersPresenter = new UsersPresenter(this);
        servicesPresenter = new ServicesPresenter(this);
        categoriesPresenter = new CategoriesPresenter(this);
        currentUser = StorageHelper.getCurrentUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        usersPresenter.getUsersCount();
        servicesPresenter.count();
        categoriesPresenter.count();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        binding.username.setText(getString(R.string.str_welcome_message, currentUser.getFullName()));
        return binding.getRoot();
    }

    @Override
    public void onGetUsersCountComplete(long adminCount, long craftsCount, long clientsCount) {
        UsersCallback.super.onGetUsersCountComplete(adminCount, craftsCount, clientsCount);
        binding.adminsCounter.setText(adminCount + "");
        binding.craftsmanCounter.setText(craftsCount + "");
        binding.clientsCounter.setText(clientsCount + "");
    }

    @Override
    public void onGetServicesCountComplete(long count) {
        binding.servicesCounter.setText(count + "");
    }

    @Override
    public void onGetCategoriesCountComplete(long count) {
        binding.categoriesCounter.setText(count + "");
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        ToastUtils.longToast(message);
    }

    @Override
    public void onShowLoading() {
        ProgressDialogFragment.show(getChildFragmentManager());
    }

    @Override
    public void onHideLoading() {
        ProgressDialogFragment.hide(getChildFragmentManager());
    }
}