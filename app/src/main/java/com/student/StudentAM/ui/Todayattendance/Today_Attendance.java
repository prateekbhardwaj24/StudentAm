package com.student.StudentAM.ui.Todayattendance;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.student.StudentAM.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Today_Attendance extends Fragment {

    private RecyclerView todayRecycler;
    private TextView no_status;
    private String Today_date;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String CurrentUserId;
    private RecyclerView.LayoutManager manager;
    private FirebaseRecyclerAdapter<today_model, Today_viewholder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_today__attendance, container, false);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        todayRecycler = root.findViewById(R.id.todayRecycler);
        no_status = root.findViewById(R.id.no_status);

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("MM-dd-yy");
        Date myDate = new Date();
        Today_date = timeStampFormat.format(myDate);

        manager = new LinearLayoutManager(getActivity());
        todayRecycler.setLayoutManager(manager);
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Today_Attendance");

        checkData();

        FirebaseRecyclerOptions<today_model> options = new FirebaseRecyclerOptions.Builder<today_model>().setQuery(mDatabase.child(CurrentUserId), today_model.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<today_model, Today_viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Today_viewholder holder, int position, @NonNull today_model model) {
                String lecture = model.getLecture_Time();
                String status = model.getStatus();
                String subject = model.getSubject();
                String markTime = model.getMark_time();
                int lectureNo = model.getLecture_no();
                String Current_date = model.getCurrent_date();
                String StudentId = model.getStudentId();
                String markingTime = holder.getFormateDate(getActivity(), markTime);

                if (StudentId.equals(CurrentUserId)){
                    if (Current_date.equals(Today_date)){

                        holder.setData(lecture, status, subject, markingTime, markTime, lectureNo, getContext());
                    }
                }

            }

            @NonNull
            @Override
            public Today_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_layout, parent, false);
                return new Today_viewholder(v);
            }
        };
        adapter.notifyDataSetChanged();
        adapter.startListening();
        todayRecycler.setAdapter(adapter);

        return root;
    }

    private void checkData() {

        mDatabase.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    no_status.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "not exist here", Toast.LENGTH_SHORT).show();
                }
                else {
//                    mDatabase.orderByChild("StudentId").equalTo(CurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot ds : snapshot.getChildren()){
//                                if (!ds.exists()){
//                                    no_status.setVisibility(View.VISIBLE);
////                                    Toast.makeText(getActivity(), "not exist", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}