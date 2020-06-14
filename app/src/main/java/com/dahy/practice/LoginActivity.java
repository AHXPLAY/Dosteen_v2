package com.dahy.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dahy.practice.ui.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    EditText email;
    EditText password;
    EditText passwordConfrimation;
    Button logButton;
    TextView header;
    TextView changer;
    ProgressBar progressBar;

    UserState nowState = UserState.signOn;

    boolean allIsOK = false;
    FirebaseUser currentUser = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();


        if(currentUser != null){
            toMain();
        }

        stateChangeListener();

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                checkConditions();
                if(allIsOK){
                    if(nowState == UserState.signOn){
                        createUser();
                    } else{
                        loginUser();
                    }
                }

            }
        });

    }


    private void initializeViews(){
        email = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        passwordConfrimation = findViewById(R.id.passwordConfirmField);
        header = findViewById(R.id.loginHeader);
        logButton = findViewById(R.id.loginButton);
        changer = findViewById(R.id.stateChanger);
        progressBar = findViewById(R.id.loginProgressBar);
    }

    private void isSignIn(){
        changer.setText(R.string.changeToReg);
        header.setText(R.string.logHeader);
        passwordConfrimation.setVisibility(View.GONE);
        logButton.setText(R.string.logButton);

    }

    private void isSignOn(){
        changer.setText(R.string.changeToLog);
        header.setText(R.string.regHeader);
        passwordConfrimation.setVisibility(View.VISIBLE);
        logButton.setText(R.string.regButton);
    }

    private void createUser(){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            addUserToDB(currentUser.getUid());
                            progressBar.setVisibility(View.GONE);
                            toMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void addUserToDB(String UserId){
        DocumentReference userDocument= db.collection("UsersDatabase").document(UserId);
        userDocument.set(new User());

    }

    private void loginUser(){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            currentUser = mAuth.getCurrentUser();
                            progressBar.setVisibility(View.GONE);
                            toMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Неправильный пароль или email",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void stateChangeListener(){
        changer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowState == UserState.signOn){
                    nowState = UserState.signIn;
                    isSignIn();

                } else {
                    nowState = UserState.signOn;
                    isSignOn();
                }

            }
        });
    }

    private void toMain(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void checkConditions(){
        allIsOK = false;

        if(email.getText().toString().length() == 0 || email.getText().toString() == null ) {
            Toast.makeText(LoginActivity.this, "Вы не написали почту",
                    Toast.LENGTH_SHORT).show();
        }
        else if(password.getText().toString().length() == 0 || password.getText().toString() == null) {
            Toast.makeText(LoginActivity.this, "Вы не написали пароль",
                    Toast.LENGTH_SHORT).show();
        }
        else if (!(password.getText().toString().equals(passwordConfrimation.getText().toString())) && nowState == UserState.signOn){
            Toast.makeText(LoginActivity.this, "Пароли не совпадают",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            allIsOK = true;
        }
    }

    enum UserState{
        signIn,
        signOn
    }
}
