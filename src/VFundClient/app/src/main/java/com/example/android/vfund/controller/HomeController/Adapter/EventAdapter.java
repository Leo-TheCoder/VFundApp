package com.example.android.vfund.controller.HomeController.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.vfund.ItemClickListener;
import com.example.android.vfund.R;
import com.example.android.vfund.controller.EventDetailController.Adapter.EventDetailAdapter;
import com.example.android.vfund.controller.EventDetailController.EventDetailActivity;
import com.example.android.vfund.controller.HomeController.HomeActivity;
import com.example.android.vfund.model.FundraisingEvent;
import com.example.android.vfund.model.User;
import com.google.android.material.button.MaterialButton;


import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ListAdapter<FundraisingEvent, EventAdapter.ViewHolder> {

    private boolean isFollowed = false;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MaterialButton btnFollow;
        private ItemClickListener itemClickListener;
        public TextView txtDescriptionEvent;
        public TextView txtProgressEvent;
        public TextView txtMoneyGoalEvent;
        public TextView txtDayLeft;
        public ProgressBar progressEvent;
        public TextView txtNameOwner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            txtDescriptionEvent = (TextView)itemView.findViewById(R.id.txtDescriptionEvent);
            txtProgressEvent = (TextView)itemView.findViewById(R.id.txtProgressEvent);
            txtMoneyGoalEvent = (TextView)itemView.findViewById(R.id.txtMoneyGoalEvent);
            txtDayLeft = (TextView)itemView.findViewById(R.id.txtDayLeft);
            progressEvent = (ProgressBar)itemView.findViewById(R.id.progressEvent);
            txtNameOwner = (TextView)itemView.findViewById(R.id.txtNameOwner);
            btnFollow = (MaterialButton)itemView.findViewById(R.id.btnFollowEvent);


            if(isFollowed) {
                btnFollow.setVisibility(View.GONE);
            }
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    private ArrayList<FundraisingEvent> _eventList;
    private Context _context;
    private HomeActivity parentActivity;

    public EventAdapter(HomeActivity activity){
        super(DIFF_CALLBACK);
        parentActivity = activity;
        _eventList = new ArrayList<FundraisingEvent>();
//        for(int i = 0; i < 4; i++) {
//            FundraisingEvent myTestEvent = new FundraisingEvent(i,"Tiêm thử vaccin x" + i , "Event description",
//                    "2021-06-18T00:00:00.000Z", false, 1234567, 0);
//            _eventList.add(myTestEvent);
//        }
        submitList(_eventList);
    }

    public void setFollowed(boolean value) {
        isFollowed = value;
    }

    public void addEvent(FundraisingEvent event) {
        _eventList.add(event);
        submitList(null);
        submitList(_eventList);
    }

    public void removeEvent(FundraisingEvent event) {
        for(int i = 0; i < _eventList.size(); i++) {
            if(_eventList.get(i).get_eventID() == event.get_eventID()) {
                _eventList.remove(i);
                submitList(_eventList);
                notifyDataSetChanged();
            }
        }
    }

    private int searchEvent(int eventID) {
        int index = -1;
        for(int i = 0; i < _eventList.size(); i++) {
            if(eventID == _eventList.get(i).get_eventID()) {
                index = i;
            }
        }
        return index;
    }

    public void updateEvent(FundraisingEvent event) {
        int index = searchEvent(event.get_eventID());
        if(index >= 0) {
            _eventList.set(index, event);
            submitList(null);
            submitList(_eventList);
        }
    }

    public void updateEvent(ArrayList<FundraisingEvent> eventList) {
        submitList(eventList);
        _eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_event_home, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final FundraisingEvent currentEvent = getItem(position);

        holder.progressEvent.setMax(Math.round(currentEvent.get_eventGoal()));
        holder.progressEvent.setProgress(Math.round(currentEvent.get_currentGain()));

        String description = currentEvent.get_eventDescription();
        if(description.length() > 50) {
            description = description.substring(0, 50);
            description += " ...";
        }
        holder.txtDescriptionEvent.setText(description);

        holder.txtMoneyGoalEvent.setText(currentEvent.getStringGoalFormat());
        holder.txtDayLeft.setText(String.valueOf(currentEvent.get_timeRemain()));
        User owner = currentEvent.get_owner();
        if(owner != null) {
            holder.txtNameOwner.setText(owner.get_name());
        }
        holder.txtProgressEvent.setText(currentEvent.getStringPercentage());

        final MaterialButton btnFollow = holder.btnFollow;
        setUpButtonFollow(currentEvent, btnFollow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFollow = currentEvent.is_Followed();
                currentEvent.set_isFollowed(!isFollow);
                setUpButtonFollow(currentEvent, btnFollow);
                if(currentEvent.is_Followed()) {
                    parentActivity.followEvent(currentEvent);
                }
                else {
                    parentActivity.unfollowEvent(currentEvent);
                }
            }
        });
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(parentActivity, EventDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("event", currentEvent);
                intent.putExtras(bundle);
                parentActivity.startActivityForResult(intent, parentActivity.REQUEST_DONATED_CODE);
            }
        });

    }

    private void setUpButtonFollow(FundraisingEvent currentEvent, MaterialButton btnFollow) {
        boolean isFollow = currentEvent.is_Followed();
        if(isFollow) {
            btnFollow.setBackgroundColor(Color.parseColor("#055659"));
            btnFollow.setText("Đã theo dõi");
            btnFollow.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#1EB980")));
            btnFollow.setStrokeWidth(1);
        }
        else {
            btnFollow.setBackgroundColor(Color.parseColor("#1EB980"));
            btnFollow.setStrokeWidth(0);
            btnFollow.setText("+ Theo dõi");
        }
    }

    public static final DiffUtil.ItemCallback<FundraisingEvent> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<FundraisingEvent>() {
                @Override
                public boolean areItemsTheSame(FundraisingEvent oldItem, FundraisingEvent newItem) {
                    return oldItem.get_eventID() == newItem.get_eventID();
                }
                @Override
                public boolean areContentsTheSame(FundraisingEvent oldItem, FundraisingEvent newItem) {
                    return oldItem.get_eventName().equals(newItem.get_eventName()) &&
                            oldItem.get_eventGoal() == newItem.get_eventGoal() &&
                            oldItem.get_currentGain() == newItem.get_currentGain() &&
                            oldItem.get_timeRemain() == newItem.get_timeRemain();
                }
            };
}
