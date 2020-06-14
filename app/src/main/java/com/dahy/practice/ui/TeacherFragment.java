package com.dahy.practice.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dahy.practice.EditEventActivity;
import com.dahy.practice.MainActivity;
import com.dahy.practice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FloatingActionButton addEvent;

    RecyclerView eventsList;
    RecyclerView.Adapter listAdapter;
    RecyclerView.LayoutManager listLM;
    View container;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_teacher, container, false);
        initializeViews(view);


        //getData();

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditEventActivity.class);
                intent.putExtra("typeActivity", "add");
                startActivity(intent);
            }
        });



        return view;
    }

    private void initializeViews(View view){
        addEvent = view.findViewById(R.id.addEvent);
        eventsList = view.findViewById(R.id.eventsList);
        progressBar = getActivity().findViewById(R.id.mainProgressBar);
        container = view.findViewById(R.id.teacherContainer);
    }
    private void createRecyclerView(ArrayList<Event> list){
        listAdapter = new EventListAdapter(list, getContext(), "teacher");
        eventsList.setAdapter(listAdapter);
        listLM = new LinearLayoutManager(getContext());
        eventsList.setLayoutManager(listLM);
    }

    private void getData(){
        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);

        DocumentReference userDocument = db.collection("UsersDatabase").document(mAuth.getUid());
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "PPAP" ;

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("listOfEvents"));
                        addEventList(document.getData().get("listOfEvents"));

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    private void addEventList(Object obj){
        if(obj == null){
            progressBar.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }else{
            ArrayList<HashMap<String, Object>> gotList = (ArrayList) obj;
            ArrayList<Event> events = new ArrayList<>();
            for (int i = 0; i < gotList.size(); i++){
                Event event = new Event();
                event.setName(gotList.get(i).get("name").toString());
                event.setClassIcon(gotList.get(i).get("classIcon").toString());
                event.setSite(gotList.get(i).get("site").toString());
                event.setDescription(gotList.get(i).get("description").toString());
                event.setCity(gotList.get(i).get("city").toString());
                event.setAdress(gotList.get(i).get("adress").toString());
                if(gotList.get(i).get("previewUrl") != null) {
                    event.setPreviewUrl(gotList.get(i).get("previewUrl").toString());
                }
                events.add(event);
            }
            createRecyclerView(events);
            progressBar.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }
}