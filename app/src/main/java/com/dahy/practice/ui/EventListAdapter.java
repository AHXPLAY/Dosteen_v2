package com.dahy.practice.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dahy.practice.EditEventActivity;
import com.dahy.practice.MainActivity;
import com.dahy.practice.R;
import com.dahy.practice.ShowInfoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventHolder> {

    private ArrayList<Event> eventsList;
    private Context context;
    private String fragmentType;

    public static class EventHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView preview;
        public ImageView classIcon;
        public TextView city;
        public View container;

        public EventHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            preview = itemView.findViewById(R.id.preview);
            classIcon = itemView.findViewById(R.id.classIcon);
            city = itemView.findViewById(R.id.city);
            this.container = itemView;
        }

    }

    public EventListAdapter(ArrayList<Event> eventsList, Context context, String fragmentType) {
        this.eventsList = eventsList;
        this.context = context;
        this.fragmentType = fragmentType;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false);
        EventHolder holder = new EventHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(EventHolder holder, final int position) {
            final Event nowItem = eventsList.get(position);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentType.equals("teacher")) {
                    forTeacher(position, nowItem);
                }else{
                forUser(nowItem);
                }
        }
          });

            holder.name.setText(nowItem.getName());
            holder.classIcon.setImageResource(chooseIcon(nowItem.getClassIcon()));
        if (nowItem.getPreviewUrl() != null){
            Log.d("previewDebug", nowItem.getPreviewUrl());
            holder.preview.setVisibility(View.VISIBLE);
            Picasso.get().load(nowItem.getPreviewUrl()).into(holder.preview);
        } else{
            Log.d("previewDebug", "unsucces");

            holder.preview.setVisibility(View.GONE);
        }

            holder.city.setText(nowItem.getCity());

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    private int chooseIcon(String classIcon){
        switch (classIcon){
            case "Sport":
                return R.drawable.sport_icon;
            case "Buisness":
                return R.drawable.buisness_icon;
            case "Education":
                return R.drawable.edu_icon;
            case "Creative":
                return R.drawable.creative_icon;
            default:
               return R.drawable.sport_icon;
        }
    }
    private void forTeacher(int position, Event nowItem){
        Intent intent = new Intent(context, EditEventActivity.class);
        intent.putExtra("typeActivity", "edit");
        intent.putExtra("pos", position);
        // Log.d("ggggg", "" + position);
        intent.putExtra("name", nowItem.getName());
        intent.putExtra("classIcon", nowItem.getClassIcon());
        intent.putExtra("city", nowItem.getCity());
        intent.putExtra("adress", nowItem.getAdress());
        intent.putExtra("description", nowItem.getDescription());
        intent.putExtra("site", nowItem.getSite());
        intent.putExtra("preview", nowItem.getPreviewUrl());
        context.startActivity(intent);
    }
    private void forUser(Event nowItem){
        Intent intent = new Intent(context, ShowInfoActivity.class);
        intent.putExtra("name", nowItem.getName());
        intent.putExtra("classIcon", nowItem.getClassIcon());
        intent.putExtra("city", nowItem.getCity());
        intent.putExtra("adress", nowItem.getAdress());
        intent.putExtra("description", nowItem.getDescription());
        intent.putExtra("site", nowItem.getSite());
        intent.putExtra("preview", nowItem.getPreviewUrl());
        context.startActivity(intent);
    }
}
