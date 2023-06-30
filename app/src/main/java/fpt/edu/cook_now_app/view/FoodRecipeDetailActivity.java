package fpt.edu.cook_now_app.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import fpt.edu.cook_now_app.R;
import fpt.edu.cook_now_app.adpter.IngredientAdapter;
import fpt.edu.cook_now_app.adpter.StepAdapter;
import fpt.edu.cook_now_app.model.FoodRecipe;

public class FoodRecipeDetailActivity extends AppCompatActivity {
    private ImageView image;
    private TextView name;
    private RecyclerView ingredientReacyclerView, stepReacyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_recipe_detail);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        ingredientReacyclerView = findViewById(R.id.ingredientReacyclerView);
        stepReacyclerView = findViewById(R.id.stepReacyclerView);

        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference("recipes");
        int id = getIntent().getIntExtra(HomeFragment.EXTRA_ID, 0);
        Query query = recipeRef.orderByChild("id").equalTo(id);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FoodRecipe foodRecipe = snapshot.getValue(FoodRecipe.class);

                Glide.with(FoodRecipeDetailActivity.this)
                        .load(foodRecipe.getImage())
                        .into(image);
                name.setText(foodRecipe.getName());

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FoodRecipeDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                ingredientReacyclerView.setLayoutManager(linearLayoutManager);
                IngredientAdapter ingredientAdapter = new IngredientAdapter(foodRecipe.getIngredients());
                ingredientReacyclerView.setAdapter(ingredientAdapter);
                ingredientReacyclerView.setNestedScrollingEnabled(false);

                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(FoodRecipeDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                stepReacyclerView.setLayoutManager(linearLayoutManager1);
                StepAdapter stepAdapter = new StepAdapter(foodRecipe.getSteps());
                stepReacyclerView.setAdapter(stepAdapter);
                stepReacyclerView.setNestedScrollingEnabled(false);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}