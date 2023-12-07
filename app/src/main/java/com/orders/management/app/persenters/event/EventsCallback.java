package com.orders.management.app.persenters.event;

import com.orders.management.app.models.Event;
import com.orders.management.app.persenters.BaseCallback;

import java.util.List;

public interface EventsCallback extends BaseCallback {
    default void onGetEventsComplete(List<Event> events) {
    }

    default void onGetEventsCountComplete(long count) {
    }

    default void onSaveEventComplete() {
    }

    default void onDeleteEventComplete(Event event) {
    }

    default void onGetEventComplete(Event event) {
    }
}
