package com.orders.management.app.persenters.firebase;

import com.orders.management.app.persenters.BaseCallback;

public interface FirebaseCallback extends BaseCallback {
    default void onSaveTokenComplete() {
    }

    default void onGetTokenComplete(String token) {
    }
}
