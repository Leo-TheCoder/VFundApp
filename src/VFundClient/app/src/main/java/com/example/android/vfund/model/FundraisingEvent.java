package com.example.android.vfund.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FundraisingEvent implements Parcelable {
    private int _eventID;
    private String _eventName;
    private String _eventDescription;
    private int _timeRemain;
    private float _eventGoal;
    private float _eventRate;
    private boolean _isFollowed;
    private float _currentGain;
    private User _owner = null;

    private static SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public FundraisingEvent(){}

    public FundraisingEvent(int id, String name, String description, String deadline, boolean isFollowed, float goal){
        _eventID = id;
        _eventName = name;
        _eventDescription = description;
        Date dateDeadline = null;
        try {
            dateDeadline = inputFormat.parse(deadline);
            Date now = new Date();
            long diff =  dateDeadline.getTime() - now.getTime();
            _timeRemain = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e){
            _timeRemain = -1;
        }
        _isFollowed =  isFollowed;
        _eventGoal = goal;
        _currentGain = 0;
    }

    protected FundraisingEvent(Parcel in) {
        _eventID = in.readInt();
        _eventName = in.readString();
        _eventDescription = in.readString();
        _timeRemain = in.readInt();
        _eventGoal = in.readFloat();
        _eventRate = in.readFloat();
        _isFollowed = in.readByte() != 0;
        _currentGain = in.readFloat();
        _owner = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<FundraisingEvent> CREATOR = new Creator<FundraisingEvent>() {
        @Override
        public FundraisingEvent createFromParcel(Parcel in) {
            return new FundraisingEvent(in);
        }

        @Override
        public FundraisingEvent[] newArray(int size) {
            return new FundraisingEvent[size];
        }
    };

    public int get_eventID() {
        return _eventID;
    }

    public int timeRemain() { return 1;}

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

    public void getDonate(float money) {
        _currentGain += money;
    }

    public float get_currentGain() {
        return _currentGain;
    }

    public int getState() {
        if(_currentGain >= _eventGoal) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public int get_timeRemain() {
        return _timeRemain;
    }

    public String get_eventDescription() {
        return _eventDescription;
    }

    public String getStringGoalFormat() {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(Math.round(_eventGoal)));
        int length = result.length();
        for(int i = length - 3; i > 0; i-=3) {
            result.insert(i, ',');
        }
        return result.toString();
    }

    public User get_owner() {
        return _owner;
    }

    public void set_owner(User _owner) {
        this._owner = _owner;
    }

    public String getStringPercentage() {
        float percentage = _currentGain/ _eventGoal * 100;
        String result = String.valueOf(Math.round(percentage)) +"%";
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(_eventID);
        dest.writeString(_eventName);
        dest.writeString(_eventDescription);
        dest.writeInt(_timeRemain);
        dest.writeFloat(_eventGoal);
        dest.writeFloat(_eventRate);
        dest.writeByte((byte) (_isFollowed ? 1 : 0));
        dest.writeFloat(_currentGain);
        dest.writeParcelable(_owner, flags);
    }
}
