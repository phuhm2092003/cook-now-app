package fpt.edu.cook_now_app.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.cook_now_app.R;
import fpt.edu.cook_now_app.adpter.FoodRecipeAdapter;
import fpt.edu.cook_now_app.model.FoodRecipe;
import fpt.edu.cook_now_app.view.foodrecipe.FoodRecipeDetailActivity;
import fpt.edu.cook_now_app.view.foodrecipe.FoodRecipesActivity;

public class SearchFragment extends Fragment {
    private RecyclerView foodRecipesRecyclerView;
    private FoodRecipeAdapter foodRecipeAdapter;
    private List<FoodRecipe> foodRecipeList;
    private EditText searchEditText;
    private TextView resultSearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpFoodRecipesRecyclerView();
        fetchFoodListFormFireBase();
        filterFoodRecipeList();
    }

    private void initView(@NonNull View view) {
        foodRecipesRecyclerView = view.findViewById(R.id.foodRecipesRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        resultSearch = view.findViewById(R.id.resultSearch);
    }


    private void setUpFoodRecipesRecyclerView() {
        foodRecipeList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        foodRecipesRecyclerView.setLayoutManager(linearLayoutManager);

        foodRecipeAdapter = new FoodRecipeAdapter(foodRecipeList, foodRecipe -> launchFoodRecipeDetailActivity(foodRecipe));
        foodRecipesRecyclerView.setAdapter(foodRecipeAdapter);

        foodRecipesRecyclerView.setNestedScrollingEnabled(false);
    }

    private void launchFoodRecipeDetailActivity(FoodRecipe foodRecipe) {
        Intent intent = new Intent(getActivity(), FoodRecipeDetailActivity.class);
        intent.putExtra("id", foodRecipe.getId());
        startActivity(intent);
    }


    private void fetchFoodListFormFireBase() {
        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference("recipes");
        recipeRef.addChildEventListener(new ChildEventListener() {
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
    }

    private void filterFoodRecipeList() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterFoodRecipeByName(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void filterFoodRecipeByName(CharSequence charSequence) {
        String searchText = charSequence.toString().trim().toLowerCase();
        List<FoodRecipe> foodRecipesFilter;

        if (searchText.isEmpty()) {
            foodRecipesFilter = foodRecipeList;
            foodRecipesRecyclerView.setVisibility(View.GONE);
        } else {
            foodRecipesFilter = new ArrayList<>();
            for (FoodRecipe foodRecipe : foodRecipeList) {
                if (foodRecipe.getName().toLowerCase().contains(searchText)) {
                    foodRecipesFilter.add(foodRecipe);
                }
            }
            if(foodRecipesFilter.size() == 0){
                foodRecipesRecyclerView.setVisibility(View.GONE);
                resultSearch.setVisibility(View.VISIBLE);
            }else {
                foodRecipesRecyclerView.setVisibility(View.VISIBLE);
                resultSearch.setVisibility(View.GONE);
            }
        }

        foodRecipeAdapter.setFoodReicepListFilter(foodRecipesFilter);
        foodRecipeAdapter.notifyDataSetChanged();
    }
}