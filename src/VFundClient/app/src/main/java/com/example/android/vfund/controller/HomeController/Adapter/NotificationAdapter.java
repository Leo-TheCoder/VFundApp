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
import com.example.android.vfund.model.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends ListAdapter<Notification, NotificationAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private ArrayList<Notification> _notifyList;

    public NotificationAdapter(){
        super(DIFF_CALLBACK);
        _notifyList = new ArrayList<Notification>();
        _notifyList.add(new Notification());
        _notifyList.add(new Notification());
        _notifyList.add(new Notification());
        submitList(_notifyList);
    }


    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_notify_home, parent, false);
        // Return a new holder instance
        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {


    }

    public static final DiffUtil.ItemCallback<Notification> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Notification>() {
                @Override
                public boolean areItemsTheSame(Notification oldItem, Notification newItem) {
                    return oldItem.get_id().equals(newItem.get_id());
                }
                @Override
                public boolean areContentsTheSame(Notification oldItem, Notification newItem) {
                    return oldItem.get_user().get_id() == (newItem.get_user().get_id()) &&
                            oldItem.get_donatedEvent().get_eventID() == newItem.get_donatedEvent().get_eventID();

                }
            };
}
