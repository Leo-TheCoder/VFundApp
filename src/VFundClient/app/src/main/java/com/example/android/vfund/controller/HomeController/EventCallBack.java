package com.example.android.vfund.controller.HomeController;

import com.example.android.vfund.model.FundraisingEvent;

public interface EventCallBack {
    public void followEvent(FundraisingEvent event);
    public void unfollowEvent(FundraisingEvent event);
}
