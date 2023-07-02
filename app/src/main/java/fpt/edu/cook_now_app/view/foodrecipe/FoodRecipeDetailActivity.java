package fpt.edu.cook_now_app.view.foodrecipe;

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
import fpt.edu.cook_now_app.view.HomeFragment;

public class FoodRecipeDetailActivity extends AppCompatActivity {
    private ImageView image, backButtom;
    private TextView name;
    private RecyclerView ingredientReacyclerView, stepRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_recipe_detail);
        initView();
        fetchDataAcitvityFromFirBase();
    }

    private void fetchDataAcitvityFromFirBase() {
        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference("recipes");
        int id = getIntent().getIntExtra(FoodRecipesActivity.EXTRA_ID, 0);
        Query query = recipeRef.orderByChild("id").equalTo(id);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FoodRecipe foodRecipe = snapshot.getValue(FoodRecipe.class);

                Glide.with(FoodRecipeDetailActivity.this)
                        .load(foodRecipe.getImage())
                        .into(image);
                name.setText(foodRecipe.getName());
                fetchIngredientRecyclerView(foodRecipe);
                fetchStepRecyclerView(foodRecipe);
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

    private void initView() {
        image = findViewById(R.id.image);
        backButtom = findViewById(R.id.backButtom);
        name = findViewById(R.id.name);
        ingredientReacyclerView = findViewById(R.id.ingredientReacyclerView);
        stepRecyclerView = findViewById(R.id.stepReacyclerView);
        backButtom.setOnClickListener(view -> onBackPressed());
    }

    private void fetchIngredientRecyclerView(FoodRecipe foodRecipe) {
        // Lấy dữ liệu các nguyên liệu nấu ăn gán cho recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FoodRecipeDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        ingredientReacyclerView.setLayoutManager(linearLayoutManager);
        IngredientAdapter ingredientAdapter = new IngredientAdapter(foodRecipe.getIngredients());
        ingredientReacyclerView.setAdapter(ingredientAdapter);
        ingredientReacyclerView.setNestedScrollingEnabled(false);
    }

    private void fetchStepRecyclerView(FoodRecipe foodRecipe) {
        // Lấy dữ liệu các bước nấu ăn gán cho recyclerview
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(FoodRecipeDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        stepRecyclerView.setLayoutManager(linearLayoutManager1);
        StepAdapter stepAdapter = new StepAdapter(foodRecipe.getSteps());
        stepRecyclerView.setAdapter(stepAdapter);
        stepRecyclerView.setNestedScrollingEnabled(false);
    }

}