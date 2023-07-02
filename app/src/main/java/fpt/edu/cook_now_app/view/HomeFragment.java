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
    private RecyclerView foodTypeRecyclerView;
    private FoodTypeAdapter foodTypeAdapter;
    private List<FoodType> foodTypeList;
    private EditText searchEditText;
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
        filterFoodTypeList();
    }

    private void initView(@NonNull View view) {
        foodTypeRecyclerView = view.findViewById(R.id.foodTypeRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
    }

    private void setUpFoodTypeRecyclerView() {
        foodTypeList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
        foodTypeRecyclerView.setLayoutManager(linearLayoutManager);

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
        DatabaseReference foodTypeRef = FirebaseDatabase.getInstance().getReference("foods_type");
        foodTypeRef.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FoodType foodType = snapshot.getValue(FoodType.class);
                if (foodType != null) {
                    int index = -1;
                    for (int i = 0; i < foodTypeList.size(); i++) {
                        if (foodTypeList.get(i).getId() == foodType.getId()) {
                            index = i;
                            break;
                        }
                    }

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
                    int index = -1;
                    for (int i = 0; i < foodTypeList.size(); i++) {
                        if (foodTypeList.get(i).getId() == foodTypeUpdate.getId()) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {
                        foodTypeList.set(index, foodTypeUpdate);
                        foodTypeAdapter.notifyItemChanged(index);
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

    private void filterFoodTypeList() {
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