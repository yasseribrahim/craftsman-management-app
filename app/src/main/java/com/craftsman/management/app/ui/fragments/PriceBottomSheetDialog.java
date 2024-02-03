package com.craftsman.management.app.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.craftsman.management.app.Constants;
import com.craftsman.management.app.R;
import com.craftsman.management.app.databinding.FragmentBottomSheetDialogPriceBinding;
import com.craftsman.management.app.models.Price;
import com.craftsman.management.app.models.Service;
import com.craftsman.management.app.persenters.service.ServicesCallback;
import com.craftsman.management.app.persenters.service.ServicesPresenter;
import com.craftsman.management.app.utilities.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class PriceBottomSheetDialog extends BottomSheetDialogFragment implements ServicesCallback {
    private FragmentBottomSheetDialogPriceBinding binding;
    private ServicesPresenter presenter;
    private Service service;
    private Price price;
    private OnPriceChangedCallback callback;

    public interface OnPriceChangedCallback {
        void onPriceChangedCallback(Service service);
    }

    public PriceBottomSheetDialog() {
    }

    public static PriceBottomSheetDialog newInstance(Service service, Price price) {
        PriceBottomSheetDialog fragment = new PriceBottomSheetDialog();
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_OBJECT_1, service);
        args.putParcelable(Constants.ARG_OBJECT_2, price);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnPriceChangedCallback) {
            callback = (OnPriceChangedCallback) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ServicesPresenter(this);
        service = getArguments().getParcelable(Constants.ARG_OBJECT_1);
        price = getArguments().getParcelable(Constants.ARG_OBJECT_2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBottomSheetDialogPriceBinding.inflate(inflater);
        bind();
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        var dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setDimAmount(0.4f); /** Set dim amount here (the dimming factor of the parent fragment) */
        return dialog;
    }

    private void bind() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service.getPrices() == null) {
                    service.setPrices(new ArrayList<>());
                }
                if (binding.price.getText().toString().isEmpty()) {
                    binding.price.setError("Invalid Price");
                    binding.price.requestFocus();
                    return;
                }
                int value = Integer.parseInt(binding.price.getText().toString());

                int index = service.getPrices().indexOf(price);
                if (index != -1) {
                    price = service.getPrices().get(index);
                } else {
                    service.getPrices().add(price);
                }
                price.setPrice(value);
                price.setDate(Calendar.getInstance().getTime());

                presenter.save(service);
            }
        });

        binding.price.setText(price.getPrice() + "");
        binding.username.setText(price.getCraftsmanId());
    }

    @Override
    public void onSaveServiceComplete() {
        ToastUtils.longToast(R.string.str_message_updated_successfully);
        if(callback != null) {
            callback.onPriceChangedCallback(service);
        }
        dismiss();
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        ToastUtils.longToast(message);
    }

    @Override
    public void onShowLoading() {
        ProgressDialogFragment.show(getParentFragmentManager());
    }

    @Override
    public void onHideLoading() {
        ProgressDialogFragment.hide(getParentFragmentManager());
    }
}