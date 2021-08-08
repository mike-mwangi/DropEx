package com.example.dropex.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.dropex.R;
import com.example.dropex.SharedPref;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// TODO: uniform start button through out

// TODO: fix the error that occurs when you change system theme
public class OnboardingActivity extends AppCompatActivity {
    private static int currentPage = 0;
    private static ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager2 viewPager;
    private OnBoardingViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        viewPager=(ViewPager2) findViewById(R.id.viewpager);
        TabLayout tabLayout=findViewById(R.id.tab);
        final ExtendedFloatingActionButton startButton=findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onNextClick();

            }
        });


        adapter = new OnBoardingViewPagerAdapter(this.getSupportFragmentManager(),this.getLifecycle());
        for (int i=1;i<=3;i++) {
            fragments.add(OnboardingFragment.newInstance(i));
        }
        adapter.setFragments(fragments);

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


        autoScroll(150);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
   adapter.setFragments(null);
    }


    public  void autoScroll (int interval) {

        final Handler mHandler = new Handler();
        final Runnable mUpdateResults = new Runnable() {
            int page=0;
            public void run() {
                int numPages = 3;
                page = (page + 1) % numPages;
                viewPager.setCurrentItem(page);



            }
        };

        int delay = 1000; // delay for 1 sec.

        int period = 4000; // repeat every 4 sec.

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {


                mHandler.post(mUpdateResults);

            }

        }, delay, period);
    }

    // open the main screen when next is tapped on the last screen

    public void onNextClick() {

        startActivity(new Intent(this, SplashScreenActivity.class));
            SharedPref.getInstance(this.getApplicationContext()).setIsFirstLaunchToFalse();
            finish();

    }
}