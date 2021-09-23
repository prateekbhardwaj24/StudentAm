package com.student.StudentAM.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.student.StudentAM.R;
import com.student.StudentAM.editSubject_Activity;
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
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private FirebaseAuth mAuth ;
    private DatabaseReference mDatabase;
    private String CurrentUserId, yesterday_date, OneMonthAgo;
    private RecyclerView home_sub_recycler;
    private RecyclerView.LayoutManager manager ;
    private int sum =0;
    private int sum1=0;
    private  int SP = 0;
    private ProgressBar home_progress;
    private TextView no_subject;

    FirebaseRecyclerAdapter<SubjectModel, home_sub_vieholder> adapter;

    private TextView overall_percentage , overall_lecture , overall_present , overall_absent , overall_status,show_date, mon, tue, wed, thur, fri, sat, sun ;
    private ProgressBar progress_bar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        overall_percentage = root.findViewById(R.id.overall_percentage);
        overall_present = root.findViewById(R.id.overall_present);
        overall_absent = root.findViewById(R.id.overall_absent);
        overall_lecture = root.findViewById(R.id.overall_lecture);
        overall_status = root.findViewById(R.id.overall_status);
        progress_bar = root.findViewById(R.id.progress_bar);

        home_progress = root.findViewById(R.id.home_progress);
        no_subject = root.findViewById(R.id.no_subject);

        show_date = root.findViewById(R.id.show_date);
        mon = root.findViewById(R.id.mon);
        tue = root.findViewById(R.id.tue);
        wed = root.findViewById(R.id.wed);
        thur = root.findViewById(R.id.thur);
        fri = root.findViewById(R.id.fri);
        sat = root.findViewById(R.id.sat);
        sun = root.findViewById(R.id.sun);

        home_sub_recycler = root.findViewById(R.id.home_sub_recy);
        manager = new LinearLayoutManager(getContext());
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        home_sub_recycler.setLayoutManager(manager);
        mDatabase = FirebaseDatabase.getInstance().getReference("Subjects");

        CountTotalLecture();
        showDate();
        showWeekName();


        FirebaseRecyclerOptions<SubjectModel> options = new FirebaseRecyclerOptions.Builder<SubjectModel>().setQuery(mDatabase.child(CurrentUserId) , SubjectModel.class)
                .build();
         adapter = new FirebaseRecyclerAdapter<SubjectModel, home_sub_vieholder>(options) {
             @Override
             protected void onBindViewHolder(@NonNull home_sub_vieholder holder, int position, @NonNull SubjectModel model) {
                 String sub_n = model.getSubject_Name();
                 int present = model.getInitial_Present();
                 int total = model.getTotal_Classes();

                 holder.setData(sub_n , present , total);
                 holder.showpercentage(sub_n , present , total);

                 holder.itemView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent = new Intent(getContext(), editSubject_Activity.class);
                         intent.putExtra("Subject", sub_n);
                         startActivity(intent);
                     }
                 });


                 DatabaseReference t_ref  = FirebaseDatabase.getInstance().getReference("Total_Criteria").child(CurrentUserId);
                 t_ref.child("Total_criteria").addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {

                         if (snapshot.exists()){
                             String i_c = snapshot.child("Individual_criteria").getValue().toString();
                             String t_c = snapshot.child("Total_criteria").getValue().toString();

                             holder.setLecture_status(sub_n, present, total, Integer.parseInt(i_c));
                         }
                         else {
                             holder.setDefaultStatus();
                         }

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });
             }

             @NonNull
             @Override
             public home_sub_vieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                 View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_sub_layout , parent , false);

                 return new home_sub_vieholder(v);
             }
         };

        adapter.notifyDataSetChanged();
        adapter.startListening();
        home_sub_recycler.setAdapter(adapter);

        return root;
    }

//
//    private void showYesterdayDate() {
//
//        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yy");
//        Calendar cal = Calendar.getInstance();
//        Calendar cal1 = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
//        cal1.add(YEAR, -1);
//        yesterday_date = dateFormat.format(cal.getTime());
//        OneMonthAgo = dateFormat.format(cal1.getTime());
////        Toast.makeText(getActivity(), ""+yesterday_date, Toast.LENGTH_SHORT).show();
//    }

    private void showTotalPercentage() {

        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Total_Lecture").child(CurrentUserId);
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    int tt = snapshot.child("TL").getValue().hashCode();
                    int pp = snapshot.child("TP").getValue().hashCode();



                    float t = snapshot.child("TL").getValue().hashCode();
                    float p = snapshot.child("TP").getValue().hashCode();


                    double percentage = (p/t)*100;
                    if (String.valueOf(percentage).equals("NaN")){
                        overall_percentage.setText(0+" %");
                    }else
                    {
                        overall_percentage.setText(new DecimalFormat("##.##").format(percentage) +" %");

                    }


                    progress_bar.setProgress((int) percentage);
                    overall_lecture.setText(tt+"");
                    overall_present.setText(pp+" P");
                    int a = (tt - pp);
                    overall_absent.setText(a+" A");


                    DatabaseReference t_ref  = FirebaseDatabase.getInstance().getReference("Total_Criteria").child(CurrentUserId);
                    t_ref.child("Total_criteria").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()){
                                String t_c = snapshot.child("Total_criteria").getValue().toString();

                                showTotalCriteria(tt, pp, Integer.parseInt(t_c));
                            }
                            else {
                                overall_status.setText("Please Set Attendance Goal");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showTotalCriteria(int tt, int pp, int t_c) {

        float p = pp;
        float t = tt;

        double percentage = (p/t)*100;
//        String To = String.valueOf(individual_criteria);


        if (t_c > percentage){

            int l_need = (100*pp - tt*t_c)/(t_c - 100);
            overall_status.setText(l_need + " lectures needed to get on track. " );
        }
        else {

            int l_leave = (100*pp - tt*t_c)/(t_c);
            overall_status.setText(l_leave + " lectures can be leave" );
        }
    }

    private void CountTotalLecture() {

        mDatabase.child(CurrentUserId).orderByChild("uid").equalTo(CurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    int tl = Integer.parseInt(""+ds.child("Total_Classes").getValue());
                    int tp = Integer.parseInt(""+ds.child("Initial_Present").getValue());

                    SP = SP + tp;
                    sum = sum + tl;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Total_Lecture").child(CurrentUserId);
                    ref.child("TL").setValue(sum);
                    ref.child("TP").setValue(SP);

                }
                sum=0;
                SP =0 ;
                showTotalPercentage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showWeekName() {

        SimpleDateFormat formatLetterDay = new SimpleDateFormat("EEEEE", Locale.getDefault());
        String letter = formatLetterDay.format(new Date());
        String dayOfTheWeek = String.valueOf(letter.charAt(0));

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek1 = sdf.format(d);

//        Toast.makeText(getContext(), ""+dayOfTheWeek1, Toast.LENGTH_SHORT).show();
        if (dayOfTheWeek.equals("M")){
            mon.setBackgroundResource(R.drawable.custom_button_enable);
        }
        if (dayOfTheWeek1.equals("Tuesday")){
            tue.setBackgroundResource(R.drawable.custom_button_enable);
        }
        if (dayOfTheWeek.equals("W")){
            wed.setBackgroundResource(R.drawable.custom_button_enable);
        }
        if (dayOfTheWeek1.equals("Thursday")){
            thur.setBackgroundResource(R.drawable.custom_button_enable);
        }
        if (dayOfTheWeek.equals("F")){
            fri.setBackgroundResource(R.drawable.custom_button_enable);
        }
        if (dayOfTheWeek1.equals("Saturday")){
            sat.setBackgroundResource(R.drawable.custom_button_enable);
        }
        if (dayOfTheWeek1.equals("Sunday")){
            sun.setBackgroundResource(R.drawable.custom_button_enable);
        }
    }

    private void showDate() {
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date myDate = new Date();
        String Today_date = timeStampFormat.format(myDate);
        show_date.setText(Today_date);
    }
}