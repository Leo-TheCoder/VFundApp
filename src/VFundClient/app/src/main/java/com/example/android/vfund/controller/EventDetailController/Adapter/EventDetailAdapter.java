package com.example.android.vfund.controller.EventDetailController.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.android.vfund.controller.EventDetailController.DonateFragment;
import com.example.android.vfund.controller.EventDetailController.StoryFragment;
import com.example.android.vfund.controller.HomeController.AccountPageFragment;
import com.example.android.vfund.controller.HomeController.Adapter.EventAdapter;
import com.example.android.vfund.controller.HomeController.Adapter.EventBriefAdapter;
import com.example.android.vfund.controller.HomeController.Adapter.NotificationAdapter;
import com.example.android.vfund.controller.HomeController.ExplorePageFragment;
import com.example.android.vfund.controller.HomeController.FollowPageFragment;
import com.example.android.vfund.controller.HomeController.HomePageFragment;
import com.example.android.vfund.controller.HomeController.NotificationPageFragment;
import com.example.android.vfund.model.Donate;
import com.example.android.vfund.model.FundraisingEvent;

public class EventDetailAdapter extends FragmentStateAdapter {
    private int mNumOfTab;
    private DonateAdapter mDonateAdapter;
    private FundraisingEvent mEvent;

    public EventDetailAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    public void setNumOfTab(int numOfTab) {
        this.mNumOfTab = numOfTab;
    }
    public void setDonateAdapter(DonateAdapter adapter) {mDonateAdapter = adapter;}
    public void setEvent(FundraisingEvent event) {mEvent = event;}

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return new StoryFragment(mEvent);
        }
        else if(position == 1) {
            return new DonateFragment(mDonateAdapter);
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
