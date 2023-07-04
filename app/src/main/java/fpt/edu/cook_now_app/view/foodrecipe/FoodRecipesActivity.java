package fpt.edu.cook_now_app.view.foodrecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.cook_now_app.R;
import fpt.edu.cook_now_app.adpter.FoodRecipeAdapter;
import fpt.edu.cook_now_app.model.FoodRecipe;
import fpt.edu.cook_now_app.view.HomeFragment;

public class FoodRecipesActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "id";
    private Toolbar toolbar;
    private TextView nameFoodType;
    private RecyclerView foodRecipesRecyclerView;
    private FoodRecipeAdapter foodRecipeAdapter;
    private List<FoodRecipe> foodRecipeList;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_recipes);

        initView();
        setTitleToolBar();
        setUpFoodRecipesRecyclerView();
        fetchFoodListFormFireBase();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        foodRecipesRecyclerView = findViewById(R.id.foodRecipesRecyclerView);
        nameFoodType = findViewById(R.id.nameFoodType);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout);
    }

    private void setTitleToolBar() {
        String name = getIntent().getStringExtra(HomeFragment.EXTRA_NAME);
        nameFoodType.setText(name);
    }

    private void setUpFoodRecipesRecyclerView() {
        foodRecipeList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        foodRecipesRecyclerView.setLayoutManager(linearLayoutManager);

        foodRecipeAdapter = new FoodRecipeAdapter(foodRecipeList, foodRecipe -> launchFoodRecipeDetailActivity(foodRecipe));
        foodRecipesRecyclerView.setAdapter(foodRecipeAdapter);

        foodRecipesRecyclerView.setNestedScrollingEnabled(false);
    }

    private void launchFoodRecipeDetailActivity(FoodRecipe foodRecipe) {
        Intent intent = new Intent(FoodRecipesActivity.this, FoodRecipeDetailActivity.class);
        intent.putExtra(EXTRA_ID, foodRecipe.getId());
        startActivity(intent);
    }

    private void fetchFoodListFormFireBase() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        foodRecipesRecyclerView.setVisibility(View.GONE);
        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference("recipes");
        int id = getIntent().getIntExtra(HomeFragment.EXTRA_ID, 0);
        Query query = recipeRef.orderByChild("id_type").equalTo(id);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FoodRecipe foodRecipe = snapshot.getValue(FoodRecipe.class);
                if (foodRecipe != null) {
                    int index = -1;
                    for (int i = 0; i < foodRecipeList.size(); i++) {
                        if (foodRecipe.getId() == foodRecipeList.get(i).getId()) {
                            index = i;
                        }
                    }
                    if (index != -1) {
                        foodRecipeList.set(index, foodRecipe);
                        foodRecipeAdapter.notifyItemChanged(index);
                    } else {
                        foodRecipeList.add(foodRecipe);
                        foodRecipeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FoodRecipe foodRecipeUpdate = snapshot.getValue(FoodRecipe.class);
                if (foodRecipeUpdate != null) {
                    int index = -1;
                    for (int i = 0; i < foodRecipeList.size(); i++) {
                        if (foodRecipeUpdate.getId() == foodRecipeList.get(i).getId()) {
                            index = i;
                        }
                    }

                    if (index != -1) {
                        foodRecipeList.set(index, foodRecipeUpdate);
                        foodRecipeAdapter.notifyItemChanged(index);
                    }
                }
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                foodRecipesRecyclerView.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }

}