package fpt.edu.cook_now_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fpt.edu.cook_now_app.adpter.PageAdapter;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 mainViewPager;
    private BottomNavigationView bottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();
        initBottomMenu();
    }

    private void initViewPager() {
        mainViewPager = findViewById(R.id.mainViewPager);

        PageAdapter pageAdapter = new PageAdapter(this);
        mainViewPager.setAdapter(pageAdapter);
        mainViewPager.setUserInputEnabled(false);
    }

    private void initBottomMenu() {
        bottomMenu = findViewById(R.id.bottomMenu);
        bottomMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    mainViewPager.setCurrentItem(0, false);
                    break;
                case R.id.menu_search:
                    mainViewPager.setCurrentItem(1, false);
                    break;
                case R.id.menu_contact:
                    mainViewPager.setCurrentItem(2, false);
                    break;
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
    }
}