package com.orders.management.app.persenters.messaging;

import com.orders.management.app.models.Message;
import com.orders.management.app.persenters.BaseCallback;

public interface MessagingCallback extends BaseCallback {
    void onSendMessageSuccess();

    void onSendMessageFailure(String message);

    void onGetMessageSuccess(Message message);

    void onGetMessageFailure(String message);

    void onEmptyMessaging();
}
