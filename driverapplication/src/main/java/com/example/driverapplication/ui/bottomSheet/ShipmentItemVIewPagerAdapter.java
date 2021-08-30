package com.example.driverapplication.ui.bottomSheet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ShipmentItemVIewPagerAdapter extends FragmentStateAdapter{
        private ArrayList<ShipmentItemFragment> fragments = new ArrayList<ShipmentItemFragment>();


        public ShipmentItemVIewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
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





        public void setFragments(ArrayList<ShipmentItemFragment> fragments) {
            this.fragments=fragments;
        }
}
