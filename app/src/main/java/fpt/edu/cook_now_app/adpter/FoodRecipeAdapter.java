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
import fpt.edu.cook_now_app.interfaces.ItemFoodRecipeOnClick;
import fpt.edu.cook_now_app.model.FoodRecipe;
import fpt.edu.cook_now_app.model.FoodType;

public class FoodRecipeAdapter extends RecyclerView.Adapter<FoodRecipeAdapter.FoodRecipesViewHolder> {
    private List<FoodRecipe> listFoodRecipe;
    private ItemFoodRecipeOnClick itemFoodRecipeOnClick;

    public FoodRecipeAdapter(List<FoodRecipe> listFoodRecipe, ItemFoodRecipeOnClick itemFoodRecipeOnClick) {
        this.listFoodRecipe = listFoodRecipe;
        this.itemFoodRecipeOnClick = itemFoodRecipeOnClick;
    }

    public void setFoodReicepListFilter(List<FoodRecipe> list) {
        this.listFoodRecipe = list;
    }

    @NonNull
    @Override
    public FoodRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_recipe, parent, false);

        return new FoodRecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRecipesViewHolder holder, int position) {
        FoodRecipe foodRecipe = listFoodRecipe.get(position);
        if (foodRecipe == null){
            return;
        }

        holder.nameFood.setText(foodRecipe.getName());
        Glide.with(holder.itemView)
                .load(foodRecipe.getImage())
                .placeholder(R.drawable.placeholder_food)
                .into(holder.imageFood);
        holder.itemView.setOnClickListener(view -> {
            itemFoodRecipeOnClick.onItemOnClick(foodRecipe);
        });
    }

    @Override
    public int getItemCount() {
        return listFoodRecipe == null ? 0 : listFoodRecipe.size();
    }

    public static class FoodRecipesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFood;
        TextView nameFood;

        public FoodRecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.imageFood);
            nameFood = itemView.findViewById(R.id.nameFood);
        }
    }
}
