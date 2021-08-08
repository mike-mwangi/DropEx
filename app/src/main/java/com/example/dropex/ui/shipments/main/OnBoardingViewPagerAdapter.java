package com.example.dropex.ui.shipments.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class OnBoardingViewPagerAdapter extends FragmentStateAdapter {
    private ArrayList<ShippingItemFragment> fragments = new ArrayList<ShippingItemFragment>();


    public OnBoardingViewPagerAdapter(@NonNull  FragmentManager fragmentManager, @NonNull  Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }





    public void setFragments(ArrayList<ShippingItemFragment> fragments) {
        this.fragments=fragments;
    }
}
