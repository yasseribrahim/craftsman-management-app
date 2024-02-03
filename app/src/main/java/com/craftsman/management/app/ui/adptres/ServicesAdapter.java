package com.craftsman.management.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.craftsman.management.app.R;
import com.craftsman.management.app.databinding.ItemServiceBinding;
import com.craftsman.management.app.models.Service;
import com.craftsman.management.app.utilities.DatesUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {
    private List<Service> services;
    private OnServicesClickListener listener;
    private boolean canEdit;

    public ServicesAdapter(List<Service> services, OnServicesClickListener listener, boolean canEdit) {
        this.services = services;
        this.listener = listener;
        this.canEdit = canEdit;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Service service = services.get(position);

        holder.binding.title.setText(service.getTitle());
        holder.binding.prices.setText(holder.binding.prices.getContext().getString(R.string.str_prices, service.getPrices() != null ? service.getPrices().size() + "" : "0"));
        holder.binding.description.setText(service.getDescription());
        holder.binding.description.setVisibility(service.getDescription() != null && !service.getDescription().isEmpty() ? View.VISIBLE : View.GONE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(service.getDate().getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        holder.binding.date.setText(format.format(calendar.getTime()));
        holder.binding.category.setText(service.getCategoryName());
        holder.binding.username.setText(service.getCreatedBy());

        holder.binding.date.setText(DatesUtils.formatDateOnly(service.getDate()));

        holder.binding.image.setVisibility(service.getImageUrl() != null && !service.getImageUrl().isEmpty() ? View.VISIBLE : View.GONE);
        Glide.with(holder.itemView.getContext()).load(service.getImageUrl()).placeholder(R.drawable.default_image).into(holder.binding.image);
    }

    private int getSize(String id) {
        return services.size();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return services.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemServiceBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemServiceBinding.bind(view);
            binding.containerActions.setVisibility(canEdit ? View.VISIBLE : View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onServiceViewListener(services.get(getAdapterPosition()));
                }
            });
            binding.containerRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onServiceDeleteListener(services.get(getAdapterPosition()));
                }
            });
            binding.containerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onServiceEditListener(services.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnServicesClickListener {
        void onServiceViewListener(Service service);

        void onServiceEditListener(Service service);

        void onServiceDeleteListener(Service service);
    }
}