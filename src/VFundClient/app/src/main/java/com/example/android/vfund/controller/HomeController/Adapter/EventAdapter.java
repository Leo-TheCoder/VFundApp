package com.example.android.vfund.controller.HomeController.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ListAdapter<FundraisingEvent, EventAdapter.ViewHolder> {

    private boolean isFollowed = false;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MaterialButton btnFollow;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
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
        _eventList.add(new FundraisingEvent());
        _eventList.add(new FundraisingEvent());
        _eventList.add(new FundraisingEvent());
        _eventList.add(new FundraisingEvent());
        submitList(_eventList);
    }

    public void setFollowed(boolean value) {
        isFollowed = value;
    }

    public void addEvent(FundraisingEvent event) {
        _eventList.add(event);
        submitList(_eventList);
        notifyDataSetChanged(); //delete when having real data
    }

    public void removeEvent(FundraisingEvent event) {
        if(_eventList.remove(event)){
            submitList(_eventList);
            notifyDataSetChanged(); //delete when having real data
        };
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
        final FundraisingEvent currentEvent = getItem(position);

        final Button btnFollow = holder.btnFollow;
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Clicke", "test");
                boolean isFollow = currentEvent.is_Followed();
                currentEvent.set_isFollowed(!isFollow);
                if(!isFollow == true) {
                    ShapeDrawable shapedrawable = new ShapeDrawable();
                    shapedrawable.setShape(new RectShape());
                    shapedrawable.getPaint().setColor(Color.parseColor("#045D56"));
                    shapedrawable.getPaint().setStrokeWidth(0.2f);
                    shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
                    btnFollow.setBackground(shapedrawable);
                    btnFollow.setBackgroundColor(ContextCompat.getColor(_context, android.R.color.transparent));
                    btnFollow.setText("Đã theo dõi");
                    parentActivity.followEvent(currentEvent);
                }
                else {
                    btnFollow.setText("+ Theo dõi");
                    parentActivity.unfollowEvent(currentEvent);
                }
            }
        });
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(view.getId() == btnFollow.getId()) {


                }
                else {
                    Intent intent = new Intent(parentActivity, EventDetailActivity.class);
                    parentActivity.startActivity(intent);
                }
            }
        });

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
