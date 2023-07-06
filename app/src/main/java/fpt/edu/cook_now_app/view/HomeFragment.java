package fpt.edu.cook_now_app.view;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.cook_now_app.R;
import fpt.edu.cook_now_app.adpter.FoodTypeAdapter;
import fpt.edu.cook_now_app.model.FoodType;
import fpt.edu.cook_now_app.view.foodrecipe.FoodRecipesActivity;

public class HomeFragment extends Fragment {
    public static final int SHIMMER_LAYOUT_HIDE_DELAY = 2000;
    private RecyclerView foodTypeRecyclerView;
    private FoodTypeAdapter foodTypeAdapter;
    private List<FoodType> foodTypeList;
    private EditText searchEditText;
    private ShimmerFrameLayout shimmerFrameLayout;
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_NAME = "name";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpFoodTypeRecyclerView();
        fetchFoodTypeListFromFirebase();
        setEventOnChangeTextSearchEditText();
    }

    private void initView(@NonNull View view) {
        foodTypeRecyclerView = view.findViewById(R.id.foodTypeRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
    }

    private void setUpFoodTypeRecyclerView() {
        foodTypeList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        foodTypeRecyclerView.setLayoutManager(gridLayoutManager);

        foodTypeAdapter = new FoodTypeAdapter(foodTypeList, foodType -> launchFoodRecipesActivity(foodType));
        foodTypeRecyclerView.setAdapter(foodTypeAdapter);
        foodTypeRecyclerView.setNestedScrollingEnabled(false);
    }

    private void launchFoodRecipesActivity(FoodType foodType) {
        Intent intent = new Intent(getActivity(), FoodRecipesActivity.class);
        intent.putExtra(EXTRA_ID, foodType.getId());
        intent.putExtra(EXTRA_NAME, foodType.getName());
        startActivity(intent);
    }

    private void fetchFoodTypeListFromFirebase() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        foodTypeRecyclerView.setVisibility(View.GONE);

        // Get data from real time database firebase
        DatabaseReference foodTypeReference = FirebaseDatabase.getInstance().getReference("foods_type");
        foodTypeReference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FoodType foodType = snapshot.getValue(FoodType.class);
                if (foodType != null) {
                    int index = findFoodTypeIndex(foodType.getId());
                    if (index != -1) {
                        foodTypeList.set(index, foodType);
                        foodTypeAdapter.notifyItemChanged(index);
                    } else {
                        foodTypeList.add(foodType);
                        foodTypeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FoodType foodTypeUpdate = snapshot.getValue(FoodType.class);
                if (foodTypeUpdate != null) {
                    int index = findFoodTypeIndex(foodTypeUpdate.getId());

                    if (index != -1) {
                        foodTypeList.set(index, foodTypeUpdate);
                        foodTypeAdapter.notifyItemChanged(index);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                FoodType foodTypeRemoved = snapshot.getValue(FoodType.class);
                if (foodTypeRemoved != null) {
                    int index = findFoodTypeIndex(foodTypeRemoved.getId());

                    if (index != -1) {
                        foodTypeList.remove(index);
                        foodTypeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new Handler().postDelayed(() -> {
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmer();
            foodTypeRecyclerView.setVisibility(View.VISIBLE);
        }, SHIMMER_LAYOUT_HIDE_DELAY);
    }

    private int findFoodTypeIndex(int idFoodType) {
        int index = -1;
        for (int i = 0; i < foodTypeList.size(); i++) {
            if (foodTypeList.get(i).getId() == idFoodType) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void setEventOnChangeTextSearchEditText() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterFoodTypeListByName(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void filterFoodTypeListByName(CharSequence charSequence) {
        String searchText = charSequence.toString().trim().toLowerCase();
        List<FoodType> foodTypeListFilter;

        if (searchText.isEmpty()) {
            foodTypeListFilter = foodTypeList;
        } else {
            foodTypeListFilter = new ArrayList<>();
            for (FoodType foodType : foodTypeList) {
                if (foodType.getName().toLowerCase().contains(searchText)) {
                    foodTypeListFilter.add(foodType);
                }
            }
        }

        foodTypeAdapter.setFoodTypeListFilter(foodTypeListFilter);
        foodTypeAdapter.notifyDataSetChanged();
    }
}