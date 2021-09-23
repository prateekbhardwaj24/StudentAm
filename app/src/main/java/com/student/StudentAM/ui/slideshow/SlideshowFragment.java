package com.student.StudentAM.ui.slideshow;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.student.StudentAM.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    private Button open_seekBar_dialog;
    private TextView show_goal_attendance, show_individual;
    private Dialog goal_dialog;
    private FirebaseAuth mAuth ;
    private DatabaseReference mDatabase;
    private String CurrentUserId, I_c, T_c;
    private int total_criteria, individual_criteria;
    private SeekBar seekBar, seekBar2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        open_seekBar_dialog = root.findViewById(R.id.open_seekBar_dialog);
        show_goal_attendance = root.findViewById(R.id.show_goal_attendance);
        show_individual = root.findViewById(R.id.show_individual);

        goal_dialog = new Dialog(getContext());
        goal_dialog.setCancelable(false);
        goal_dialog.setContentView(R.layout.goal_layout);
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference(" Attendance_goal");

        seekBar = goal_dialog.findViewById(R.id.seekBar);
        TextView seekBar_per = goal_dialog.findViewById(R.id.seekBar_per);
        seekBar2 = goal_dialog.findViewById(R.id.seekBar2);
        TextView seekBar_per2 = goal_dialog.findViewById(R.id.seekBar_per2);
        Button cancel = goal_dialog.findViewById(R.id.cancel_dialog);
        Button set_dialog = goal_dialog.findViewById(R.id.set_goal);

        open_seekBar_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goal_dialog.show();
            }
        });

        loadData();

        seekBar_per.setText(total_criteria+ " %");
        seekBar.setProgress(total_criteria);
        seekBar2.setProgress(individual_criteria);
        seekBar_per2.setText(individual_criteria+ " %");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar_per.setText(progress+ " %");

                total_criteria = progress;
                T_c = String.valueOf(total_criteria);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar_per2.setText(progress+ " %");

                individual_criteria = progress;
                I_c = String.valueOf(individual_criteria);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goal_dialog.dismiss();
            }
        });

        set_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(T_c) && TextUtils.isEmpty(I_c)){
                    Toast.makeText(getContext(), "Please Set Goal", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(T_c) || TextUtils.isEmpty(I_c)){
                    if (TextUtils.isEmpty(T_c)){
                        Toast.makeText(getContext(), "Please Set Total Criteria", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Please Set Individual Criteria", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                    show_goal_attendance.setText(total_criteria+ " %");
                    show_individual.setText(individual_criteria+ " %");
                    saveData();
                    goal_dialog.dismiss();
                }
            }
        });

        return root;
    }

    private void saveData() {

        final Map messageTextBody = new HashMap();
        messageTextBody.put("Total_criteria" , T_c);
        messageTextBody.put("Individual_criteria" , I_c);


        DatabaseReference t_ref  = FirebaseDatabase.getInstance().getReference("Total_Criteria");
        t_ref.child(CurrentUserId).child("Total_criteria").setValue(messageTextBody);
    }

    private void loadData() {
        DatabaseReference t_ref  = FirebaseDatabase.getInstance().getReference("Total_Criteria").child(CurrentUserId);
        t_ref.child("Total_criteria").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String i_c = snapshot.child("Individual_criteria").getValue().toString();
                    String t_c = snapshot.child("Total_criteria").getValue().toString();

                    show_individual.setText(i_c+" %");
                    show_goal_attendance.setText(t_c+" %");
                    seekBar.setProgress(Integer.parseInt(t_c));
                    seekBar2.setProgress(Integer.parseInt(i_c));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}