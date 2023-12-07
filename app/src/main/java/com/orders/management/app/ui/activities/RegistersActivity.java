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
import com.orders.management.app.models.Event;
import com.orders.management.app.models.User2;
import com.orders.management.app.persenters.event.EventsCallback;
import com.orders.management.app.persenters.event.EventsPresenter;
import com.orders.management.app.persenters.user.UsersCallback;
import com.orders.management.app.ui.adptres.RegistersAdapter;
import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.ActivityRegistersBinding;
import com.orders.management.app.persenters.user.UsersPresenter;

import java.util.ArrayList;
import java.util.List;

public class RegistersActivity extends BaseActivity implements UsersCallback, EventsCallback, RegistersAdapter.OnItemClickListener {
    private ActivityRegistersBinding binding;
    private UsersPresenter usersPresenter;
    private EventsPresenter eventsPresenter;
    private RegistersAdapter adapter;
    private List<User2> users, searchedUsers;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        eventsPresenter = new EventsPresenter(this);
        usersPresenter = new UsersPresenter(this);
        event = getIntent().getParcelableExtra(Constants.ARG_OBJECT);

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
        binding.appBarLayout.toolbar.setTitle(event.getTitle());
        setSupportActionBar(binding.appBarLayout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.appBarLayout.toolbar.setNavigationIcon(R.drawable.back_to_home_button);
    }

    private void load() {
        usersPresenter.getUsers(event.getRegisters());
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (eventsPresenter != null) {
            eventsPresenter.onDestroy();
        }
    }

    @Override
    public void onGetUsersComplete(List<User2> users) {
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
    public void onItemViewListener(User2 user) {

    }

    @Override
    public void onDeleteItemViewListener(User2 user) {
        int position = searchedUsers.indexOf(user);
        users.remove(user);
        adapter.notifyItemRemoved(position);
        event.getRegisters().remove(user.getId());
        eventsPresenter.save(event);
    }

    @Override
    public void onSaveEventComplete() {
        Toast.makeText(this, R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
        load();
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