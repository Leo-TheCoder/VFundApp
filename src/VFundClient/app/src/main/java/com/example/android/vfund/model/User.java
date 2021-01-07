package com.example.android.vfund.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class User {
    private String _name;
    private String _password;
    private String _email;
    private String _phone;
    private int _id;
    private int _age;

    public User(){}
    public User(int id, String name, String email, String birth, String phone) {
        this._id = id;
        this._name = name;
        this._email = email;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        try {
            cal.setTime(SimpleDateFormat.getDateInstance().parse(birth));
            this._age = Calendar.getInstance().get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
            this._age = 0;
        }
        this._phone = phone;
    }

    public String get_email() {
        return _email;
    }

    public String get_phone() {
        return _phone;
    }

    public int get_age() {
        return _age;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

}
