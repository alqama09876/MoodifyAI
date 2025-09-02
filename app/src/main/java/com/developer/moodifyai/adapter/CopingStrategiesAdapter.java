package com.developer.moodifyai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.moodifyai.R;
import com.developer.moodifyai.coping_strategies.CopingDetailsScreen;
import com.developer.moodifyai.model.CopingStrategy;

import java.util.ArrayList;
import java.util.List;

public class CopingStrategiesAdapter extends RecyclerView.Adapter<CopingStrategiesAdapter.ViewHolder> {
    Context context;
    List<CopingStrategy> copingStrategies;

    public CopingStrategiesAdapter(Context context, List<CopingStrategy> copingStrategies) {
        this.context = context;
        this.copingStrategies = copingStrategies;
    }

    @NonNull
    @Override
    public CopingStrategiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_coping_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CopingStrategiesAdapter.ViewHolder holder, int position) {
        CopingStrategy copingStrategy = copingStrategies.get(position);
        holder.tv_csTitle.setText(copingStrategy.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CopingDetailsScreen.class);
            intent.putExtra("title", copingStrategy.getTitle());
            intent.putExtra("description", copingStrategy.getDescription());
            intent.putExtra("subTitle", copingStrategy.getSubTitle());
            intent.putExtra("subDescription", (ArrayList<String>) copingStrategy.getSubDescription());
            intent.putExtra("image", copingStrategy.getImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return copingStrategies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_csTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_csTitle = itemView.findViewById(R.id.tv_csTitle);
        }
    }
}