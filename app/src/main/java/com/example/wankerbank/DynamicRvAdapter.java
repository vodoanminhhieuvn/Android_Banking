package com.example.wankerbank;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wankerbank.DVRInterface.LoadMore;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class LoadingViewHolder extends RecyclerView.ViewHolder{

    public ProgressBar progressBar;

    public LoadingViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder{

    public TextView type;
    public TextView fund;
    public TextView date;
    public ImageView card_icon;

    public ItemViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        type = itemView.findViewById(R.id.Type_Text);
        fund = itemView.findViewById(R.id.Fund_Text);
        date = itemView.findViewById(R.id.Date_Text);
        card_icon = itemView.findViewById(R.id.Card_Icon);
    }
}

public class DynamicRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<DynamicRvModel> items;
    int visibleThreshold = 5;

    public DynamicRvAdapter(RecyclerView recyclerView, Activity activity, List<DynamicRvModel> items) {
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null) {
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore (LoadMore loadMore) {
        this.loadMore = loadMore;
    }

    int lastVisibleItem, totalItemCount;

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.dynamic_rv_item_layout, parent, false);
            return new ItemViewHolder(view);
        }
        else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.dynamic_rv_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            Log.w("ON CALLED", "TRUE");
            DynamicRvModel item = items.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;

            if (items.get(position).getType().equals("Transfer")) {
                viewHolder.type.setTextColor(Color.parseColor("#eb596e"));
                viewHolder.card_icon.setImageResource(R.drawable.transfer);
            }
            else if (items.get(position).getType().equals("Deposit")) {
                viewHolder.type.setTextColor(Color.parseColor("#5eaaa8"));
                viewHolder.fund.setTextColor(Color.parseColor("#5eaaa8"));
                viewHolder.card_icon.setImageResource(R.drawable.deposit);
            }

            else if (items.get(position).getType().equals("Withdrawal")) {
                viewHolder.type.setTextColor(Color.parseColor("#eb596e"));
                viewHolder.card_icon.setImageResource(R.drawable.withdrawal);
            }

            viewHolder.type.setText(items.get(position).getType());
            viewHolder.date.setText(items.get(position).getDate());
            viewHolder.fund.setText(items.get(position).getFund());


        }

        else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
        }
   }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
}
