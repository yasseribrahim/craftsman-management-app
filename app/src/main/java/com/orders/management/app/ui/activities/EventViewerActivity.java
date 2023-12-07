package com.orders.management.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.orders.management.app.Constants;
import com.orders.management.app.models.Event;
import com.orders.management.app.models.User2;
import com.orders.management.app.persenters.event.EventsCallback;
import com.orders.management.app.persenters.event.EventsPresenter;
import com.orders.management.app.persenters.user.UsersCallback;
import com.orders.management.app.persenters.user.UsersPresenter;
import com.orders.management.app.utilities.DatesUtils;
import com.orders.management.app.utilities.NumbersUtils;
import com.orders.management.app.utilities.helpers.LocaleHelper;
import com.orders.management.app.utilities.helpers.StorageHelper;
import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.ActivityEventViewerBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventViewerActivity extends BaseActivity implements UsersCallback, EventsCallback {
    private ActivityEventViewerBinding binding;
    private UsersPresenter usersPresenter;
    private EventsPresenter eventsPresenter;
    private Event event;
    private User2 currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, getCurrentLanguage().getLanguage());
        super.onCreate(savedInstanceState);
        binding = ActivityEventViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usersPresenter = new UsersPresenter(this);
        eventsPresenter = new EventsPresenter(this);
        currentUser = StorageHelper.getCurrentUser();

        event = getIntent().getParcelableExtra(Constants.ARG_OBJECT);
        bind();

        binding.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventViewerActivity.this, ReviewsActivity.class);
                intent.putExtra(Constants.ARG_OBJECT, event);
                startActivity(intent);
            }
        });
        binding.counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventViewerActivity.this, RegistersActivity.class);
                intent.putExtra(Constants.ARG_OBJECT, event);
                startActivity(intent);
            }
        });
        binding.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventViewerActivity.this, ReviewsActivity.class);
                intent.putExtra(Constants.ARG_OBJECT, event);
                startActivity(intent);
            }
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var registers = event.getRegisters();
                var events = currentUser.getEvents();

                registers.add(currentUser.getId());
                event.setRegisters(registers);
                eventsPresenter.save(event);

                events.add(event.getId());
                currentUser.setEvents(events);
                usersPresenter.save(currentUser);
            }
        });

        binding.btnUnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var registers = event.getRegisters();
                var events = currentUser.getEvents();

                registers.remove(currentUser.getId());
                event.setRegisters(registers);
                eventsPresenter.save(event);

                events.remove(event.getId());
                currentUser.setEvents(events);
                usersPresenter.save(currentUser);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventsPresenter.getEvent(event.getId());
    }

    @Override
    public void onGetEventComplete(Event event) {
        this.event = event;
        bind();
    }

    @Override
    public void onSaveEventComplete() {
        Toast.makeText(this, R.string.str_message_added_successfully, Toast.LENGTH_LONG).show();
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
        binding.review.setRating(event.getRating());
        binding.reviewValue.setText(NumbersUtils.round(event.getRating(), 1) + "");

        List<String> registers = event.getRegisters() != null ? event.getRegisters() : new ArrayList<>();
        List<String> currentUserEvents = currentUser.getEvents();
        binding.counter.setText(registers.size() + "");
        binding.btnRegister.setVisibility(!currentUserEvents.contains(event.getId()) ? View.VISIBLE : View.GONE);
        binding.btnUnRegister.setVisibility(currentUserEvents.contains(event.getId()) ? View.VISIBLE : View.GONE);

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
        }
    }

    private Calendar getEventCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getDate());
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
        }
    }
}