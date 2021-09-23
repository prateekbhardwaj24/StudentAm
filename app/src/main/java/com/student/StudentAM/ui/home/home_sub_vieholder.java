package com.student.StudentAM.ui.home;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.student.StudentAM.R;

import java.text.DecimalFormat;

public class home_sub_vieholder extends RecyclerView.ViewHolder {

    private TextView SubjectName,present1, totalClasses, percentage_show, lecture_status;
    private ProgressBar progressbar;

    public home_sub_vieholder(@NonNull View itemView) {
        super(itemView);

        SubjectName = itemView.findViewById(R.id.SubjectName);
        present1 = itemView.findViewById(R.id.present);
        totalClasses = itemView.findViewById(R.id.totalClasses);
        percentage_show = itemView.findViewById(R.id.percentage_show);
        progressbar = itemView.findViewById(R.id.progressbar);
        lecture_status = itemView.findViewById(R.id.lecture_status);
    }

    public void setData(String sub_n, int present, int total) {
        SubjectName.setText(sub_n);
        present1.setText(present+"");
        totalClasses.setText(total+"");
    }

    public void showpercentage(String sub_n, int present, int total) {

        float p = present;
        float t = total;

        double percentage = (p/t)*100;
        if (new DecimalFormat("##.##").format(percentage).equals("NaN")){
            percentage_show.setText(0+" %");
        }
        else {
            percentage_show.setText(new DecimalFormat("##.##").format(percentage) +" %");
        }


        progressbar.setProgress((int) percentage);
    }

    public void setLecture_status(String sub_n, int present, int total, int individual_criteria) {

        float p = present;
        float t = total;

        double percentage = (p/t)*100;
        String To = String.valueOf(individual_criteria);


        if (individual_criteria > percentage){
            int l_need = (100*present - total*individual_criteria)/(individual_criteria - 100);
            lecture_status.setText("Status: " + l_need + " lectures needed to get on track. " );
        }
        else {
            int l_leave = (100*present - total*individual_criteria)/(individual_criteria);
            lecture_status.setText("Status: " + l_leave + " lectures can be leave" );
        }
    }
    public void setDefaultStatus() {
        lecture_status.setText("Status: Please Set Attendance Goal");
    }
}
