package com.craftsman.management.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.craftsman.management.app.R;
import com.craftsman.management.app.databinding.ItemStudentBinding;
import com.craftsman.management.app.models.User;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> {
    private List<User> users;
    private OnItemClickListener listener;

    // data is passed into the constructor
    public StudentsAdapter(List<User> users, OnItemClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);

        holder.binding.name.setText(user.getFullName());
        holder.binding.username.setText(user.getUsername());
        holder.binding.phone.setText(user.getPhone());
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
        ItemStudentBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemStudentBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemViewListener(users.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemViewListener(User user);
    }
}