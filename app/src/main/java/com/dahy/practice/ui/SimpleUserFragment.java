package com.dahy.practice.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dahy.practice.LoginActivity;
import com.dahy.practice.MainActivity;
import com.dahy.practice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleUserFragment extends Fragment {

    private RecyclerView simpleList;
    private RecyclerView.Adapter simpleListAdapter;
    private RecyclerView.LayoutManager simpleListManager;

    View layout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       layout = inflater.inflate(R.layout.fragment_simple_user, container, false);
        getData();
        return layout;
    }

    private void getData() {

        CollectionReference userDocument = db.collection("UsersDatabase");
        userDocument.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "dicky";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Event> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                list.addAll(createEvent(document.getData().get("listOfEvents")));
                            }
                            simpleListCreate(list, layout);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void simpleListCreate(ArrayList<Event> listItems, View view){
        simpleList = view.findViewById(R.id.simpleList);

        simpleListManager = new LinearLayoutManager(getContext());
        simpleList.setLayoutManager(simpleListManager);

        simpleListAdapter = new EventListAdapter(listItems, getContext(),"showInfo");
        simpleList.setAdapter((simpleListAdapter));
    }
    private ArrayList<Event> createEvent(Object obj){
        ArrayList<HashMap<String, Object>> gotList = (ArrayList) obj;
        ArrayList<Event> events = new ArrayList<>();
        if(gotList != null) {
            for (int i = 0; i < gotList.size(); i++) {
                Event event = new Event();
                event.setName(gotList.get(i).get("name").toString());
                event.setClassIcon(gotList.get(i).get("classIcon").toString());
                event.setSite(gotList.get(i).get("site").toString());
                event.setDescription(gotList.get(i).get("description").toString());
                event.setCity(gotList.get(i).get("city").toString());
                event.setAddress(gotList.get(i).get("address").toString());
                event.setContacts(gotList.get(i).get("contacts").toString());
                event.setEndDate(gotList.get(i).get("endDate").toString());
                event.setBeginDate(gotList.get(i).get("beginDate").toString());
                if (gotList.get(i).get("previewUrl") != null) {
                    event.setPreviewUrl(gotList.get(i).get("previewUrl").toString());
                }
                events.add(event);
            }
        }
        return events;
    }
}
