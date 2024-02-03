package com.craftsman.management.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.craftsman.management.app.CustomApplication;
import com.craftsman.management.app.databinding.ActivityAboutBinding;
import com.craftsman.management.app.databinding.ActivityAboutBinding;
import com.craftsman.management.app.models.About;
import com.craftsman.management.app.persenters.about.AboutCallback;
import com.craftsman.management.app.persenters.about.AboutPresenter;
import com.craftsman.management.app.ui.activities.admin.AboutEditActivity;
import com.craftsman.management.app.utilities.helpers.StorageHelper;

import java.util.Locale;

public class AboutActivity extends BaseActivity implements AboutCallback {
    private ActivityAboutBinding binding;
    private AboutPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());

        binding.btnEdit.setVisibility(StorageHelper.getCurrentUser().isAdmin() ? View.VISIBLE : View.GONE);
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutActivity.this, AboutEditActivity.class));
            }
        });

        presenter = new AboutPresenter(this);
        setContentView(binding.getRoot());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        presenter.getAbout(PreferenceManager.getDefaultSharedPreferences(CustomApplication.getApplication()).getString("language", Locale.getDefault().getLanguage()));
    }

    @Override
    public void onGetAboutComplete(About about) {
        binding.content.setText(about.getContent());
        binding.conditions.setText(about.getConditions());
        binding.objectives.setText(about.getObjectives());
    }
}