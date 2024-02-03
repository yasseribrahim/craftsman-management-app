package com.craftsman.management.app.persenters.service;

import com.craftsman.management.app.models.Service;
import com.craftsman.management.app.persenters.BaseCallback;

import java.util.List;

public interface ServicesCallback extends BaseCallback {
    default void onGetServicesComplete(List<Service> services) {
    }

    default void onGetServicesCountComplete(long count) {
    }

    default void onSaveServiceComplete() {
    }

    default void onDeleteServiceComplete(Service service) {
    }

    default void onGetServiceComplete(Service service) {
    }
}
