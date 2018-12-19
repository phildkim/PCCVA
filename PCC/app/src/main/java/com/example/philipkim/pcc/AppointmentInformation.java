package com.example.philipkim.pcc;

public class AppointmentInformation {
    private String deanID, deanName, appDate, startTime, endTime, allTimes;

    // Constructs the faculty information as an object
    public AppointmentInformation(String deadID, String deanName, String appDate, String startTime, String endTime, String allTimes) {
        this.deanID = deadID;
        this.deanName = deanName;
        this.appDate = appDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allTimes = allTimes;
    }

    public String getDeanID() { return deanID; }

    // Get dean name
    public String getDeanName() {
        return deanName;
    }

    // Get appointment date
    public String getAppDate() {
        return appDate;
    }

    // Get appointment starting time
    public String getStartTime() {
        return startTime;
    }

    // Get appointment ending time
    public String getEndTime() {
        return endTime;
    }

    // Get appointment ending time
    public String getAllTimes() {
        return allTimes;
    }
}