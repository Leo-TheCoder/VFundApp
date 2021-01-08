package com.example.android.vfund.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class User implements Parcelable {
    private String _name;
    private String _password;
    private String _email;
    private String _phone;
    private int _id;
    private int _age;

    private static SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public User(){}
    public User(int id, String name, String email, String birth, String phone) {
        this._id = id;
        this._name = name;
        this._email = email;

        Date date = null;
        try {
            date = inputFormat.parse(birth);
            Date now = new Date();
            long diff = now.getTime() - date.getTime();
            _age = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            _age = 0;
        }

        this._phone = phone;
    }

    protected User(Parcel in) {
        _name = in.readString();
        _password = in.readString();
        _email = in.readString();
        _phone = in.readString();
        _id = in.readInt();
        _age = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_name);
        dest.writeString(_password);
        dest.writeString(_email);
        dest.writeString(_phone);
        dest.writeInt(_id);
        dest.writeInt(_age);
    }
}
