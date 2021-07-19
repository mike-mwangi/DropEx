package com.example.dropex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class OnboardingActivity extends AppCompatActivity implements OnboardingFragment.OnBoardingListener {
    private static int currentPage = 0;
    private static ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager2 viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        viewPager=(ViewPager2) findViewById(R.id.viewpager);
        TabLayout tabLayout=findViewById(R.id.tab);



        OnBoardingViewPagerAdapter adapter = new OnBoardingViewPagerAdapter(this.getSupportFragmentManager(),this.getLifecycle());
        for (int i=1;i<=3;i++) {
            fragments.add(OnboardingFragment.newInstance(i, this));
        }
        adapter.addFragments(fragments);

       viewPager.setAdapter(adapter);

        // keep track of the current screen
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage=position;
            }
        });
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("")
        ).attach();
    }


    // open the main screen when next is tapped on the last screen
    @Override
    public void onNextClick() {

            startActivity(new Intent(this, MainActivity.class));
            SharedPref.getInstance(this.getApplicationContext()).setIsFirstLaunchToFalse();
            finish();

    }
}