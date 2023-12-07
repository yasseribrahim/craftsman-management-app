package com.orders.management.app.persenters.about;

import com.orders.management.app.persenters.BaseCallback;
import com.orders.management.app.models.About;

public interface AboutCallback extends BaseCallback {
    default void onGetAboutComplete(About about) {
    }

    default void onSaveAboutComplete() {
    }
}
