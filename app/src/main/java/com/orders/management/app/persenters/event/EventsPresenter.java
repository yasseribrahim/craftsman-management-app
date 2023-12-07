package com.orders.management.app.persenters.event;

import androidx.annotation.NonNull;

import com.orders.management.app.Constants;
import com.orders.management.app.models.Event;
import com.orders.management.app.persenters.BasePresenter;
import com.orders.management.app.utilities.helpers.StorageHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventsPresenter implements BasePresenter {
    private DatabaseReference reference;
    private ValueEventListener listener;
    private EventsCallback callback;

    public EventsPresenter(EventsCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_NAME_EVENTS).getRef();
        this.callback = callback;
    }

    public void save(Event event) {
        callback.onHideLoading();
        if (event.getId() == null) {
            event.setId("event-" + System.currentTimeMillis());
        }
        reference.child(event.getId()).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (callback != null) {
                    callback.onSaveEventComplete();
                    callback.onHideLoading();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e.getMessage(), null);
                callback.onHideLoading();
            }
        });
    }

    public void getEvent(String id) {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Event event = null;
                if (snapshot.exists()) {
                    event = snapshot.getValue(Event.class);
                }

                if (callback != null) {
                    callback.onGetEventComplete(event);
                    callback.onHideLoading();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (callback != null) {
                    callback.onFailure("Unable to get message: " + databaseError.getMessage(), null);
                    callback.onHideLoading();
                }
            }
        };
        reference.child(id).addListenerForSingleValueEvent(listener);
    }

    public void delete(Event event) {
        reference.child(event.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (callback != null) {
                    callback.onDeleteEventComplete(event);
                    callback.onHideLoading();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e.getMessage(), null);
                callback.onHideLoading();
            }
        });
    }

    public void getEvents() {
        callback.onShowLoading();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Event> events = new ArrayList<>();
                var isAdmin = StorageHelper.getCurrentUser().isAdmin();
                for (var child : snapshot.getChildren()) {
                    var event = child.getValue(Event.class);
                    if (isAdmin || event.isApproved()) {
                        events.add(event);
                    }
                }

                if (callback != null) {
                    callback.onGetEventsComplete(events);
                }
                callback.onHideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onHideLoading();
            }
        });
    }

    public void getEventsCreated() {
        callback.onShowLoading();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Event> events = new ArrayList<>();
                var currentUsername = StorageHelper.getCurrentUser().getUsername();
                for (var child : snapshot.getChildren()) {
                    var event = child.getValue(Event.class);
                    if (event.getCreatedBy().equalsIgnoreCase(currentUsername)) {
                        events.add(event);
                    }
                }

                if (callback != null) {
                    callback.onGetEventsComplete(events);
                }
                callback.onHideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onHideLoading();
            }
        });
    }

    public void count() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onGetEventsCountComplete(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        if (reference != null && listener != null) {
            reference.removeEventListener(listener);
        }
    }
}
