package com.example.android.vfund.model;

public class Notification {
    private String _id;
    private User _user;
    private float _money;
    private FundraisingEvent _donatedEvent;

    public Notification(){}

    public String get_id(){
        return _id;
    }
    public FundraisingEvent get_donatedEvent() {
        return _donatedEvent;
    }

    public User get_user(){
        return _user;
    }
}
