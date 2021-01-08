package com.example.android.vfund.controller.EventDetailController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vfund.R;
import com.example.android.vfund.controller.DonateController.DonateActivity;
import com.example.android.vfund.controller.EventDetailController.Adapter.DonateAdapter;
import com.example.android.vfund.controller.EventDetailController.Adapter.EventDetailAdapter;
import com.example.android.vfund.model.FundraisingEvent;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener{

    ViewPager2 viewPager;
    TextView txtCustomTab;
    Button btnDonate;
    ImageButton btnBack;
    TextView txtEventName;
    TextView txtProgress;
    TextView txtGoalMoney;
    TextView txtDayLeft;
    ProgressBar progressBar;

    CharSequence[] customTextTab = {"Nội dung", "Đóng góp"};
    public static final int REQUEST_DONATE_CODE = 1;

    private EventDetailAdapter eventDetailPagerAdapter;
    private int numOfTabs = 2;

    private FundraisingEvent mEvent;
    private DonateAdapter myDonateAdapter;
    private Intent getToDonateActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent callingIntent = getIntent();
        Bundle eventBundle = callingIntent.getExtras();
        mEvent = eventBundle.getParcelable("event");

        viewPager = (ViewPager2)findViewById(R.id.viewpager_detail);
        btnDonate = (Button)findViewById(R.id.btnDonate_Detail);
        btnDonate.setOnClickListener(this);
        btnBack = (ImageButton)findViewById(R.id.btnBackArrow_Detail);
        btnBack.setOnClickListener(this);
        txtEventName = (TextView)findViewById(R.id.txtEventName_Detail);
        txtEventName.setText(mEvent.get_eventName());
        txtProgress = (TextView)findViewById(R.id.txtProgressEvent_Detail);
        txtProgress.setText(mEvent.getStringPercentage());
        txtGoalMoney = (TextView)findViewById(R.id.txtMoneyGoalEvent_Detail);
        txtGoalMoney.setText(mEvent.getStringGoalFormat());
        txtDayLeft = (TextView)findViewById(R.id.txtDayLeft_Detail);
        txtDayLeft.setText(String.valueOf(mEvent.get_timeRemain()));
        progressBar = (ProgressBar)findViewById(R.id.progressEvent_Detail);
        progressBar.setMax(Math.round(mEvent.get_eventGoal()));
        progressBar.setProgress(Math.round(mEvent.get_currentGain()));


        eventDetailPagerAdapter = new EventDetailAdapter(this);

        eventDetailPagerAdapter.setNumOfTab(numOfTabs);
        //mEvent = new FundraisingEvent();
        myDonateAdapter = new DonateAdapter();

        eventDetailPagerAdapter.setEvent(mEvent);
        eventDetailPagerAdapter.setDonateAdapter(myDonateAdapter);

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(eventDetailPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

       setUpTabLayout();


    }

    private void setUpTabLayout() {
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

    @Override
    public void onClick(View v) {
        if(v.getId() == btnDonate.getId()){
            getToDonateActivity = new Intent(this, DonateActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("event", mEvent);
            getToDonateActivity.putExtras(bundle);
            startActivityForResult(getToDonateActivity, REQUEST_DONATE_CODE);
        }
        else if(v.getId() == btnBack.getId()){
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == REQUEST_DONATE_CODE && resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                mEvent = bundle.getParcelable("event");
                txtProgress.setText(mEvent.getStringPercentage());
                progressBar.setProgress(Math.round(mEvent.get_currentGain()));
                Intent intent = getIntent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}