package com.example.android.vfund.controller.EventDetailController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.android.vfund.R;
import com.example.android.vfund.controller.EventDetailController.Adapter.DonateAdapter;
import com.example.android.vfund.controller.EventDetailController.Adapter.EventDetailAdapter;
import com.example.android.vfund.model.FundraisingEvent;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class EventDetailActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TextView txtCustomTab;

    CharSequence[] customTextTab = {"Nội dung", "Đóng góp"};

    private EventDetailAdapter eventDetailPagerAdapter;
    private int numOfTabs = 2;

    private FundraisingEvent myEvent;
    private DonateAdapter myDonateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        viewPager = (ViewPager2)findViewById(R.id.viewpager_detail);

        eventDetailPagerAdapter = new EventDetailAdapter(this);

        eventDetailPagerAdapter.setNumOfTab(numOfTabs);
        myEvent = new FundraisingEvent();
        myDonateAdapter = new DonateAdapter();

        eventDetailPagerAdapter.setEvent(myEvent);
        eventDetailPagerAdapter.setDonateAdapter(myDonateAdapter);

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(eventDetailPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        final TabLayout tabLayout = (TabLayout)findViewById(R.id.detailTabLayout);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                View v = getLayoutInflater().inflate(R.layout.custom_tab_detail, null);
                txtCustomTab = (TextView)v.findViewById(R.id.txtCustomTabDetail);
                txtCustomTab.setText(customTextTab[position]);
                txtCustomTab.setTextColor(Color.parseColor("#99A5F8C0"));
                tab.setCustomView(v);
            }
        }).attach();
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                View selectedView = tabLayout.getTabAt(position).getCustomView();
                txtCustomTab = (TextView)selectedView.findViewById(R.id.txtCustomTabDetail);
                txtCustomTab.setText(customTextTab[position]);
                txtCustomTab.setTextColor(Color.parseColor("#D2FBDB"));
                tabLayout.getTabAt(position).setCustomView(selectedView);

                for(int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (i == position) {
                        continue;
                    } else {
                        View v = tabLayout.getTabAt(i).getCustomView();
                        txtCustomTab = (TextView) v.findViewById(R.id.txtCustomTabDetail);
                        txtCustomTab.setText(customTextTab[i]);
                        txtCustomTab.setTextColor(Color.parseColor("#99A5F8C0"));
                        tabLayout.getTabAt(i).setCustomView(v);
                    }
                }
            }
        });
    }
}