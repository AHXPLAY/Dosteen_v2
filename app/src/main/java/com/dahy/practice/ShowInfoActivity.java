package com.dahy.practice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ShowInfoActivity extends AppCompatActivity {
    TextView name;
    TextView type;
    TextView city;
    TextView adress;
    TextView site;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        initializeViews();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Информация события");

        Intent choseEvent = getIntent();

        fillFields(choseEvent);
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
        adress.setText(intent.getStringExtra("adress"));
        description.setText(intent.getStringExtra("description"));
        site.setText(intent.getStringExtra("site"));

    }

    private void initializeViews(){
        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        city = findViewById(R.id.city);
        adress = findViewById(R.id.adress);
        site = findViewById(R.id.site);
        description = findViewById(R.id.description);

    }
}
