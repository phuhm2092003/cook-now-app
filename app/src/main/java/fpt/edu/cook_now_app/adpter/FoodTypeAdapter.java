package fpt.edu.cook_now_app.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fpt.edu.cook_now_app.R;
import fpt.edu.cook_now_app.model.FoodType;

public class FoodTypeAdapter extends RecyclerView.Adapter<FoodTypeAdapter.FoodTypeViewHolder> {
    private List<FoodType> list;

    public FoodTypeAdapter(List<FoodType> list) {
        this.list = list;
    }

    public void setFoodTypes(List<FoodType> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FoodTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_type, parent, false);
        return new FoodTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodTypeViewHolder holder, int position) {
        FoodType foodType = list.get(position);
        if (foodType == null) {
            return;
        }

        holder.name.setText(foodType.getName());
        Glide.with(holder.itemView)
                .load(foodType.getImage())
                .placeholder(R.drawable.placeholder_food_type)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class FoodTypeViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public FoodTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }
    }
}
