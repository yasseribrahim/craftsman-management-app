package com.craftsman.management.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.craftsman.management.app.Constants;
import com.craftsman.management.app.R;
import com.craftsman.management.app.databinding.ActivityServicePricesBinding;
import com.craftsman.management.app.models.ChatId;
import com.craftsman.management.app.models.Notification;
import com.craftsman.management.app.models.Price;
import com.craftsman.management.app.models.Service;
import com.craftsman.management.app.models.User;
import com.craftsman.management.app.persenters.notification.NotificationsCallback;
import com.craftsman.management.app.persenters.notification.NotificationsPresenter;
import com.craftsman.management.app.persenters.service.ServicesCallback;
import com.craftsman.management.app.persenters.service.ServicesPresenter;
import com.craftsman.management.app.persenters.user.UsersCallback;
import com.craftsman.management.app.persenters.user.UsersPresenter;
import com.craftsman.management.app.ui.adptres.PricesAdapter;
import com.craftsman.management.app.ui.fragments.PriceBottomSheetDialog;
import com.craftsman.management.app.utilities.DatesUtils;
import com.craftsman.management.app.utilities.helpers.LocaleHelper;
import com.craftsman.management.app.utilities.helpers.StorageHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ServicePricesActivity extends BaseActivity implements ServicesCallback, NotificationsCallback, PriceBottomSheetDialog.OnPriceChangedCallback, PricesAdapter.OnPricesClickListener, UsersCallback {
    private static final int REQUEST_CODE_CAMERA_AND_STORAGE = 100;
    private static final String TAG = ServicePricesActivity.class.getSimpleName();
    private ActivityServicePricesBinding binding;
    private ServicesPresenter servicesPresenter;
    private NotificationsPresenter notificationsPresenter;
    private UsersPresenter usersPresenter;
    private PricesAdapter adapter;
    private Service service;
    private List<User> users;
    private User currentUser;
    private boolean canEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityServicePricesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        servicesPresenter = new ServicesPresenter(this);
        notificationsPresenter = new NotificationsPresenter(this);
        usersPresenter = new UsersPresenter(this);

        service = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        currentUser = StorageHelper.getCurrentUser();
        canEdit = currentUser.getUsername().equalsIgnoreCase(service.getCreatedBy());

        if (service.getPrices() == null) {
            service.setPrices(new ArrayList<>());
        }

        var canAccepted = canEdit;
        var canChat = canEdit;
        adapter = new PricesAdapter(service.getPrices(), this, canChat, canAccepted);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        bind();

        users = new ArrayList<>();

        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user1 = null;
                User user2 = currentUser;
                for (User user : users) {
                    if (user.getUsername().equalsIgnoreCase(service.getCreatedBy())) {
                        user1 = user;
                        break;
                    }
                }

                if (user1 != null) {
                    ChatId id = new ChatId(user1, user2);
                    Intent intent = new Intent(ServicePricesActivity.this, MessagingActivity.class);
                    intent.putExtra(Constants.ARG_OBJECT, id);
                    startActivity(intent);
                }
            }
        });

        binding.btnAddPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Price price = new Price("", currentUser.getUsername(), 0, Calendar.getInstance().getTime(), false);
                int index = service.getPrices() != null ? service.getPrices().indexOf(price) : -1;
                if (index != -1) {
                    price = service.getPrices().get(index);
                }
                PriceBottomSheetDialog dialog = PriceBottomSheetDialog.newInstance(service, price);
                dialog.show(getSupportFragmentManager(), "");
            }
        });
    }

    @Override
    public void onPriceChangedCallback(Service service) {
        this.service = service;
        bind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        usersPresenter.getUsers(0);
    }

    @Override
    public void onGetUsersComplete(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
    }

    @Override
    public void onSaveServiceComplete() {
        Toast.makeText(this, R.string.str_message_updated_successfully, Toast.LENGTH_LONG).show();
        Notification notification = new Notification();
        notification.setMessage(getString(R.string.str_notification_message, service.getTitle(), DatesUtils.formatDate(service.getDate())));
        notification.setServiceId(service.getId());
        notificationsPresenter.save(notification, users);
        bind();
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, getString(R.string.str_save_fail, message), Toast.LENGTH_LONG).show();
    }

    private void bind() {
        binding.btnAddPrice.setVisibility(currentUser.isCraftsman() ? View.VISIBLE : View.GONE);
        binding.btnChat.setVisibility(currentUser.isCraftsman() ? View.VISIBLE : View.GONE);

        binding.username.setText(service.getCreatedBy());
        Glide.with(this).load(service.getImageUrl()).placeholder(R.drawable.default_image).into(binding.image);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPriceEditListener(Price price) {
        PriceBottomSheetDialog dialog = PriceBottomSheetDialog.newInstance(service, price);
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onPriceAcceptedListener(Price price) {
        if(price.isAccepted()) {
            service.setAcceptedPrices(price);
        } else {
            service.setAcceptedPrices(null);
        }
        for (Price price1 : service.getPrices()) {
            price1.setAccepted(price1.equals(price));
        }

        servicesPresenter.save(service);
    }

    @Override
    public void onPriceDeleteListener(Price price) {
        int index = service.getPrices().indexOf(price);
        if(index != -1) {
            service.getPrices().remove(index);
            servicesPresenter.save(service);
        }
    }

    @Override
    public void onPriceChatListener(Price price) {
        var user1 = currentUser;
        User user2 = null;
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(price.getCraftsmanId())) {
                user2 = user;
                break;
            }
        }

        if (user2 != null) {
            ChatId id = new ChatId(user1, user2);
            Intent intent = new Intent(this, MessagingActivity.class);
            intent.putExtra(Constants.ARG_OBJECT, id);
            startActivity(intent);
        }
    }
}