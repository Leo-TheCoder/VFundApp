package com.example.android.vfund.model;

import java.util.Date;

public class FundraisingEvent {
    private String _eventID;
    private String _eventName;
    private String _eventDescription;
    private Date _eventDate;
    private float _eventGoal;
    private float _eventRate;
    private boolean _isFollowed;
    private float _currentGain;

    public String get_eventID() {
        return _eventID;
    }

    public Date get_eventDate() {
        return _eventDate;
    }

    public String get_eventName() {
        return _eventName;
    }

    public float get_eventGoal() {
        return _eventGoal;
    }

    public boolean is_Followed() {
        return _isFollowed;
    }

    public void set_isFollowed(boolean isFollowed) {
        _isFollowed = isFollowed;
    }

    public FundraisingEvent(){}
}
