package com.student.StudentAM;

public class student_status_modal {

    String Class_Date, subject, Time, l_Time, status;
    int lectureNo;

    public student_status_modal() {
    }

    public student_status_modal(String class_Date, String subject, String time, String l_Time, String status, int lectureNo) {
        Class_Date = class_Date;
        this.subject = subject;
        Time = time;
        this.l_Time = l_Time;
        this.status = status;
        this.lectureNo = lectureNo;
    }

    public String getClass_Date() {
        return Class_Date;
    }

    public void setClass_Date(String class_Date) {
        Class_Date = class_Date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getL_Time() {
        return l_Time;
    }

    public void setL_Time(String l_Time) {
        this.l_Time = l_Time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLectureNo() {
        return lectureNo;
    }

    public void setLectureNo(int lectureNo) {
        this.lectureNo = lectureNo;
    }
}
