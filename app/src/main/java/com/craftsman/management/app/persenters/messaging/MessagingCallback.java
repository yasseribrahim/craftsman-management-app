package com.craftsman.management.app.persenters.messaging;

import com.craftsman.management.app.models.Message;
import com.craftsman.management.app.persenters.BaseCallback;

public interface MessagingCallback extends BaseCallback {
    void onSendMessageSuccess();

    void onSendMessageFailure(String message);

    void onGetMessageSuccess(Message message);

    void onGetMessageFailure(String message);

    void onEmptyMessaging();
}
