package com.craftsman.management.app.ui.adptres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.craftsman.management.app.R;
import com.craftsman.management.app.databinding.ItemPriceBinding;
import com.craftsman.management.app.models.Price;
import com.craftsman.management.app.utilities.DatesUtils;
import com.craftsman.management.app.utilities.helpers.StorageHelper;

import java.util.List;

public class PricesAdapter extends RecyclerView.Adapter<PricesAdapter.ViewHolder> {
    private List<Price> prices;
    private OnPricesClickListener listener;
    private boolean canChat, canAccepted;

    public PricesAdapter(List<Price> prices, OnPricesClickListener listener, boolean canChat, boolean canAccepted) {
        this.prices = prices;
        this.listener = listener;
        this.canChat = canChat;
        this.canAccepted = canAccepted;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Price price = prices.get(position);

        holder.binding.username.setText(price.getCraftsmanId());
        holder.binding.price.setText(price.getPrice() + "");
        holder.binding.date.setText(DatesUtils.formatDate(price.getDate()));

        var canEdit = StorageHelper.getCurrentUser().getId().equalsIgnoreCase(price.getCraftsmanId());
        holder.binding.containerActions.setVisibility(canEdit ? View.VISIBLE : View.GONE);
    }

    private int getSize(String id) {
        return prices.size();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return prices.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPriceBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemPriceBinding.bind(view);
            binding.btnChat.setVisibility(canChat ? View.VISIBLE : View.GONE);
            binding.accepted.setVisibility(canAccepted ? View.VISIBLE : View.GONE);
            binding.containerRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onPriceDeleteListener(prices.get(getAdapterPosition()));
                }
            });
            binding.containerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onPriceEditListener(prices.get(getAdapterPosition()));
                }
            });
            binding.btnChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onPriceChatListener(prices.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnPricesClickListener {

        void onPriceEditListener(Price price);

        void onPriceDeleteListener(Price price);

        void onPriceChatListener(Price price);
    }
}