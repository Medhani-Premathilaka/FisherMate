package com.example.fishermate;

public class Inputs {
    private String location;
    private String dateTime;
    private int noOfCrewMembers;
    private int maxDays;

    public Inputs(String location, String dateTime, int noOfCrewMembers, int maxDays) {
        this.location = location;
        this.dateTime = dateTime;
        this.noOfCrewMembers = noOfCrewMembers;
        this.maxDays = maxDays;
    }

    public String getLocation() {
        return location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getNoOfCrewMembers() {
        return noOfCrewMembers;
    }

    public int getMaxDays() {
        return maxDays;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setNoOfCrewMembers(int noOfCrewMembers) {
        this.noOfCrewMembers = noOfCrewMembers;
    }

    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }


}
