package com.sbitbd.fixed_bd.ui.seller_form.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.seller_form.ui.notifications.view_pager.viewPager;
import com.sbitbd.fixed_bd.ui.seller_form.ui.notifications.view_tab.commision_withdraw;
import com.sbitbd.fixed_bd.ui.seller_form.ui.notifications.view_tab.deposite_bal;
import com.sbitbd.fixed_bd.ui.seller_form.ui.notifications.view_tab.my_commision_balance;


public class NotificationsFragment extends Fragment {

    private MaterialCardView send;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private my_commision_balance my_commision_balance;
    private commision_withdraw commision_withdraw;
    private deposite_bal deposite_bal;

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        initView(root);
        return root;
    }

    private void initView(View root){
        tabLayout = root.findViewById(R.id.tab_id);
        viewpager = root.findViewById(R.id.view_pager);
        my_commision_balance = new my_commision_balance();
        commision_withdraw = new commision_withdraw();
        deposite_bal = new deposite_bal();

        tabLayout.setupWithViewPager(viewpager);
        viewPager viewPager = new viewPager(getChildFragmentManager(),0);
        viewPager.addFragment(my_commision_balance,"কমিশন ব্যালেন্স");
        viewPager.addFragment(commision_withdraw,"কমিশন উত্তোলন");
        viewPager.addFragment(deposite_bal,"ডিপোজিট ব্যালেন্স");
        viewpager.setAdapter(viewPager);
    }
}