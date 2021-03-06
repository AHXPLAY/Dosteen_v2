package com.dahy.practice.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dahy.practice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleUserFragment extends Fragment {

    private RecyclerView simpleList;
    private EventListAdapter simpleListAdapter;
    private RecyclerView.LayoutManager simpleListManager;

    private ProgressBar progressBar;
    View layout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       layout = inflater.inflate(R.layout.fragment_simple_user, container, false);
        getData();
        setHasOptionsMenu(true);
        progressBar = getActivity().findViewById(R.id.mainProgressBar);
        progressBar.setVisibility(View.VISIBLE);
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
        if(listItems != null){
            progressBar.setVisibility(View.GONE);
            simpleList = view.findViewById(R.id.simpleList);

            simpleListManager = new LinearLayoutManager(getContext());
            simpleList.setLayoutManager(simpleListManager);

            simpleListAdapter = new EventListAdapter(listItems, getContext(),"showInfo");
            simpleList.setAdapter((simpleListAdapter));
        }

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
                simpleListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("newSearchQuery", newText);
                simpleListAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
