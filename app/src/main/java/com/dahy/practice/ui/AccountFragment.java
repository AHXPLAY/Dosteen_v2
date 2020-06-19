package com.dahy.practice.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dahy.practice.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {
    ImageView userIcon;
    ImageView editIcon;
    Button changeProfileButton;
    Button cancelButton;
    TextView usernameLabel;
    EditText usernameEdit;
    TextView achievementsLabel;
    EditText achievementsEdit;

    View progressBar;
    RelativeLayout mainContainer;
    RelativeLayout applyProgressBar;

    ProfileState profileState = ProfileState.isIdle;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    Uri imageUri;
    final static  int RC_IMAGE = 123;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        final String userId = mAuth.getUid();
        progressBar = getActivity().findViewById(R.id.mainProgressBar);
        initializeViews(view);
        progressBar.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);

        getUserData(userId);


        editIcon.setClickable(false);
        userIcon.setClickable(false);
        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileState == ProfileState.isIdle){
                    toChange();
                    toFillEdits();
                }else{
                    toIdle();
                    toApply(userId);

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toIdle();
                //toCLeanEdits();
            }
        });


        imageClickListeners();

        return view;

    }

    private void initializeViews(View view){
        cancelButton = view.findViewById(R.id.cancel_button);
        mainContainer = view.findViewById(R.id.account_container);
        applyProgressBar = view.findViewById(R.id.applyProgressBar);

        userIcon = view.findViewById(R.id.user_image);
        editIcon = view.findViewById(R.id.editImage);
        changeProfileButton = view.findViewById(R.id.changeDataBtn);

        usernameLabel = view.findViewById(R.id.usernameLabel);
        usernameEdit = view.findViewById(R.id.usernameEdit);

        achievementsLabel = view.findViewById(R.id.achievementsLabel);
        achievementsEdit = view.findViewById(R.id.achievementsEdit);    }

    private void toChange(){
        profileState = ProfileState.isChanging;
        changeProfileButton.setText(R.string.changeProfileButtonChanging);
        cancelButton.setVisibility(View.VISIBLE);

        editIcon.setVisibility(View.VISIBLE);
        editIcon.setClickable(true);
        userIcon.setClickable(true);

        usernameLabel.setVisibility(View.GONE);
        usernameEdit.setVisibility(View.VISIBLE);

        achievementsLabel.setVisibility(View.GONE);
        achievementsEdit.setVisibility(View.VISIBLE);


    }
    private void toIdle(){
        profileState = ProfileState.isIdle;
        changeProfileButton.setText(R.string.changeProfileButtonIdle);
        cancelButton.setVisibility(View.GONE);

        editIcon.setVisibility(View.GONE);
        editIcon.setClickable(false);
        userIcon.setClickable(false);

        usernameLabel.setVisibility(View.VISIBLE);
        usernameEdit.setVisibility(View.GONE);

        achievementsLabel.setVisibility(View.VISIBLE);
        achievementsEdit.setVisibility(View.GONE);
    }
    private void toApply(String userId){
        applyProgressBar.setVisibility(View.VISIBLE);

        if (imageUri != null){
            uploadUserIcon(imageUri, userId);
        } else {
            applyProgressBar.setVisibility(View.GONE);
            changeFields();
        }

        DocumentReference userDocument = db.collection("UsersDatabase").document(userId);
        userDocument.update("ahivements", achievementsEdit.getText().toString());
        userDocument.update("name", usernameEdit.getText().toString());

    }
    private void toSelectProfileImage(){
        Intent selectImage = new Intent(Intent.ACTION_GET_CONTENT);
        selectImage.setType("image/*");
        selectImage.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(selectImage, "Выберите изображение"), RC_IMAGE);

    }

    private void changeFields() {
        ArrayList<View> labelsList = new ArrayList<>();
        ArrayList<View> editsList = new ArrayList<>();

        labelsList.add(usernameLabel);
        labelsList.add(achievementsLabel);

        editsList.add(usernameEdit);
        editsList.add(achievementsEdit);

        for(int i = 0; i < labelsList.size(); i++){
            TextView label =(TextView)labelsList.get(i);
            EditText edit =(EditText)editsList.get(i);
            label.setText(edit.getText().toString());
        }

    }

    private void imageClickListeners(){
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSelectProfileImage();
            }
        });

        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSelectProfileImage();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_IMAGE && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            Picasso.get().load(selectedImageUri).into(userIcon);
            imageUri = selectedImageUri;
            //uploadUserIcon(selectedImageUri, mAuth.getUid());
        }
    }

    private void getUserData(String uid){
        DocumentReference userDocument = db.collection("UsersDatabase").document(uid);

        userDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                setFieldsData(user);

            }
        });
    }


    private void setFieldsData(User user){
            usernameLabel.setText(user.getName());
            achievementsLabel.setText(user.getAhivements());
            if (checkIfImageExists(user.getImageUrl())) {
                Picasso.get().load(user.getImageUrl()).into(userIcon, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("imageLoadingTrouble: ", "succes");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("imageLoadingTrouble: ", e.toString());

                    }
                });
                progressBar.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);

            }else{
                progressBar.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);
            }

    }


    public void uploadUserIcon(Uri uri, final String userId){

        final StorageReference userImageRef = storage.getReference().child(userId).child("profileImage");
        final UploadTask uploadTask = userImageRef.putFile(uri);



        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    Log.d("ImageURL", downloadUri.toString());
                    DocumentReference userDocument = db.collection("UsersDatabase").document(userId);
                   userDocument.update("imageUrl", downloadUri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            applyProgressBar.setVisibility(View.GONE);
                            changeFields();
                        }
                    });

                } else {
                    // Handle failures
                    // ...a
                }
            }
        });
    }

    private void toFillEdits(){
        usernameEdit.setText(usernameLabel.getText().toString());
        achievementsEdit.setText(achievementsLabel.getText().toString());
    }

    private boolean checkIfImageExists(String url){
        if (url.equals("")){
            return false;
        }else{
            return true;
        }
    }
    enum ProfileState{
        isChanging,
        isIdle
    }

}
