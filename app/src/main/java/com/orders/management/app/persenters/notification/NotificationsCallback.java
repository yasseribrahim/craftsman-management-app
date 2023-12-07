package com.orders.management.app.persenters.notification;

import com.orders.management.app.models.Notification;
import com.orders.management.app.persenters.BaseCallback;

import java.util.List;

public interface NotificationsCallback extends BaseCallback {
    default void onGetNotificationsComplete(List<Notification> notifications) {
    }

    default void onSaveNotificationComplete() {
    }

    default void onDeleteNotificationComplete(int position) {
    }
}
