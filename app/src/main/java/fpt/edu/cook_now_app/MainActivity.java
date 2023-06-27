package fpt.edu.cook_now_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import fpt.edu.cook_now_app.adpter.PageAdapter;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 pageMain;
    private BottomNavigationView menuBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initViewPager();

        menuBottom.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    pageMain.setCurrentItem(0, false);
                    break;
                case R.id.menu_search:
                    pageMain.setCurrentItem(1, false);
                    break;
                case R.id.menu_contact:
                    pageMain.setCurrentItem(2, false);
                    break;
            }
            return true;
        });
    }

    private void initView() {
        menuBottom = findViewById(R.id.menuBottom);
    }

    private void initViewPager() {
        PageAdapter pageAdapter = new PageAdapter(this);
        pageMain = findViewById(R.id.pageMain);
        pageMain.setAdapter(pageAdapter);
    }
}