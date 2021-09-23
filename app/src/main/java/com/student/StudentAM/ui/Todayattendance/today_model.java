package com.student.StudentAM.ui.Todayattendance;

public class today_model {

    String Lecture_Time, Status, Subject, mark_time, Current_date, StudentId;
    int lecture_no;

    public today_model() {
    }

    public today_model(String lecture_Time, String status, String subject, String mark_time, String current_date, String studentId, int lecture_no) {
        Lecture_Time = lecture_Time;
        Status = status;
        Subject = subject;
        this.mark_time = mark_time;
        Current_date = current_date;
        StudentId = studentId;
        this.lecture_no = lecture_no;
    }

    public String getLecture_Time() {
        return Lecture_Time;
    }

    public void setLecture_Time(String lecture_Time) {
        Lecture_Time = lecture_Time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getMark_time() {
        return mark_time;
    }

    public void setMark_time(String mark_time) {
        this.mark_time = mark_time;
    }

    public int getLecture_no() {
        return lecture_no;
    }

    public void setLecture_no(int lecture_no) {
        this.lecture_no = lecture_no;
    }

    public String getCurrent_date() {
        return Current_date;
    }

    public void setCurrent_date(String current_date) {
        Current_date = current_date;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

}
