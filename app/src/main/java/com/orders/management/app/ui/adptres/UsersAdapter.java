package com.orders.management.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.orders.management.app.utilities.helpers.StorageHelper;
import com.bumptech.glide.Glide;
import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.ItemUserBinding;
import com.orders.management.app.models.User2;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<User2> users;
    private OnItemClickListener listener;

    // data is passed into the constructor
    public UsersAdapter(List<User2> users, OnItemClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User2 user = users.get(position);

        holder.binding.name.setText(user.getFullName());
        holder.binding.username.setText(user.getUsername());
        holder.binding.phone.setText(user.getPhone());
        holder.binding.containerRemove.setVisibility(StorageHelper.getCurrentUser() != null ? StorageHelper.getCurrentUser().getId().equalsIgnoreCase(user.getId()) ? View.GONE : View.VISIBLE : View.GONE);
        if (user.getAddress() != null) {
            holder.binding.address.setText(user.getAddress());
        } else {
            holder.binding.address.setText("---");
        }
        Glide.with(holder.itemView.getContext()).load(user.getImageProfile()).placeholder(R.drawable.ic_profile).into(holder.binding.image);
    }

    private int getSize(String id) {
        return users.size();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return users.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemUserBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemUserBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onItemViewListener(getAdapterPosition());
                }
            });
            binding.containerRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onDeleteItemViewListener(getAdapterPosition());
                }
            });
            binding.containerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onItemEditListener(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemViewListener(int position);

        void onItemEditListener(int position);

        void onDeleteItemViewListener(int position);
    }
}