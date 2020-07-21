package com.dahy.practice.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
    EventListAdapter listAdapter;
    RecyclerView.LayoutManager listLM;
    View container;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_teacher, container, false);
        initializeViews(view);
        setHasOptionsMenu(true);
        //String searchQuery = getArguments().getString("searchQuery");
        //Log.d("searchQuery", searchQuery);

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
                event.setAddress(gotList.get(i).get("address").toString());
                event.setConstant((boolean)gotList.get(i).get("constant"));
                event.setContacts(gotList.get(i).get("contacts").toString());
                event.setBeginDate(gotList.get(i).get("beginDate").toString());
                event.setEndDate(gotList.get(i).get("endDate").toString());
                //Log.d("ConstantMean",gotList.get(i).get("constant").toString() );
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("newSearchQuery", query);
                listAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("newSearchQuery", newText);
                listAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
