package com.craftsman.management.app.persenters.about;

import com.craftsman.management.app.persenters.BaseCallback;
import com.craftsman.management.app.models.About;

public interface AboutCallback extends BaseCallback {
    default void onGetAboutComplete(About about) {
    }

    default void onSaveAboutComplete() {
    }
}
