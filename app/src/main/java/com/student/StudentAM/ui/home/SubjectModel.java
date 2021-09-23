package com.student.StudentAM.ui.home;

public class SubjectModel {
    private String Subject_Name ;
    private int  Initial_Present , Total_Classes;

    public SubjectModel() {

    }

    public SubjectModel(String subject_Name, int initial_Present, int total_Classes) {
        Subject_Name = subject_Name;
        Initial_Present = initial_Present;
        Total_Classes = total_Classes;
    }

    public String getSubject_Name() {
        return Subject_Name;
    }

    public void setSubject_Name(String subject_Name) {
        Subject_Name = subject_Name;
    }

    public int getInitial_Present() {
        return Initial_Present;
    }

    public void setInitial_Present(int initial_Present) {
        Initial_Present = initial_Present;
    }

    public int getTotal_Classes() {
        return Total_Classes;
    }

    public void setTotal_Classes(int total_Classes) {
        Total_Classes = total_Classes;
    }
}
