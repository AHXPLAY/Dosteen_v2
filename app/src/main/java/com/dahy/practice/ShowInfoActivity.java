package com.dahy.practice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
    ImageView preview;
    ProgressBar imageLoading;

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
        String previewUrl = intent.getStringExtra("preview");
        if(previewUrl != null){
            imageLoading.setVisibility(View.VISIBLE);
            Picasso.get().load(previewUrl).into(preview, new Callback() {
                @Override
                public void onSuccess() {
                    imageLoading.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("loadImageException", e.toString());
                    Toast.makeText(ShowInfoActivity.this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        site.setMovementMethod(LinkMovementMethod.getInstance());
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
        preview = findViewById(R.id.previewImage);
        imageLoading = findViewById(R.id.imageLoading);
    }

}
