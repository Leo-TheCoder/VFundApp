package com.example.android.vfund.controller.HomeController;

import com.example.android.vfund.model.FundraisingEvent;

public interface EventCallBack {
    public void followEvent(FundraisingEvent event);
    public void unfollowEvent(FundraisingEvent event);
    public void updateEvent(FundraisingEvent event);
    public void createEvent(FundraisingEvent event);
}
