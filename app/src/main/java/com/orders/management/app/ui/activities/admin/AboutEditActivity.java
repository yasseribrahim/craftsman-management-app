package com.orders.management.app.ui.activities.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.orders.management.app.models.About;
import com.orders.management.app.persenters.about.AboutCallback;
import com.orders.management.app.persenters.about.AboutPresenter;
import com.orders.management.app.ui.activities.BaseActivity;
import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.ActivityAboutEditBinding;

public class AboutEditActivity extends BaseActivity implements AboutCallback {
    private ActivityAboutEditBinding binding;
    private AboutPresenter presenter;
    private About aboutAr, aboutEn;

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutEditActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutEditBinding.inflate(getLayoutInflater());
        presenter = new AboutPresenter(this);

        binding.contentAr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aboutAr.setContent(s.toString().trim());
            }
        });
        binding.conditionsAr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aboutAr.setConditions(s.toString().trim());
            }
        });
        binding.objectivesAr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aboutAr.setObjectives(s.toString().trim());
            }
        });
        binding.contentEn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aboutEn.setContent(s.toString().trim());
            }
        });
        binding.conditionsEn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aboutEn.setConditions(s.toString().trim());
            }
        });
        binding.objectivesEn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aboutEn.setObjectives(s.toString().trim());
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.save(aboutAr, "ar");
                presenter.save(aboutEn, "en");
            }
        });

        setContentView(binding.getRoot());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        aboutAr = null;
        aboutEn = null;
        presenter.getAbout("ar");
    }

    @Override
    public void onSaveAboutComplete() {
        Toast.makeText(this, R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShowLoading() {
        binding.progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideLoading() {
        binding.progress.setVisibility(View.GONE);
    }

    @Override
    public void onGetAboutComplete(About about) {
        if (aboutAr == null) {
            aboutAr = about;
            binding.contentAr.setText(about.getContent());
            binding.conditionsAr.setText(about.getConditions());
            binding.objectivesAr.setText(about.getObjectives());
            presenter.getAbout("en");
        } else {
            aboutEn = about;
            binding.contentEn.setText(about.getContent());
            binding.conditionsEn.setText(about.getConditions());
            binding.objectivesEn.setText(about.getObjectives());
        }
    }
}