package com.example.android.vfund.controller.HomeController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vfund.R;
import com.example.android.vfund.controller.HomeController.Adapter.EventAdapter;
import com.example.android.vfund.controller.HomeController.Adapter.EventBriefAdapter;
import com.example.android.vfund.controller.HomeController.Adapter.HomeViewPagerAdapter;
import com.example.android.vfund.controller.HomeController.Adapter.NotificationAdapter;
import com.example.android.vfund.controller.QueryUtils;
import com.example.android.vfund.model.FundraisingEvent;
import com.example.android.vfund.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements EventCallBack, LoaderManager.LoaderCallbacks<ArrayList<FundraisingEvent>> {

    ViewPager2 viewPager;
    TextView txtAddEvent;
    private TextView txtCustomTab;
    private ImageView imgCustomTab;
    int[] customImagesTab = {R.drawable.home_icon, R.drawable.follow_icon,
            R.drawable.explore_icon, R.drawable.notify_icon, R.drawable.account_icon};
    String[] customTextTab = {"Trang chủ", "Theo dõi", "Khám phá", "Thông báo", "Tài khoản"};
    int[] customImagesTabSelected = {R.drawable.home_icon_selected, R.drawable.follow_icon_selected,
            R.drawable.explore_icon_selected, R.drawable.notify_icon_selected, R.drawable.account_icon_selected};
    public static final int REQUEST_DONATED_CODE = 1;
    private final int ID_FETCH_EVENT_LOADER = 1;

    private HomeViewPagerAdapter homeViewPagerAdapter;
    private int numOfTab = 5;

    private static final String EVENT_REQUEST_URL = "http://10.0.2.2:8080/api/events/getevent";
    private EventAdapter myEventAdapter;
    private EventAdapter myEventFollowedAdapter;
    private NotificationAdapter myNotifyAdapter;
    private EventBriefAdapter myEventBriefAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent callingIntent = getIntent();
        Bundle userBundle = callingIntent.getExtras();
        User loginUser = userBundle.getParcelable("user");

        txtAddEvent = (TextView)findViewById(R.id.txtAddEvent);
        viewPager = (ViewPager2)findViewById(R.id.viewpager);

        homeViewPagerAdapter = new HomeViewPagerAdapter(this);

        myEventAdapter = new EventAdapter(this);
        myEventFollowedAdapter = new EventAdapter(this);
        myNotifyAdapter = new NotificationAdapter();
        myEventBriefAdapter = new EventBriefAdapter();

        LoaderManager.getInstance(this).initLoader(ID_FETCH_EVENT_LOADER, null, this);

        homeViewPagerAdapter.setNumOfTab(numOfTab);

        homeViewPagerAdapter.setEventAdapter(myEventAdapter);
        homeViewPagerAdapter.setEventFollowedAdapter(myEventFollowedAdapter);
        homeViewPagerAdapter.setNotifyAdapter(myNotifyAdapter);
        homeViewPagerAdapter.setEventBriefAdapter(myEventBriefAdapter);
        homeViewPagerAdapter.setLoginUser(loginUser);

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(homeViewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);

        setUpTabLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == REQUEST_DONATED_CODE && resultCode == RESULT_OK) {
                Log.e("LOGTAG", "TESTING");
                Bundle bundle = data.getExtras();
                FundraisingEvent event = bundle.getParcelable("event");
                Log.e("LOGTAG", "" + event.getStringPercentage());
                updateEvent(event);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void followEvent(FundraisingEvent event) {
        myEventFollowedAdapter.addEvent(event);
    }

    @Override
    public void unfollowEvent(FundraisingEvent event) {
        myEventFollowedAdapter.removeEvent(event);
    }

    @Override
    public void updateEvent(FundraisingEvent event) {
        myEventAdapter.updateEvent(event);
        myEventFollowedAdapter.updateEvent(event);
    }

    private void setUpTabLayout() {
        final TabLayout tabLayout = (TabLayout)findViewById(R.id.homeTabLayout);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                View v = getLayoutInflater().inflate(R.layout.custom_tab, null);
                txtCustomTab = (TextView)v.findViewById(R.id.txtCustomTab);
                imgCustomTab = (ImageView)v.findViewById(R.id.imgCustomTab);
                txtCustomTab.setText(customTextTab[position]);
                imgCustomTab.setImageResource(customImagesTab[position]);
                tab.setCustomView(v);
            }
        }).attach();
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                View selectedView = tabLayout.getTabAt(position).getCustomView();
                txtCustomTab = (TextView)selectedView.findViewById(R.id.txtCustomTab);
                imgCustomTab = (ImageView)selectedView.findViewById(R.id.imgCustomTab);
                txtCustomTab.setText(customTextTab[position]);
                txtCustomTab.setTextColor(Color.parseColor("#D2FBDB"));
                imgCustomTab.setImageResource(customImagesTabSelected[position]);
                tabLayout.getTabAt(position).setCustomView(selectedView);

                for(int i = 0; i < tabLayout.getTabCount(); i++){
                    if(i == position){
                        continue;
                    }
                    else {
                        View v = tabLayout.getTabAt(i).getCustomView();
                        txtCustomTab = (TextView)v.findViewById(R.id.txtCustomTab);
                        imgCustomTab = (ImageView)v.findViewById(R.id.imgCustomTab);
                        txtCustomTab.setText(customTextTab[i]);
                        txtCustomTab.setTextColor(Color.parseColor("#99A5F8C0"));
                        imgCustomTab.setImageResource(customImagesTab[i]);
                        tabLayout.getTabAt(i).setCustomView(v);
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public Loader<ArrayList<FundraisingEvent>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EventAsyncTaskLoader(this, EVENT_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<FundraisingEvent>> loader, ArrayList<FundraisingEvent> data) {
        myEventAdapter.submitList(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<FundraisingEvent>> loader) {
        myEventAdapter.submitList(null);
    }

    private static class EventAsyncTaskLoader extends AsyncTaskLoader<ArrayList<FundraisingEvent>>{
        private String mURL = null;
        public EventAsyncTaskLoader(@NonNull Context context, String url) {
            super(context);
            mURL = url;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Nullable
        @Override
        public ArrayList<FundraisingEvent> loadInBackground() {
            if(mURL == null) {
                return null;
            }
            ArrayList<FundraisingEvent> eventList = QueryUtils.fetchEventData(mURL);
            return eventList;
        }
    }
}