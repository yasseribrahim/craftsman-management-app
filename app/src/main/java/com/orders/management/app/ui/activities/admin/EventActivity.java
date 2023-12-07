package com.orders.management.app.ui.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.orders.management.app.Constants;
import com.orders.management.app.models.Event;
import com.orders.management.app.models.Notification;
import com.orders.management.app.models.User2;
import com.orders.management.app.persenters.event.EventsCallback;
import com.orders.management.app.persenters.event.EventsPresenter;
import com.orders.management.app.persenters.notification.NotificationsCallback;
import com.orders.management.app.persenters.user.UsersCallback;
import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.ActivityEventBinding;
import com.orders.management.app.persenters.notification.NotificationsPresenter;
import com.orders.management.app.persenters.user.UsersPresenter;
import com.orders.management.app.ui.activities.BaseActivity;
import com.orders.management.app.utilities.DatesUtils;
import com.orders.management.app.utilities.ToastUtils;
import com.orders.management.app.utilities.helpers.LocaleHelper;
import com.orders.management.app.utilities.helpers.StorageHelper;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventActivity extends BaseActivity implements EventsCallback, UsersCallback, NotificationsCallback {
    private ActivityEventBinding binding;
    private EventsPresenter eventsPresenter;
    private NotificationsPresenter notificationsPresenter;
    private UsersPresenter usersPresenter;
    private Event event;
    private List<User2> users;
    private boolean isDateSelected = false, isTimeSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        eventsPresenter = new EventsPresenter(this);
        notificationsPresenter = new NotificationsPresenter(this);
        usersPresenter = new UsersPresenter(this);

        event = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        bind();

        users = new ArrayList<>();
        binding.accepted.setVisibility(StorageHelper.getCurrentUser().isAdmin() ? View.VISIBLE : View.GONE);
        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText(getString(R.string.str_date_hint));
                final MaterialDatePicker picker = builder.build();
                picker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        if (selection instanceof Long) {
                            Calendar calendar = Calendar.getInstance();

                            calendar.setTimeInMillis((Long) selection);
                            setDate(calendar.getTime());
                        }
                    }
                });
            }
        });

        binding.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = getEventCalendar();

                int hours = calendar.get(Calendar.HOUR);
                int minutes = calendar.get(Calendar.MINUTE);

                MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
                builder.setTitleText(getString(R.string.str_time_hint));
                var picker = builder.setTitleText("SELECT YOUR TIMING")
                        // set the default hour for the
                        // dialog when the dialog opens
                        .setHour(hours)
                        // set the default minute for the
                        // dialog when the dialog opens
                        .setMinute(minutes)
                        // set the time format
                        // according to the region
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .build();
                picker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        var pickedHour = picker.getHour();
                        var pickedMinute = picker.getMinute();

                        Calendar calendar = Calendar.getInstance();

                        calendar.set(Calendar.HOUR_OF_DAY, pickedHour);
                        calendar.set(Calendar.MINUTE, pickedMinute);
                        setTime(calendar.getTime());
                    }
                });
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!users.isEmpty()) {
                    String title = binding.title.getText().toString().trim();
                    String description = binding.description.getText().toString().trim();
                    String address = binding.address.getText().toString().trim();

                    if (title.isEmpty()) {
                        binding.title.setError(getString(R.string.str_title_hint));
                        binding.title.requestFocus();
                        return;
                    }
                    if (description.isEmpty()) {
                        binding.description.setError(getString(R.string.str_description_hint));
                        binding.description.requestFocus();
                        return;
                    }
                    if (address.isEmpty()) {
                        binding.address.setError(getString(R.string.str_address_invalid));
                        binding.address.requestFocus();
                        return;
                    }
                    if (!isDateSelected) {
                        binding.date.setError(getString(R.string.str_date_hint));
                        ToastUtils.longToast(getString(R.string.str_date_hint));
                        return;
                    }
                    if (!isTimeSelected) {
                        binding.time.setError(getString(R.string.str_time_hint));
                        ToastUtils.longToast(getString(R.string.str_time_hint));
                        return;
                    }

                    event.setTitle(title);
                    event.setDescription(binding.description.getText().toString());
                    if (event.getId() == null) {
                        event.setCreatedBy(StorageHelper.getCurrentUser().getUsername());
                    }
                    event.setAddress(address);
                    event.setApproved(binding.accepted.isChecked());
                    eventsPresenter.save(event);
                } else {
                    ToastUtils.longToast("Please Wait!!!");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        usersPresenter.getUsers(Constants.USER_TYPE_STUDENT);
    }

    @Override
    public void onGetUsersComplete(List<User2> users) {
        this.users.clear();
        this.users.addAll(users);
    }

    @Override
    public void onSaveEventComplete() {
        Toast.makeText(this, R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
        Notification notification = new Notification();
        notification.setMessage(getString(R.string.str_notification_message, event.getTitle(), DatesUtils.formatDate(event.getDate())));
        notification.setEventId(event.getId());
        if(StorageHelper.getCurrentUser().isAdmin()) {
            notificationsPresenter.save(notification, users);
        } else {
            finish();
        }
    }

    @Override
    public void onSaveNotificationComplete() {
        finish();
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, getString(R.string.str_save_fail, message), Toast.LENGTH_LONG).show();
    }

    private void bind() {
        binding.title.setText(event.getTitle());
        binding.description.setText(event.getDescription());
        binding.address.setText(event.getAddress());
        binding.accepted.setChecked(event.isApproved());
        setDate(event.getDate());
        setTime(event.getDate());
    }

    private void setDate(Date date) {
        if (date != null) {
            Calendar eventCalendar = getEventCalendar();
            Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTime(date);

            eventCalendar.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
            eventCalendar.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
            eventCalendar.set(Calendar.DATE, dateCalendar.get(Calendar.DATE));

            event.setDate(eventCalendar.getTime());
            binding.date.setText(DatesUtils.formatDateOnly(date));
            isDateSelected = true;
        }
    }

    private Calendar getEventCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);

        if (event.getDate() != null) {
            calendar.setTime(event.getDate());
        }

        return calendar;
    }

    private void setTime(Date date) {
        if (date != null) {
            Calendar eventCalendar = getEventCalendar();
            Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTime(date);

            eventCalendar.set(Calendar.HOUR, dateCalendar.get(Calendar.HOUR));
            eventCalendar.set(Calendar.MINUTE, dateCalendar.get(Calendar.MINUTE));
            eventCalendar.set(Calendar.SECOND, 0);
            eventCalendar.set(Calendar.MILLISECOND, 0);

            event.setDate(eventCalendar.getTime());
            binding.time.setText(DatesUtils.formatTimeOnly(date));
            isTimeSelected = true;
        }
    }
}