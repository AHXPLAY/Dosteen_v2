package com.dahy.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.dahy.practice.R;
import com.dahy.practice.ui.Event;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class EditEventActivity extends AppCompatActivity {

    EditText name;
    Spinner eventClass;
    EditText city;
    EditText adress;
    EditText site;
    EditText description;
    Button apply;
    Button previewButton;
    ImageView previewImage;

    Toolbar toolbar;
    Event event = new Event();
    int position;
    String typeActivity;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Intent previousIntent;
    Uri imageUri;
    final static int RC_IMAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);


        previousIntent = getIntent();
        position = previousIntent.getIntExtra("pos", -1);
        typeActivity = previousIntent.getStringExtra("typeActivity");
        initializeViews(previousIntent);

        if (typeActivity.equals("add")) {
            getSupportActionBar().setTitle("Создать событие");

        } else if (typeActivity.equals("edit")) {
            getSupportActionBar().setTitle("Изменить событие");
        }
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectImage = new Intent(Intent.ACTION_GET_CONTENT);
                selectImage.setType("image/*");
                selectImage.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(selectImage, "Выберите изображение"), RC_IMAGE);
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadPreview(imageUri, mAuth.getUid());
                } else {
                    createEventObj(previousIntent.getStringExtra("preview"));
                    if (typeActivity.equals("add")) {
                        addEvent();

                    } else if (typeActivity.equals("edit")) {
                        editEvent(position);
                    }
                }


            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Picasso.get().load(selectedImageUri).into(previewImage);
            imageUri = selectedImageUri;
            //uploadUserIcon(selectedImageUri, mAuth.getUid());
        }
    }

    private void initializeViews(Intent previous) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.nameField);
        eventClass = findViewById(R.id.chooseClass);
        createSpinner();
        city = findViewById(R.id.cityField);
        adress = findViewById(R.id.adressField);
        site = findViewById(R.id.siteField);
        description = findViewById(R.id.descriptionField);
        apply = findViewById(R.id.applyButton);
        previewButton = findViewById(R.id.previewButton);
        previewImage = findViewById(R.id.previewImage);
        if (previous.getStringExtra("typeActivity").equals("edit")) {
            name.setText(previous.getStringExtra("name"));
            city.setText(previous.getStringExtra("city"));
            adress.setText(previous.getStringExtra("adress"));
            site.setText(previous.getStringExtra("site"));
            description.setText(previous.getStringExtra("description"));
            if (previous.getStringExtra("preview") != null) {
                Picasso.get().load(previous.getStringExtra("preview")).into(previewImage);
            }
            event.setClassIcon(previous.getStringExtra("classIcon"));
            switch (previous.getStringExtra("classIcon")) {
                case "Sport":
                    eventClass.setSelection(0);
                    break;
                case "Education":
                    eventClass.setSelection(1);
                    break;
                case "Buisness":
                    eventClass.setSelection(2);
                    break;
                case "Creative":
                    eventClass.setSelection(3);
                    break;
                default:
                    eventClass.setSelection(0);
            }
            apply.setText("Изменить");

        }
    }

    private void createSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.chooseClassArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        eventClass.setAdapter(adapter);
        eventClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        event.setClassIcon("Sport");
                        break;
                    case 1:
                        event.setClassIcon("Education");
                        break;
                    case 2:
                        event.setClassIcon("Buisness");
                        break;
                    case 3:
                        event.setClassIcon("Creative");
                        break;
                    default:
                        event.setClassIcon("Sport");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addEvent() {
        DocumentReference userDocument = db.collection("UsersDatabase").document(mAuth.getUid());
        userDocument.update("listOfEvents", FieldValue.arrayUnion(event)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                } else {

                }
            }
        });
    }

    private void editEvent(final int pos) {
        final DocumentReference userDocument = db.collection("UsersDatabase").document(mAuth.getUid());
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAGY", "DocumentSnapshot data: " + document.getData());
                        uploadToFirebase(document.getData().get("listOfEvents"), pos, userDocument);
                    } else {
                    }
                } else {
                    Log.d("TAGY", "get failed with ", task.getException());
                }
            }
        });
    }

    private void uploadToFirebase(Object obj, int pos, DocumentReference userDocument) {
        ArrayList list = (ArrayList) obj;
        list.set(pos, event);
        userDocument.update("listOfEvents", list).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                } else {

                }
            }
        });

    }

    private void createEventObj(String uri) {
        event.setName(name.getText().toString());
        event.setAdress(adress.getText().toString());
        event.setCity(city.getText().toString());
        event.setDescription(description.getText().toString());
        event.setSite(site.getText().toString());
        event.setPreviewUrl(uri);


    }

    public void uploadPreview(Uri uri, final String userId) {
        final StorageReference userImageRef = storage.getReference().child(userId).child("eventsPreviews").child(generateUniqueID());
        final UploadTask UT = userImageRef.putFile(uri);


        Task<Uri> urlTask = UT.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return userImageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    createEventObj(downloadUri.toString());
                    Log.d("ImageURL", downloadUri.toString());
                    if (typeActivity.equals("add")) {
                        addEvent();

                    } else if (typeActivity.equals("edit")) {
                        editEvent(position);
                    }
                } else {
                    // Handle failures
                    // ...a
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(previousIntent.getStringExtra("typeActivity").equals("edit")){
            getMenuInflater().inflate(R.menu.delete_menu, menu);
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deleteEvent:
                deleteEvent();

        }
        return super.onOptionsItemSelected(item);

    }

    private void deleteEvent() {

        DocumentReference userDocument = db.collection("UsersDatabase").document(mAuth.getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put("adress", previousIntent.getStringExtra("adress"));
        map.put("city", previousIntent.getStringExtra("city"));
        map.put("classIcon", previousIntent.getStringExtra("classIcon"));
        map.put("constant", false);
        map.put("description", previousIntent.getStringExtra("description"));
        map.put("name", previousIntent.getStringExtra("name"));
        map.put("picturesUrls", null);
        map.put("previewUrl", previousIntent.getStringExtra("preview"));
        map.put("site", previousIntent.getStringExtra("site"));


        userDocument.update("listOfEvents", FieldValue.arrayRemove(map)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    private String generateUniqueID(){
        UUID id = UUID.randomUUID();
        Log.d("UniqueID", id.toString());
        return id.toString();
    }
}