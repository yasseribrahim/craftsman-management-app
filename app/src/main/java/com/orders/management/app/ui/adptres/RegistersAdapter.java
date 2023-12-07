package com.orders.management.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.ItemRegisterBinding;
import com.orders.management.app.models.User2;
import com.orders.management.app.utilities.helpers.StorageHelper;
import com.bumptech.glide.Glide;

import java.util.List;

public class RegistersAdapter extends RecyclerView.Adapter<RegistersAdapter.ViewHolder> {
    private final List<User2> users;
    private final OnItemClickListener listener;

    // data is passed into the constructor
    public RegistersAdapter(List<User2> users, OnItemClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_register, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User2 user = users.get(position);

        holder.binding.username.setText(user.getUsername());
        holder.binding.name.setText(user.getFullName());
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

    public interface OnItemClickListener {
        void onItemViewListener(User2 user);

        void onDeleteItemViewListener(User2 user);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemRegisterBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemRegisterBinding.bind(view);

            binding.btnDelete.setVisibility(StorageHelper.getCurrentUser().isAdmin() ? View.VISIBLE : View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemViewListener(users.get(getAdapterPosition()));
                }
            });

            binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onDeleteItemViewListener(users.get(getAdapterPosition()));
                }
            });
        }
    }
}