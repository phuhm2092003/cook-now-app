package fpt.edu.cook_now_app.view;

import android.annotation.SuppressLint;
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

public class HomeFragment extends Fragment {
    private RecyclerView foodTypesRecyclerView;
    private FoodTypeAdapter foodTypeAdapter;
    private List<FoodType> foodTypes;
    private EditText searchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        setUpRecyclerView();
        loadDataFromFireBase();
        filterDataFoodTypes();
    }

    private void initView(@NonNull View view) {
        foodTypesRecyclerView = view.findViewById(R.id.foodTypesRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
    }

    private void setUpRecyclerView() {
        foodTypes = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
        foodTypesRecyclerView.setLayoutManager(linearLayoutManager);

        foodTypeAdapter = new FoodTypeAdapter(foodTypes);
        foodTypesRecyclerView.setAdapter(foodTypeAdapter);

        foodTypesRecyclerView.setNestedScrollingEnabled(false);
    }

    private void loadDataFromFireBase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("foods_type");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Cập nhật giao diện RecyclerView khi có đối tượng được thêm vào RealTimeDatabase
                FoodType foodType = snapshot.getValue(FoodType.class);
                if (foodType != null) {
                    int index = -1;
                    for (int i = 0; i < foodTypes.size(); i++) {
                        if (foodTypes.get(i).getId() == foodType.getId()) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {
                        foodTypes.set(index, foodType);
                        foodTypeAdapter.notifyItemChanged(index);
                    } else {
                        foodTypes.add(foodType);
                        foodTypeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Cập nhật giao diện RecyclerView khi có đối tượng được cập nhật thông tin
                FoodType foodTypeUpdate = snapshot.getValue(FoodType.class);
                if (foodTypeUpdate != null) {
                    int index = -1;
                    for (int i = 0; i < foodTypes.size(); i++) {
                        if (foodTypes.get(i).getId() == foodTypeUpdate.getId()) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {
                        foodTypes.set(index, foodTypeUpdate);
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

    private void filterDataFoodTypes() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Tìm kiếm loại món ăn theo tên
                String searchText = charSequence.toString().trim().toLowerCase();
                List<FoodType> foodTypesFilter;

                if (searchText.isEmpty()) {
                    foodTypesFilter = foodTypes;
                } else {
                    foodTypesFilter = new ArrayList<>();
                    for (FoodType foodType : foodTypes) {
                        if (foodType.getName().toLowerCase().contains(searchText)) {
                            foodTypesFilter.add(foodType);
                        }
                    }
                }

                foodTypeAdapter.setFoodTypes(foodTypesFilter);
                foodTypeAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}