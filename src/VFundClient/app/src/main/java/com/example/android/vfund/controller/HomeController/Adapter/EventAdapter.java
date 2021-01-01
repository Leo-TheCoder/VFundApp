package com.example.android.vfund.controller.HomeController.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.vfund.R;
import com.example.android.vfund.model.FundraisingEvent;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ListAdapter<FundraisingEvent, EventAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    private ArrayList<FundraisingEvent> _eventList;

    public EventAdapter(){
        super(DIFF_CALLBACK);
        _eventList = new ArrayList<FundraisingEvent>();
        _eventList.add(new FundraisingEvent());
        _eventList.add(new FundraisingEvent());
        _eventList.add(new FundraisingEvent());
        submitList(_eventList);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    public static final DiffUtil.ItemCallback<FundraisingEvent> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<FundraisingEvent>() {
                @Override
                public boolean areItemsTheSame(FundraisingEvent oldItem, FundraisingEvent newItem) {
                    return oldItem.get_eventID().equals(newItem.get_eventID());
                }
                @Override
                public boolean areContentsTheSame(FundraisingEvent oldItem, FundraisingEvent newItem) {
                    return oldItem.get_eventName().equals(newItem.get_eventName()) &&
                            oldItem.get_eventGoal() == newItem.get_eventGoal() &&
                            oldItem.get_eventDate().equals(newItem.get_eventDate());
                }
            };
}
