package com.example.android.vfund.controller.HomeController.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.android.vfund.controller.HomeController.AccountPageFragment;
import com.example.android.vfund.controller.HomeController.ExplorePageFragment;
import com.example.android.vfund.controller.HomeController.FollowPageFragment;
import com.example.android.vfund.controller.HomeController.HomePageFragment;
import com.example.android.vfund.controller.HomeController.NotificationPageFragment;

public class HomeViewPagerAdapter extends FragmentStateAdapter {
    private int mNumOfTab;
    private static EventAdapter mEventAdapter;
    private static NotificationAdapter mNotifyAdapter;
    private static EventBriefAdapter mEventBriefAdapter;

    public HomeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    public void setNumOfTab(int numOfTab) {
        this.mNumOfTab = numOfTab;
    }
    public void setEventAdapter(EventAdapter adapter) { mEventAdapter = adapter; }
    public void setNotifyAdapter(NotificationAdapter adapter) { mNotifyAdapter = adapter; }
    public void setEventBriefAdapter(EventBriefAdapter adapter) { mEventBriefAdapter = adapter; }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return new HomePageFragment(mEventAdapter);
        }
        else if(position == 1) {
            return new FollowPageFragment(mEventAdapter);
        }
        else if(position == 2) {
            return new ExplorePageFragment();
        }
        else if(position == 3) {
            return new NotificationPageFragment(mNotifyAdapter);
        }
        else if(position == 4) {
            return new AccountPageFragment(mEventBriefAdapter);
        }
        else {
            return null;
        }
    }
    @Override
    public int getItemCount() {
        return mNumOfTab;
    }
}
