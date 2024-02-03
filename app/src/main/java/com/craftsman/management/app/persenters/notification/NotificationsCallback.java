package com.craftsman.management.app.persenters.notification;

import com.craftsman.management.app.models.Notification;
import com.craftsman.management.app.persenters.BaseCallback;

import java.util.List;

public interface NotificationsCallback extends BaseCallback {
    default void onGetNotificationsComplete(List<Notification> notifications) {
    }

    default void onSaveNotificationComplete() {
    }

    default void onDeleteNotificationComplete(int position) {
    }
}
