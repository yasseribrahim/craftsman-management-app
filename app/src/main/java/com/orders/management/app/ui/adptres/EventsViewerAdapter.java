package com.orders.management.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.orders.management.app.models.Event;
import com.orders.management.app.utilities.NumbersUtils;
import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.ItemEventViewerBinding;
import com.orders.management.app.utilities.DatesUtils;

import java.util.List;

public class EventsViewerAdapter extends RecyclerView.Adapter<EventsViewerAdapter.ViewHolder> {
    private List<Event> events;
    private OnEventsClickListener listener;

    public EventsViewerAdapter(List<Event> events, OnEventsClickListener listener) {
        this.events = events;
        this.listener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_viewer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = events.get(position);

        holder.binding.title.setText(event.getTitle());
        holder.binding.address.setText(event.getAddress());
        holder.binding.review.setRating(event.getRating());
        holder.binding.reviewValue.setText(NumbersUtils.round(event.getRating(), 1) + "");
        holder.binding.description.setText(event.getDescription());
        holder.binding.createdBy.setText(event.getCreatedBy());
        holder.binding.description.setVisibility(event.getDescription() != null && !event.getDescription().isEmpty() ? View.VISIBLE : View.GONE);
        holder.binding.date.setText(DatesUtils.formatDateOnly(event.getDate()));
        holder.binding.time.setText(DatesUtils.formatTimeOnly(event.getDate()));
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
        ItemEventViewerBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemEventViewerBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onEventViewListener(events.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnEventsClickListener {
        void onEventViewListener(Event event);
    }
}