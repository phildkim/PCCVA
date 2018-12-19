package com.example.philipkim.pcc;

public class StudentAppointmentInformation {
    private String studentID, studentName, studentAppDate, studentAppTime;

    public StudentAppointmentInformation(String studentID, String studentName, String studentAppDate, String studentAppTime) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentAppDate = studentAppDate;
        this.studentAppTime = studentAppTime;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentAppDate() {
        return studentAppDate;
    }

    public String getStudentAppTime() {
        return studentAppTime;
    }

}
