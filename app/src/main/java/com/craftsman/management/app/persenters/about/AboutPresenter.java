package com.craftsman.management.app.persenters.about;

import androidx.annotation.NonNull;

import com.craftsman.management.app.Constants;
import com.craftsman.management.app.models.About;
import com.craftsman.management.app.persenters.BasePresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutPresenter implements BasePresenter {
    private DatabaseReference reference;
    private ValueEventListener listener;
    private AboutCallback callback;

    public AboutPresenter(AboutCallback callback) {
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_NAME_ABOUT).getRef();
        this.callback = callback;
    }

    public void save(About about, String language) {
        reference.child(language).setValue(about).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onSaveAboutComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e.getMessage(), null);
            }
        });
    }

    public void getAbout(String language) {
        callback.onShowLoading();
        reference.child(language).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                About about = new About();
                if (snapshot.exists()) {
                    about = snapshot.getValue(About.class);
                }
                if (callback != null) {
                    callback.onGetAboutComplete(about);
                }
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
