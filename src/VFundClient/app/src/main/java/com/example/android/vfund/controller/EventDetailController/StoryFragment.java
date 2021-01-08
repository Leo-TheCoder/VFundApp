package com.example.android.vfund.controller.EventDetailController;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.vfund.R;
import com.example.android.vfund.controller.HomeController.Adapter.EventAdapter;
import com.example.android.vfund.model.FundraisingEvent;

public class StoryFragment extends Fragment {

    private FundraisingEvent mEvent;
    private TextView txtStoryDescription;
//    private final String KEY_STATE_HOME_PAGE_RECYCLERVIEW = "homePageRecyclerView";
//    private Parcelable mState;

    public StoryFragment() {
        // Required empty public constructor
    }

    public StoryFragment(FundraisingEvent event) {
        mEvent = event;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       txtStoryDescription = (TextView)view.findViewById(R.id.txtStoryEvent_Detail);
       txtStoryDescription.setText(mEvent.get_eventDescription());

    }

}
