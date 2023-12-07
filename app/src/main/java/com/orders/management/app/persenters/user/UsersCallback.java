package com.orders.management.app.persenters.user;

import com.orders.management.app.persenters.BaseCallback;
import com.orders.management.app.models.User2;

import java.util.List;

public interface UsersCallback extends BaseCallback {
    default void onGetUsersComplete(List<User2> users) {
    }

    default void onSaveUserComplete() {
    }
    default void onGetUsersCountComplete(long adminCount, long studentCount) {
    }

    default void onDeleteUserComplete(User2 user) {
    }

    default void onSignupUserComplete() {
    }

    default void onSignupUserFail(String message) {
    }

    default void onGetUserComplete(User2 user) {
    }
}
