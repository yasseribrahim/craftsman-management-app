package com.orders.management.app.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.orders.management.app.Constants;
import com.orders.management.app.models.Event;
import com.orders.management.app.models.Review;
import com.orders.management.app.ui.adptres.ReviewsAdapter;
import com.orders.management.app.ui.fragments.ReviewBottomSheet;
import com.orders.management.app.utilities.NumbersUtils;
import com.orders.management.university.app.R;
import com.orders.management.university.app.databinding.ActivityReviewsBinding;
import com.orders.management.app.persenters.event.EventsCallback;
import com.orders.management.app.persenters.event.EventsPresenter;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends BaseActivity implements EventsCallback, ReviewBottomSheet.OnReviewCallback, ReviewsAdapter.OnItemClickListener {
    private ActivityReviewsBinding binding;
    private EventsPresenter presenter;
    private ReviewsAdapter adapter;
    private List<Review> reviews;
    private Event event;
    private boolean isDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new EventsPresenter(this);
        event = getIntent().getParcelableExtra(Constants.ARG_OBJECT);

        binding.btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var dialog = ReviewBottomSheet.newInstance();
                dialog.show(getSupportFragmentManager(), "");
            }
        });

        binding.refreshLayout.setColorSchemeResources(R.color.refreshColor1, R.color.refreshColor2, R.color.refreshColor3, R.color.refreshColor4);
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        reviews = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReviewsAdapter(reviews, this);
        binding.recyclerView.setAdapter(adapter);
        bind();
    }

    private void load() {
        presenter.getEvent(event.getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public void onGetEventComplete(Event event) {
        this.event = event;
        bind();
    }

    @Override
    public void onReviewCallback(Review review) {
        int index = event.getReviews().indexOf(review);
        if (index != -1) {
            event.getReviews().set(index, review);
        } else {
            event.getReviews().add(review);
        }
        presenter.save(event);
    }

    private void bind() {
        binding.review.setRating(event.getRating());
        binding.reviewValue.setText(NumbersUtils.round(event.getRating(), 1) + "");
        this.reviews.clear();
        this.reviews.addAll(event.getReviews());
        adapter.notifyDataSetChanged();

        binding.message.setVisibility(reviews.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onShowLoading() {
        binding.refreshLayout.setRefreshing(true);
    }

    @Override
    public void onHideLoading() {
        binding.refreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(String message, View.OnClickListener listener) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemViewListener(Review review) {

    }

    @Override
    public void onDeleteItemViewListener(Review review) {
        int position = reviews.indexOf(review);
        reviews.remove(review);
        adapter.notifyItemRemoved(position);
        event.setReviews(reviews);
        isDelete = true;
        presenter.save(event);
    }

    @Override
    public void onSaveEventComplete() {
        if (isDelete) {
            Toast.makeText(this, R.string.str_message_delete_successfully, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.str_message_updated_successfully, Toast.LENGTH_LONG).show();
        }
        isDelete = false;
        load();
    }
}