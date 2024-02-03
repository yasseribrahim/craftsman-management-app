package com.craftsman.management.app.ui.fragments;

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

import com.craftsman.management.app.Constants;
import com.craftsman.management.app.R;
import com.craftsman.management.app.databinding.FragmentServicesAcceptedBinding;
import com.craftsman.management.app.models.Service;
import com.craftsman.management.app.persenters.service.ServicesCallback;
import com.craftsman.management.app.persenters.service.ServicesPresenter;
import com.craftsman.management.app.ui.adptres.ServicesAdapter;
import com.craftsman.management.app.utilities.helpers.StorageHelper;

import java.util.ArrayList;
import java.util.List;

public class ServicesAcceptedFragment extends Fragment implements ServicesCallback, ServicesAdapter.OnServicesClickListener {
    private FragmentServicesAcceptedBinding binding;
    private ServicesPresenter presenter;
    private ServicesAdapter adapter;
    private List<Service> services, searchedServices;

    public static ServicesAcceptedFragment newInstance() {
        Bundle args = new Bundle();
        ServicesAcceptedFragment fragment = new ServicesAcceptedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentServicesAcceptedBinding.inflate(inflater);

        presenter = new ServicesPresenter(this);

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

        services = new ArrayList<>();
        searchedServices = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ServicesAdapter(searchedServices, this, false);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void load() {
        presenter.getServicesAccepted();
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
    public void onGetServicesComplete(List<Service> services) {
        this.services.clear();
        var currentEvents = StorageHelper.getCurrentUser().getServices();
        for (var event : services) {
            if (currentEvents.contains(event.getId())) {
                this.services.add(event);
            }
        }
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
        searchedServices.clear();
        if (!searchedText.isEmpty()) {
            for (Service user : services) {
                if (isMatched(user, searchedText)) {
                    searchedServices.add(user);
                }
            }
        } else {
            searchedServices.addAll(services);
        }

        refresh();
    }

    private boolean isMatched(Service user, String text) {
        String searchedText = text.toLowerCase();
        boolean result = user.getTitle().toLowerCase().contains(searchedText) ||
                (user.getImageUrl() != null && user.getImageUrl().toLowerCase().contains(searchedText)) ||
                (user.getDescription() != null && user.getDescription().toLowerCase().contains(searchedText));
        return result;
    }

    private void refresh() {
        binding.message.setVisibility(View.GONE);
        if (searchedServices.isEmpty()) {
            binding.message.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onServiceViewListener(Service service) {
        Intent intent = new Intent(getContext(), ServiceViewerActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, service);
        startActivity(intent);
    }

    @Override
    public void onServiceEditListener(Service service) {

    }

    @Override
    public void onServiceDeleteListener(Service service) {

    }

    @Override
    public void onDeleteServiceComplete(Service service) {
        int index = searchedServices.indexOf(service);
        if (index != -1) {
            searchedServices.remove(index);
            adapter.notifyItemRemoved(index);
        }
        Toast.makeText(getContext(), R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
    }
}