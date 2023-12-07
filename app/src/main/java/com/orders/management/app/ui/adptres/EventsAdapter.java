package com.orders.management.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.orders.management.app.models.Event;
import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.ItemEventBinding;
import com.orders.management.app.models.User2;
import com.orders.management.app.utilities.DatesUtils;
import com.orders.management.app.utilities.helpers.StorageHelper;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private List<Event> events;
    private OnEventsClickListener listener;
    private User2 currentUser;

    public EventsAdapter(List<Event> events, OnEventsClickListener listener) {
        this.events = events;
        this.listener = listener;
        this.currentUser = StorageHelper.getCurrentUser();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = events.get(position);

        holder.binding.title.setText(event.getTitle());
        holder.binding.address.setText(event.getAddress());
        holder.binding.description.setText(event.getDescription());
        holder.binding.createdBy.setText(event.getCreatedBy());
        holder.binding.description.setVisibility(event.getDescription() != null && !event.getDescription().isEmpty() ? View.VISIBLE : View.GONE);
        holder.binding.date.setText(DatesUtils.formatDateOnly(event.getDate()));
        holder.binding.time.setText(DatesUtils.formatTimeOnly(event.getDate()));
        holder.binding.visibility.setImageDrawable(ResourcesCompat.getDrawable(holder.binding.visibility.getResources(), event.isApproved() ? R.drawable.ic_visibility_on : R.drawable.ic_visibility_off, null));
    }

    private int getSize(String id) {
        return events.size();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return events.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemEventBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemEventBinding.bind(view);
            binding.containerActions.setVisibility(currentUser.isAdmin() ? View.VISIBLE : View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onEventViewListener(events.get(getAdapterPosition()));
                }
            });
            binding.containerRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onEventDeleteListener(events.get(getAdapterPosition()));
                }
            });
            binding.containerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onEventEditListener(events.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnEventsClickListener {
        void onEventViewListener(Event event);

        void onEventEditListener(Event event);

        void onEventDeleteListener(Event event);
    }
}