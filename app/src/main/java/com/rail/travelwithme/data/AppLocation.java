package com.rail.travelwithme.data;

public class AppLocation {

    private String mTimeStamp;
    private String mLatitute;
    private String mLongitute;


    public AppLocation() {
    }

    public AppLocation(String timestamp, String latitute, String longitute) {
        this.mTimeStamp = timestamp;
        this.mLatitute = latitute;
        this.mLongitute = longitute;
    }


    public String getTimestamp() {
        return mTimeStamp;
    }

    public void setTimestamp(String timestamp) {
        mTimeStamp = timestamp;
    }


    public String getLatitute() {
        return mLatitute;
    }

    public void setLatitut(String latitute) {
        mLatitute = latitute;
    }


    public String getLongitute() {
        return mLongitute;
    }

    public void setLongitute(String longitute) {
        mLongitute = longitute;
    }
}