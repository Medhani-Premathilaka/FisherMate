package com.example.fishermate;

public class Inputs {

    public int getNoOfCrewMembers() {
        return noOfCrewMembers;
    }

    public void setNoOfCrewMembers(int noOfCrewMembers) {
        this.noOfCrewMembers = noOfCrewMembers;
    }

    public int getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }

    private String location;
    private String dateTime;
    private int noOfCrewMembers;
    private int maxDays;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
