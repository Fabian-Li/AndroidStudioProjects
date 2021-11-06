package com.example.studentplanerguide.mainPages.subjects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.studentplanerguide.Data.skillsList;
import com.example.studentplanerguide.R;
import com.example.studentplanerguide.adapters.RecyclerViewPracticeSkillsAdapter;
import com.example.studentplanerguide.adapters.RecyclerViewSkillsAdapter;
import com.example.studentplanerguide.adapters.RecyclerViewTopicsAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PracticeSkillsActivity extends AppCompatActivity {

    private Intent information;
    private TextView textView;
    String subjectname;
    String subjectid;
    String subjectlocation;

    private List<skillsList> skillsLists;

    private final FirebaseFirestore firebasefirestore = FirebaseFirestore.getInstance();

    private RecyclerView recyclerViewTasks;
    private RecyclerViewPracticeSkillsAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_skills);

        textView = findViewById(R.id.skillsPracticeName);
        recyclerViewTasks = findViewById(R.id.practiceTasksRecyclerView);

        information = getIntent();
        subjectname = information.getStringExtra(RecyclerViewSkillsAdapter.EXTRA_NAME);
        subjectid = information.getStringExtra(RecyclerViewSkillsAdapter.EXTRA_ID);
        subjectlocation = information.getStringExtra(RecyclerViewSkillsAdapter.EXTRA_LOCATION);
        textView.setText(subjectname);
        System.out.println(subjectlocation + "/" + subjectname);
        initData();
    }

    private void initRecyclerView() {
        taskAdapter = new RecyclerViewPracticeSkillsAdapter(this, skillsLists);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.setAdapter(taskAdapter);
    }

    private void initData() {
        skillsLists = new ArrayList<>();
        firebasefirestore.collection(subjectlocation + "/" + subjectname).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    skillsLists.add(new skillsList((String) document.get("name"), subjectlocation +"/"+ subjectname + "/", document.getId()));
                }
                Log.d("testingingings", skillsLists.toString());
                initRecyclerView();
            } else {
                Log.d("testings", "Error getting documents: ", task.getException());
            }
        });
    }
}