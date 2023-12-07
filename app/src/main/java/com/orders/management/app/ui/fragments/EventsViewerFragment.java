package com.orders.management.app.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.orders.management.app.Constants;
import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.FragmentEventsBinding;
import com.orders.management.university.app.databinding.FragmentEventsViewerBinding;
import com.orders.management.app.models.Event;
import com.orders.management.app.persenters.event.EventsCallback;
import com.orders.management.app.persenters.event.EventsPresenter;
import com.orders.management.app.ui.activities.EventViewerActivity;
import com.orders.management.app.ui.adptres.EventsViewerAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventsViewerFragment extends Fragment implements EventsCallback, EventsViewerAdapter.OnEventsClickListener {
    private FragmentEventsViewerBinding binding;
    private EventsPresenter presenter;
    private EventsViewerAdapter usersAdapter;
    private List<Event> events, searchedEvents;

    public static EventsViewerFragment newInstance() {
        Bundle args = new Bundle();
        EventsViewerFragment fragment = new EventsViewerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventsViewerBinding.inflate(inflater);

        presenter = new EventsPresenter(this);

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

        events = new ArrayList<>();
        searchedEvents = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersAdapter = new EventsViewerAdapter(searchedEvents, this);
        binding.recyclerView.setAdapter(usersAdapter);

        return binding.getRoot();
    }

    private void load() {
        presenter.getEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public void onGetEventsComplete(List<Event> users) {
        this.events.clear();
        this.events.addAll(users);
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void search(String searchedText) {
        searchedEvents.clear();
        if (!searchedText.isEmpty()) {
            for (Event user : events) {
                if (isMatched(user, searchedText)) {
                    searchedEvents.add(user);
                }
            }
        } else {
            searchedEvents.addAll(events);
        }

        refresh();
    }

    private boolean isMatched(Event user, String text) {
        String searchedText = text.toLowerCase();
        boolean result = user.getTitle().toLowerCase().contains(searchedText) ||
                (user.getAddress() != null && user.getAddress().toLowerCase().contains(searchedText)) ||
                (user.getDescription() != null && user.getDescription().toLowerCase().contains(searchedText));
        return result;
    }

    private void refresh() {
        binding.message.setVisibility(View.GONE);
        if (searchedEvents.isEmpty()) {
            binding.message.setVisibility(View.VISIBLE);
        }

        usersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEventViewListener(Event event) {
        Intent intent = new Intent(getContext(), EventViewerActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, event);
        startActivity(intent);
    }

    @Override
    public void onDeleteEventComplete(Event event) {
        int index = searchedEvents.indexOf(event);
        if (index != -1) {
            searchedEvents.remove(index);
            usersAdapter.notifyItemRemoved(index);
        }
        Toast.makeText(getContext(), R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
    }
}