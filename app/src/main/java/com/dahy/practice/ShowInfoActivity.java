package com.dahy.practice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ShowInfoActivity extends AppCompatActivity {
    TextView name;
    TextView type;
    TextView city;
    TextView adress;
    TextView site;
    TextView description;
    TextView contacts;
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        initializeViews();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Информация о событии");

        Intent chosenEventIntent = getIntent();

        fillFields(chosenEventIntent);
    }

    private void fillFields(Intent intent) {
        name.setText(intent.getStringExtra("name"));
        switch (intent.getStringExtra("classIcon")){
            case "Sport" :
                type.setText("Спорт");
                break;
            case "Education" :
                type.setText("Образование");
                break;
            case "Buisness" :
                type.setText("Бизнес");
                break;
            case "Creative" :
                type.setText("Творчество");
                break;
            default:
                type.setText("Спорт");
        }
        city.setText(intent.getStringExtra("city"));
        adress.setText(intent.getStringExtra("address"));
        description.setText(intent.getStringExtra("description"));
        site.setText(intent.getStringExtra("site"));
        contacts.setText(intent.getStringExtra("contacts"));
        String beginDate = intent.getStringExtra("beginDate");
        String endDate =  intent.getStringExtra("endDate");
        if ((beginDate != null && endDate != null) && !(beginDate.isEmpty() || endDate.isEmpty())){
            date.setText(beginDate + " - " + endDate);
        }else{
            date.setText("Постоянно");
        }


    }

    private void initializeViews(){
        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        city = findViewById(R.id.city);
        adress = findViewById(R.id.adress);
        site = findViewById(R.id.site);
        description = findViewById(R.id.description);
        contacts = findViewById(R.id.contacts);
        date = findViewById(R.id.date);
    }

}
