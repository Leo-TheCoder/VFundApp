package com.example.android.vfund.controller.HomeController.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.vfund.R;
import com.example.android.vfund.model.FundraisingEvent;

import java.util.ArrayList;

public class EventBriefAdapter extends ListAdapter<FundraisingEvent, EventBriefAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    private ArrayList<FundraisingEvent> _eventList;

    public EventBriefAdapter(){
        super(DIFF_CALLBACK);
        _eventList = new ArrayList<FundraisingEvent>();
        _eventList.add(new FundraisingEvent());
        _eventList.add(new FundraisingEvent());
        _eventList.add(new FundraisingEvent());
        submitList(_eventList);
    }


    @NonNull
    @Override
    public EventBriefAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_event_account, parent, false);
        // Return a new holder instance
        EventBriefAdapter.ViewHolder viewHolder = new EventBriefAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventBriefAdapter.ViewHolder holder, int position) {


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
                            oldItem.get_timeRemain() == newItem.get_timeRemain();
                }
            };
}
