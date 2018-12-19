package com.example.philipkim.pcc;

public class FacultyInformation {
    // Initialized variables
    private String name, title, phone, email;

    /*
     * Constructs the facultyInformation object with their information
     * */
    public FacultyInformation(String name, String title, String phone, String email) {
        this.name = name;
        this.title = title;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
