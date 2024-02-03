package com.craftsman.management.app.persenters.service;

import androidx.annotation.NonNull;

import com.craftsman.management.app.Constants;
import com.craftsman.management.app.models.Service;
import com.craftsman.management.app.persenters.BasePresenter;
import com.craftsman.management.app.utilities.helpers.StorageHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServicesPresenter implements BasePresenter {
    private DatabaseReference reference;
    private ValueEventListener listener;
    private ServicesCallback callback;

    public ServicesPresenter(ServicesCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_NAME_SERVICES).getRef();
        this.callback = callback;
    }

    public void save(Service service) {
        callback.onHideLoading();
        if (service.getId() == null) {
            service.setId("event-" + System.currentTimeMillis());
        }
        reference.child(service.getId()).setValue(service).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (callback != null) {
                    callback.onSaveServiceComplete();
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

    public void getService(String id) {
        callback.onShowLoading();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Service service = null;
                if (snapshot.exists()) {
                    service = snapshot.getValue(Service.class);
                }

                if (callback != null) {
                    callback.onGetServiceComplete(service);
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

    public void delete(Service service) {
        reference.child(service.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (callback != null) {
                    callback.onDeleteServiceComplete(service);
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

    public void getServices() {
        callback.onShowLoading();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Service> services = new ArrayList<>();
                for (var child : snapshot.getChildren()) {
                    var service = child.getValue(Service.class);
                    services.add(service);
                }

                if (callback != null) {
                    callback.onGetServicesComplete(services);
                }
                callback.onHideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onHideLoading();
            }
        });
    }

    public void getServicesCreated() {
        callback.onShowLoading();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Service> services = new ArrayList<>();
                var currentUsername = StorageHelper.getCurrentUser().getUsername();
                for (var child : snapshot.getChildren()) {
                    var service = child.getValue(Service.class);
                    if (service.getCreatedBy().equalsIgnoreCase(currentUsername)) {
                        services.add(service);
                    }
                }

                if (callback != null) {
                    callback.onGetServicesComplete(services);
                }
                callback.onHideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onHideLoading();
            }
        });
    }

    public void getServicesAccepted() {
        callback.onShowLoading();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Service> services = new ArrayList<>();
                var currentUsername = StorageHelper.getCurrentUser().getUsername();
                for (var child : snapshot.getChildren()) {
                    var service = child.getValue(Service.class);
                    if (service.getAcceptedPrices() != null && service.getAcceptedPrices().getCraftsmanId().equalsIgnoreCase(currentUsername)) {
                        services.add(service);
                    }
                }

                if (callback != null) {
                    callback.onGetServicesComplete(services);
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
                callback.onGetServicesCountComplete(snapshot.getChildrenCount());
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
