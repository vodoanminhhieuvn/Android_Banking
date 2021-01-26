package com.example.wankerbank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StaticRvAdapter extends RecyclerView.Adapter<StaticRvAdapter.StaticRVViewHolder> {

    private ArrayList<StaticRvModel> items;
    int row_index = -1;

    public StaticRvAdapter(ArrayList<StaticRvModel> items) {
        this.items = items;
    }

    @NotNull
    @Override
    public StaticRVViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
       ViewGroup root;
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.static_rv_item, parent, false);

       StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);

       return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull StaticRVViewHolder holder, final int position) {
        StaticRvModel currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
            }
        });

        if (row_index == position) {
            holder.linearLayout.setBackgroundResource(R.drawable.static_bg);
        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.static_selected_bg);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRVViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        public StaticRVViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Fund_Text);
            imageView = itemView.findViewById(R.id.Transaction_Icon);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
