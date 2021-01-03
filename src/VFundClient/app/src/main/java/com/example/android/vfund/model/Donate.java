package com.example.android.vfund.model;

import java.util.ArrayList;

public class Donate {
    private String _id;
    private FundraisingEvent _event;
    private User _donor;
    private float _money;
    private String _message;

    public Donate(){}

    public String get_id() {
        return _id;
    }

    public User get_donor() {
        return _donor;
    }

    public FundraisingEvent get_event(){
        return _event;
    }

    public float get_money() {
        return _money;
    }

    public String get_message() {
        return _message;
    }
}
