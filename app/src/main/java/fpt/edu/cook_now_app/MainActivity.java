package fpt.edu.cook_now_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fpt.edu.cook_now_app.adpter.PageAdapter;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 mainViewPager;
    private BottomNavigationView bottomMenu;
    public static final int INDEX_PAGE_HOME = 0;
    public static final int INDEX_PAGE_SEARCH = 1;
    public static final int INDEX_PAGE_CONTACT = 2;

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
        bottomMenu.setOnItemSelectedListener(menuItem -> handleBottomMenuItemSelected(menuItem));
    }

    @SuppressLint("NonConstantResourceId")
    private boolean handleBottomMenuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                mainViewPager.setCurrentItem(INDEX_PAGE_HOME, false);
                break;
            case R.id.menu_search:
                mainViewPager.setCurrentItem(INDEX_PAGE_SEARCH, false);
                break;
            case R.id.menu_contact:
                mainViewPager.setCurrentItem(INDEX_PAGE_CONTACT, false);
                break;
        }
        return true;
    }
}