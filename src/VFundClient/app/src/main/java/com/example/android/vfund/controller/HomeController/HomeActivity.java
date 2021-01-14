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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vfund.controller.CreateEventController.CreateEventActivity;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity implements EventCallBack, LoaderManager.LoaderCallbacks<ArrayList<ArrayList<FundraisingEvent>>> {

    ViewPager2 viewPager;
    TextView txtAddEvent;
    ProgressBar progressHome;
    private TextView txtCustomTab;
    private ImageView imgCustomTab;
    int[] customImagesTab = {R.drawable.home_icon, R.drawable.follow_icon,
            R.drawable.explore_icon, R.drawable.notify_icon, R.drawable.account_icon};
    String[] customTextTab = {"Trang chủ", "Theo dõi", "Khám phá", "Thông báo", "Tài khoản"};
    int[] customImagesTabSelected = {R.drawable.home_icon_selected, R.drawable.follow_icon_selected,
            R.drawable.explore_icon_selected, R.drawable.notify_icon_selected, R.drawable.account_icon_selected};
    public static final int REQUEST_DONATED_CODE = 1;
    public static final int REQUEST_FOLLOW_CODE = 2;

    private final int ID_FETCH_EVENT_LOADER = 1;

    private HomeViewPagerAdapter homeViewPagerAdapter;
    private int numOfTab = 5;

    private static final String EVENT_REQUEST_URL = "http://10.0.2.2:8080/api/events/getevent";
    private static final String EVENT_GET_FOLLOW_REQUEST_URL = "http://10.0.2.2:8080/api/users/getfollowevents/";
    private static final String EVENT_FOLLOW_REQUEST_URL = "http://10.0.2.2:8080/api/users/follow";
    private EventAdapter myEventAdapter;
    private EventAdapter myEventFollowedAdapter;
    private NotificationAdapter myNotifyAdapter;
    private EventBriefAdapter myEventBriefAdapter;
    private User mLoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent callingIntent = getIntent();
        Bundle userBundle = callingIntent.getExtras();
        mLoginUser = userBundle.getParcelable("user");

        txtAddEvent = (TextView)findViewById(R.id.txtAddEvent);
        viewPager = (ViewPager2)findViewById(R.id.viewpager);
        progressHome = (ProgressBar)findViewById(R.id.progressHome);
        txtAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getToAddEvent = new Intent(getApplicationContext(), CreateEventActivity.class);
                startActivity(getToAddEvent);
            }
        });

        homeViewPagerAdapter = new HomeViewPagerAdapter(this);

        myEventAdapter = new EventAdapter(this);
        myEventFollowedAdapter = new EventAdapter(this);
        myNotifyAdapter = new NotificationAdapter();
        myEventBriefAdapter = new EventBriefAdapter();


        StringBuilder followRequest = new StringBuilder();
        followRequest.append(EVENT_GET_FOLLOW_REQUEST_URL).append("US").append(String.valueOf(mLoginUser.get_id()));
        Log.e("TEST", followRequest.toString());
        Bundle followRequestBundle = new Bundle();
        followRequestBundle.putString("eventList", followRequest.toString());
        LoaderManager.getInstance(this).initLoader(ID_FETCH_EVENT_LOADER, followRequestBundle, this);

        homeViewPagerAdapter.setNumOfTab(numOfTab);

        homeViewPagerAdapter.setEventAdapter(myEventAdapter);
        homeViewPagerAdapter.setEventFollowedAdapter(myEventFollowedAdapter);
        homeViewPagerAdapter.setNotifyAdapter(myNotifyAdapter);
        homeViewPagerAdapter.setEventBriefAdapter(myEventBriefAdapter);
        homeViewPagerAdapter.setLoginUser(mLoginUser);
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
                Bundle bundle = data.getExtras();
                FundraisingEvent event = bundle.getParcelable("event");
                boolean isFollow = bundle.getBoolean("follow");
                updateEvent(event);
                if(isFollow) {
                    followEvent(event);
                }
                else {
                    unfollowEvent(event);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void followEvent(final FundraisingEvent event) {
        myEventFollowedAdapter.addEvent(event);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                StringBuilder requestFollow = new StringBuilder(EVENT_FOLLOW_REQUEST_URL);
                requestFollow.append("?UserID=US").append(mLoginUser.get_id()).append("&EventID=E").append(event.get_eventID());
                URL request = QueryUtils.createUrl(requestFollow.toString());
                try {
                    QueryUtils.makeHttpRequestFollowEvent(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void unfollowEvent(final FundraisingEvent event) {
        myEventFollowedAdapter.removeEvent(event);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                StringBuilder requestFollow = new StringBuilder(EVENT_FOLLOW_REQUEST_URL);
                requestFollow.append("?UserID=US").append(mLoginUser.get_id()).append("&EventID=E").append(event.get_eventID());
                URL request = QueryUtils.createUrl(requestFollow.toString());
                try {
                    QueryUtils.makeHttpRequestUnFollowEvent(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
    public Loader<ArrayList<ArrayList<FundraisingEvent>>> onCreateLoader(int id, @Nullable Bundle args) {
        String requestFollow = args.getString("eventList");
        progressHome.setVisibility(View.VISIBLE);
        return new EventAsyncTaskLoader(this, EVENT_REQUEST_URL, requestFollow);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<ArrayList<FundraisingEvent>>> loader, ArrayList<ArrayList<FundraisingEvent>> data) {
        progressHome.setVisibility(View.GONE);
        myEventAdapter.updateEvent(data.get(0));
        myEventFollowedAdapter.updateEvent(data.get(1));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<ArrayList<FundraisingEvent>>> loader) {
        myEventAdapter.submitList(null);
        myEventFollowedAdapter.submitList(null);
    }

    private static class EventAsyncTaskLoader extends AsyncTaskLoader<ArrayList<ArrayList<FundraisingEvent>>>{
        private String mUrlAll = null;
        private String mUrlFollow = null;
        public EventAsyncTaskLoader(@NonNull Context context, String urlAll, String urlFollow) {
            super(context);
            mUrlAll = urlAll;
            mUrlFollow = urlFollow;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Nullable
        @Override
        public ArrayList<ArrayList<FundraisingEvent>> loadInBackground() {
            if(mUrlAll == null || mUrlFollow == null) {
                return null;
            }

            ArrayList<ArrayList<FundraisingEvent>> eventLists = new ArrayList<ArrayList<FundraisingEvent>>();
            ArrayList<FundraisingEvent> eventList = QueryUtils.fetchEventData(mUrlAll, false);
            ArrayList<FundraisingEvent> eventFollowList = QueryUtils.fetchEventData(mUrlFollow, true);

            for(int i = 0; i < eventFollowList.size(); i++) {
                FundraisingEvent event = eventFollowList.get(i);
                for(int j = 0; j < eventList.size(); j++) {
                    FundraisingEvent tmpEvent = eventList.get(j);
                    if(tmpEvent.get_eventID() == event.get_eventID()) {
                        eventList.get(j).set_isFollowed(true);
                        break;
                    }
                }
            }

            eventLists.add(eventList);
            eventLists.add(eventFollowList);

            return eventLists;
        }
    }
}