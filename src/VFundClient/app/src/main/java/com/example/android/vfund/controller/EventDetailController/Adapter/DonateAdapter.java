package com.example.android.vfund.controller.EventDetailController.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.vfund.R;
import com.example.android.vfund.model.Donate;

import java.util.ArrayList;

public class DonateAdapter extends ListAdapter<Donate, DonateAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private ArrayList<Donate> _donateList;

    public DonateAdapter(){
        super(DIFF_CALLBACK);
        _donateList = new ArrayList<Donate>();
        _donateList.add(new Donate());
        _donateList.add(new Donate());
        _donateList.add(new Donate());
        _donateList.add(new Donate());
        submitList(_donateList);
    }

    @NonNull
    @Override
    public DonateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_donate_detail, parent, false);
        // Return a new holder instance
        DonateAdapter.ViewHolder viewHolder = new DonateAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DonateAdapter.ViewHolder holder, int position) {


    }

    public static final DiffUtil.ItemCallback<Donate> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Donate>() {
                @Override
                public boolean areItemsTheSame(Donate oldItem, Donate newItem) {
                    return oldItem.get_id().equals(newItem.get_id());
                }
                @Override
                public boolean areContentsTheSame(Donate oldItem, Donate newItem) {
                    return oldItem.get_donor().get_id() == (newItem.get_donor().get_id()) &&
                            oldItem.get_event().get_eventID() == newItem.get_event().get_eventID() &&
                            oldItem.get_money() == newItem.get_money();
                }
            };

}
