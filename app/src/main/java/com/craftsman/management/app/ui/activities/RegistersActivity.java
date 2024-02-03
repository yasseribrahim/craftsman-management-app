package com.craftsman.management.app.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.craftsman.management.app.Constants;
import com.craftsman.management.app.models.Service;
import com.craftsman.management.app.models.User;
import com.craftsman.management.app.persenters.service.ServicesCallback;
import com.craftsman.management.app.persenters.service.ServicesPresenter;
import com.craftsman.management.app.persenters.user.UsersCallback;
import com.craftsman.management.app.ui.adptres.RegistersAdapter;
import com.craftsman.management.app.R;
import com.craftsman.management.app.databinding.ActivityRegistersBinding;
import com.craftsman.management.app.persenters.user.UsersPresenter;

import java.util.ArrayList;
import java.util.List;

public class RegistersActivity extends BaseActivity implements UsersCallback, ServicesCallback, RegistersAdapter.OnItemClickListener {
    private ActivityRegistersBinding binding;
    private UsersPresenter usersPresenter;
    private ServicesPresenter servicesPresenter;
    private RegistersAdapter adapter;
    private List<User> users, searchedUsers;
    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        servicesPresenter = new ServicesPresenter(this);
        usersPresenter = new UsersPresenter(this);
        service = getIntent().getParcelableExtra(Constants.ARG_OBJECT);

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
        adapter = new RegistersAdapter(searchedUsers, this);
        binding.recyclerView.setAdapter(adapter);
        setUpActionBar();
    }

    @SuppressLint("WrongConstant")
    private void setUpActionBar() {
        binding.appBarLayout.toolbar.setTitle(service.getTitle());
        setSupportActionBar(binding.appBarLayout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.back_to_home_button);
    }

    private void load() {
        usersPresenter.getUsers(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (servicesPresenter != null) {
            servicesPresenter.onDestroy();
        }
    }

    @Override
    public void onGetUsersComplete(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        search(binding.textSearch.getText().toString().trim());
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

    @Override
    public void onItemViewListener(User user) {

    }

    @Override
    public void onDeleteItemViewListener(User user) {
        int position = searchedUsers.indexOf(user);
        users.remove(user);
        adapter.notifyItemRemoved(position);
        service.getPrices().remove(user.getId());
        servicesPresenter.save(service);
    }

    @Override
    public void onSaveServiceComplete() {
        Toast.makeText(this, R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
        load();
    }

    private void search(String searchedText) {
        searchedUsers.clear();
        if (!searchedText.isEmpty()) {
            for (User user : users) {
                if (isMatched(user, searchedText)) {
                    searchedUsers.add(user);
                }
            }
        } else {
            searchedUsers.addAll(users);
        }

        refresh();
    }

    private boolean isMatched(User user, String text) {
        String searchedText = text.toLowerCase();
        boolean result = user.getUsername().toLowerCase().contains(searchedText) ||
                user.getFullName().toLowerCase().contains(searchedText) ||
                (user.getAddress() != null && user.getAddress().toLowerCase().contains(searchedText)) ||
                (user.getPhone() != null && user.getPhone().toLowerCase().contains(searchedText));
        return result;
    }

    private void refresh() {
        binding.message.setVisibility(View.GONE);
        if (searchedUsers.isEmpty()) {
            binding.message.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}