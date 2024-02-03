package com.craftsman.management.app.ui.activities.client;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.craftsman.management.app.R;
import com.craftsman.management.app.databinding.ActivityHomeClientBinding;
import com.craftsman.management.app.models.User;
import com.craftsman.management.app.ui.fragments.CraftsmansFragment;
import com.craftsman.management.app.ui.fragments.MoreFragment;
import com.craftsman.management.app.ui.fragments.NotificationsFragment;
import com.craftsman.management.app.ui.fragments.ServicesCreatedFragment;
import com.craftsman.management.app.utilities.helpers.StorageHelper;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeClientBinding binding;
    private MenuItem previousItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpActionBar();
        setupNavDrawer();
    }

    @SuppressLint("WrongConstant")
    private void setupNavDrawer() {
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (previousItem == null || previousItem.getItemId() != item.getItemId()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switch (item.getItemId()) {
                                case R.id.nav_craftsman:
                                    showFragment(CraftsmansFragment.newInstance(), R.id.container);
                                    binding.content.toolbar.setTitle(R.string.menu_craftsman);
                                    break;
                                case R.id.nav_services_created:
                                    showFragment(ServicesCreatedFragment.newInstance(), R.id.container);
                                    binding.content.toolbar.setTitle(R.string.menu_services_created);
                                    break;
                                case R.id.nav_notifications:
                                    showFragment(NotificationsFragment.newInstance(), R.id.container);
                                    binding.content.toolbar.setTitle(R.string.menu_notifications);
                                    break;
                                case R.id.nav_more:
                                    showFragment(MoreFragment.newInstance(), R.id.container);
                                    binding.content.toolbar.setTitle(R.string.menu_more);
                                    break;
                            }
                        }
                    }, 300);
                    if (previousItem != null) {
                        previousItem.setChecked(false);
                    }
                    item.setChecked(true);
                    previousItem = item;
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        binding.navigationView.setCheckedItem(R.id.nav_craftsman);
        showFragment(CraftsmansFragment.newInstance(), R.id.container);
        binding.content.toolbar.setTitle(R.string.menu_craftsman);

        CircleImageView profileImage = binding.navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        TextView name = binding.navigationView.getHeaderView(0).findViewById(R.id.full_name);
        TextView username = binding.navigationView.getHeaderView(0).findViewById(R.id.username);
        User user = StorageHelper.getCurrentUser();
        if (user != null) {
            name.setText(user.getFullName());
            username.setText(user.getUsername());
            Glide.with(this).load(user.getImageProfile()).placeholder(R.drawable.ic_profile).into(profileImage);
        }
    }

    //Set up Action Bar
    @SuppressLint("WrongConstant")
    private void setUpActionBar() {
        binding.content.toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(binding.content.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.content.toolbar.setNavigationIcon(R.drawable.ic_menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (binding.drawerLayout.isDrawerOpen(Gravity.START)) {
                    binding.drawerLayout.closeDrawer(Gravity.START);
                } else {
                    binding.drawerLayout.openDrawer(Gravity.START);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showFragment(Fragment fragment, int fragmentContainer) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer, fragment);
        transaction.commitAllowingStateLoss();
    }
}