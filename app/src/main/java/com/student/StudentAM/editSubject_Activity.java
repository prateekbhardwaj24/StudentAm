package com.student.StudentAM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class editSubject_Activity extends AppCompatActivity {

    private String Subject_name, Present, Total, tt, date;
    private TextView selectDate;
    private RecyclerView edit_subject_recycler;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String CurrentUserId;
    private RecyclerView.LayoutManager manager;
    private FirebaseRecyclerAdapter<student_status_modal, subject_edit_holder> adapter;
    private TextView show_present, total_l, s_name, per;
    private ProgressBar sub_progress;
    private  Calendar myCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject_);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Subject_name = getIntent().getExtras().get("Subject").toString();


        edit_subject_recycler = findViewById(R.id.edit_subject_recycler);
        total_l = findViewById(R.id.total_l);
        s_name = findViewById(R.id.s_name);
        show_present = findViewById(R.id.show_present);
        sub_progress = findViewById(R.id.sub_progress);
        per = findViewById(R.id.per);
        selectDate = findViewById(R.id.selectDate);

        manager = new LinearLayoutManager(editSubject_Activity.this);
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        edit_subject_recycler.setLayoutManager(manager);
        mDatabase = FirebaseDatabase.getInstance().getReference("Student_Status").child(CurrentUserId).child(Subject_name);

        loadSubjectData();
        s_name.setText(Subject_name);

        showDate();
    }

    private void showDate() {

        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(editSubject_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "MM-dd-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date = sdf.format(myCalendar.getTime());

        selectDate.setText(sdf.format(myCalendar.getTime()));
        showStatus(date);
    }

    private void showStatus(String date1) {

        FirebaseRecyclerOptions<student_status_modal> options = new FirebaseRecyclerOptions.Builder<student_status_modal>().setQuery(mDatabase.child(date1), student_status_modal.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<student_status_modal, subject_edit_holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull subject_edit_holder holder, int position, @NonNull student_status_modal model) {

                String classDate = model.getClass_Date();
                String ClassTime = model.getL_Time();
                String Status = model.getStatus();
                String subject = model.getSubject();
                String markTime = model.getTime();
                int l_no = model.getLectureNo();

                String markingTime = holder.getFormateDate(getApplicationContext(), markTime);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student_Status").child(CurrentUserId);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(subject)){
                            holder.setdata(markingTime, Status, subject, getApplicationContext(), CurrentUserId, markTime,classDate, ClassTime, l_no);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public subject_edit_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_status_edit_layout, parent, false);

                return new subject_edit_holder(v);
            }
        };
        adapter.notifyDataSetChanged();
        adapter.startListening();
        edit_subject_recycler.setAdapter(adapter);
    }

    private void loadSubjectData() {

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Subjects").child(CurrentUserId).child(Subject_name);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int present = snapshot.child("Initial_Present").getValue().hashCode();
                int classes = snapshot.child("Total_Classes").getValue().hashCode();
                show_present.setText(present + "");
                total_l.setText(classes + "");

                float p = present;
                float t = classes;

                double percentage = (p / t) * 100;
                per.setText(new DecimalFormat("##.##").format(percentage) + " %");

                sub_progress.setProgress((int) percentage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}