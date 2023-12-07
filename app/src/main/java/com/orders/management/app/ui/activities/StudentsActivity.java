package com.orders.management.app.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.orders.management.app.Constants;
import com.orders.management.app.ui.adptres.StudentsAdapter;
import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.ActivityStudentsBinding;
import com.orders.management.app.models.User2;
import com.orders.management.app.persenters.user.UsersCallback;
import com.orders.management.app.persenters.user.UsersPresenter;
import com.orders.management.app.utilities.helpers.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

public class StudentsActivity extends BaseActivity implements UsersCallback, StudentsAdapter.OnItemClickListener {
    private ActivityStudentsBinding binding;
    private UsersPresenter presenter;
    private StudentsAdapter usersAdapter;
    private List<User2> users, searchedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new UsersPresenter(this);

        binding.refreshLayout.setColorSchemeResources(R.color.refreshColor1, R.color.refreshColor2, R.color.refreshColor3, R.color.refreshColor4);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        binding.textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(binding.textSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        users = new ArrayList<>();
        searchedUsers = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new StudentsAdapter(searchedUsers, this);
        binding.recyclerView.setAdapter(usersAdapter);
        setUpActionBar();
    }

    @SuppressLint("WrongConstant")
    private void setUpActionBar() {
        binding.appBarLayout.toolbar.setTitle("");
        setSupportActionBar(binding.appBarLayout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.back_to_home_button);
    }

    private void load() {
        presenter.getUsers(Constants.USER_TYPE_STUDENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public void onGetUsersComplete(List<User2> users) {
        this.users.clear();
        this.users.addAll(users);
        search(binding.textSearch.getText().toString());
    }

    @Override
    public void onShowLoading() {
        binding.refreshLayout.setRefreshing(true);
    }

    @Override
    public void onHideLoading() {
        binding.refreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void search(String searchedText) {
        searchedUsers.clear();
        if (!searchedText.isEmpty()) {
            for (User2 user : users) {
                if (isMatched(user, searchedText)) {
                    searchedUsers.add(user);
                }
            }
        } else {
            searchedUsers.addAll(users);
        }

        refresh();
    }

    private boolean isMatched(User2 user, String text) {
        String searchedText = text.toLowerCase();
        boolean result = user.getFullName().toLowerCase().contains(searchedText) ||
                (user.getAddress() != null && user.getAddress().toLowerCase().contains(searchedText)) ||
                (user.getPhone() != null && user.getPhone().toLowerCase().contains(searchedText)) ||
                (user.getUsername() != null && user.getUsername().toLowerCase().contains(searchedText));
        return result;
    }

    private void refresh() {
        binding.message.setVisibility(View.GONE);
        if (searchedUsers.isEmpty()) {
            binding.message.setVisibility(View.VISIBLE);
        }

        usersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemViewListener(User2 user) {
//        Intent intent = new Intent(this, QuestionsViewerActivity.class);
//        intent.putExtra(Constants.ARG_OBJECT, lesson);
//        intent.putExtra(Constants.ARG_USER, user);
//        startActivity(intent);
    }
}