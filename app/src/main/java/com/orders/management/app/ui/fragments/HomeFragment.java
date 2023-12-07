package com.orders.management.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.FragmentHomeBinding;
import com.orders.management.app.models.User2;
import com.orders.management.app.persenters.event.EventsCallback;
import com.orders.management.app.persenters.event.EventsPresenter;
import com.orders.management.app.persenters.user.UsersCallback;
import com.orders.management.app.persenters.user.UsersPresenter;
import com.orders.management.app.utilities.ToastUtils;
import com.orders.management.app.utilities.helpers.StorageHelper;

public class HomeFragment extends Fragment implements UsersCallback, EventsCallback {
    private FragmentHomeBinding binding;
    private UsersPresenter usersPresenter;
    private EventsPresenter eventsPresenter;
    private User2 currentUser;

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
        eventsPresenter = new EventsPresenter(this);
        currentUser = StorageHelper.getCurrentUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        usersPresenter.getUsersCount();
        eventsPresenter.count();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        binding.username.setText(getString(R.string.str_welcome_message, currentUser.getFullName()));
        return binding.getRoot();
    }

    @Override
    public void onGetUsersCountComplete(long adminCount, long studentCount) {
        binding.adminsCounter.setText(adminCount + "");
        binding.studentsCounter.setText(studentCount + "");
    }

    @Override
    public void onGetEventsCountComplete(long count) {
        binding.questionsCounter.setText(count + "");
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