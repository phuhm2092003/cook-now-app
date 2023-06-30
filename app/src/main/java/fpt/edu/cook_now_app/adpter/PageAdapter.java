package fpt.edu.cook_now_app.adpter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import fpt.edu.cook_now_app.view.ContactFragment;
import fpt.edu.cook_now_app.view.HomeFragment;
import fpt.edu.cook_now_app.view.SearchFragment;

public class PageAdapter extends FragmentStateAdapter {
    public static final int NUMBER_OF_PAGES = 3;

    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new SearchFragment();
            case 2:
                return new ContactFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUMBER_OF_PAGES;
    }
}
