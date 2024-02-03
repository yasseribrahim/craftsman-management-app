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
import com.craftsman.management.app.databinding.FragmentServicesBinding;
import com.craftsman.management.app.models.Service;
import com.craftsman.management.app.persenters.service.ServicesCallback;
import com.craftsman.management.app.persenters.service.ServicesPresenter;
import com.craftsman.management.app.ui.activities.ServiceActivity;
import com.craftsman.management.app.ui.adptres.ServicesAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends Fragment implements ServicesCallback, ServicesAdapter.OnServicesClickListener {
    private FragmentServicesBinding binding;
    private ServicesPresenter presenter;
    private ServicesAdapter servicesAdapter;
    private List<Service> services, searchedServices;

    public static ServicesFragment newInstance() {
        Bundle args = new Bundle();
        ServicesFragment fragment = new ServicesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentServicesBinding.inflate(inflater);

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
        servicesAdapter = new ServicesAdapter(searchedServices, this, false);
        binding.recyclerView.setAdapter(servicesAdapter);

        return binding.getRoot();
    }

    private void load() {
        presenter.getServices();
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
    public void onGetServicesComplete(List<Service> users) {
        this.services.clear();
        this.services.addAll(users);
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

        servicesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onServiceViewListener(Service service) {
        openServiceActivity(service);
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
            servicesAdapter.notifyItemRemoved(index);
        }
        Toast.makeText(getContext(), R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
    }

    private void openServiceActivity(Service service) {
        Intent intent = new Intent(getContext(), ServiceActivity.class);
        intent.putExtra(Constants.ARG_OBJECT, service);
        startActivity(intent);
    }
}