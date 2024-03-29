package com.sbitbd.fixed_bd.ui.seller_form.ui.notifications.view_pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class viewPager extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> fragmentTitle = new ArrayList<>();

    public viewPager(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    public void addFragment(Fragment fragment, String title){
        fragments.add(fragment);
        fragmentTitle.add(title);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
