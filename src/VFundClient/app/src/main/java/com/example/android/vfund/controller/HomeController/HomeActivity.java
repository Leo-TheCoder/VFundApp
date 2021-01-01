package com.example.android.vfund.controller.HomeController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TextView;

import com.example.android.vfund.R;
import com.example.android.vfund.controller.HomeController.Adapter.EventAdapter;
import com.example.android.vfund.controller.HomeController.Adapter.HomeViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TextView txtAddEvent;

    private HomeViewPagerAdapter homeViewPagerAdapter;
    private int numOfTab = 5;

    private EventAdapter myEventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtAddEvent = (TextView)findViewById(R.id.txtAddEvent);
        viewPager = (ViewPager2)findViewById(R.id.viewpager);

        homeViewPagerAdapter = new HomeViewPagerAdapter(this);

        myEventAdapter = new EventAdapter();
        homeViewPagerAdapter.setNumOfTab(numOfTab);
        homeViewPagerAdapter.setEventAdapter(myEventAdapter);

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(homeViewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);

        final TabLayout tabLayout = (TabLayout)findViewById(R.id.homeTabLayout);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Trang chủ");
                        tab.setIcon(R.drawable.ic_outline_home_24);
                        break;
                    case 1:
                        tab.setText("Theo dõi");

                        if(tab.isSelected()){
                            tab.setIcon(R.drawable.follow_icon_selected);
                        }
                        else {
                            tab.setIcon(R.drawable.follow_icon);
                        }
                        break;
                    case 2:
                        tab.setText("Khám phá");
                        if(tab.isSelected()){
                            tab.setIcon(R.drawable.explore_icon_selected);
                        }
                        else {

                        }
                        break;
                    case 3:
                        tab.setText("Thông báo");
                        tab.setIcon(R.drawable.notify_icon);
                        if(tab.isSelected()){
                            tab.setIcon(R.drawable.notify_icon_selected);
                        }
                        else {
                            tab.setIcon(R.drawable.explore_icon);
                        }
                        break;
                    case 4:
                        tab.setText("Tài khoản");
                        if(tab.isSelected()){
                            tab.setIcon(R.drawable.account_icon_selected);
                        }
                        else {
                            tab.setIcon(R.drawable.account_icon);
                        }
                        break;
                    default:
                        break;
                }
            }
        }).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).setIcon(R.drawable.home_icon_selected);
            }
        });
    }

}