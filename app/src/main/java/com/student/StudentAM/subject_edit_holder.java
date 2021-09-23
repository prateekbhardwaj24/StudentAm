package com.student.StudentAM;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class subject_edit_holder extends RecyclerView.ViewHolder {

    TextView present_status, status_date, lectureTime, lectureNO;

    public subject_edit_holder(@NonNull View itemView) {
        super(itemView);

        present_status = itemView.findViewById(R.id.present_status);
        status_date = itemView.findViewById(R.id.status_date);
        lectureNO = itemView.findViewById(R.id.lectureNO);
        lectureTime = itemView.findViewById(R.id.lectureTime);
    }


    public String getFormateDate(Context applicationContext, String status_time) {

        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(Long.parseLong(status_time));

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
        }
    }

    public void setdata(String markingTime, String status, String subject, Context applicationContext, String currentUserId, String markTime, String classDate, String classTime, int l_no) {

        status_date.setText(classDate);
        lectureNO.setText("Lecture Number : "+ l_no);
        lectureTime.setText("Lecture Time : "+classTime);

        if (status.equals("Present")){
            present_status.setText("Present");
            present_status.setTextColor(applicationContext.getResources().getColor(R.color.text_color));
        }
        else {
            present_status.setText("Absent");
            present_status.setTextColor(applicationContext.getResources().getColor(R.color.btn_color));
        }
    }
}
