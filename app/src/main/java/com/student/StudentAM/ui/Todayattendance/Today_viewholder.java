package com.student.StudentAM.ui.Todayattendance;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.student.StudentAM.R;

import java.util.Calendar;

public class Today_viewholder extends RecyclerView.ViewHolder {

    private TextView present_status, status_date, lectureTime, lectureNO;

    public Today_viewholder(@NonNull View itemView) {
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

    public void setData(String lecture, String status, String subject, String markingTime, String markTime, int lectureNumber, Context context) {
        status_date.setText(subject);
        lectureTime.setText(lecture);
        lectureNO.setText("Lecture Number "+ lectureNumber);


        if (status.equals("Present")){
            present_status.setText("Present");
            present_status.setTextColor(context.getResources().getColor(R.color.text_color));
        }
        else {
            present_status.setText("Absent");
            present_status.setTextColor(context.getResources().getColor(R.color.btn_color));
        }

    }
}
