package com.dahy.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.dahy.practice.ui.SimpleUserFragment;
import com.dahy.practice.ui.TeacherFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button signOut;
    DrawerLayout drawer;
    NowFragment nowFragment;
    Fragment thisFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("searchTODO", "Создалось активити");
       // Intent intent = getIntent();
        //if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        //    String query = intent.getStringExtra(SearchManager.QUERY);
       //     Log.d("searchTODO", query);
        //    sendToFrangment(query);
      //  }


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_simple_user, R.id.nav_teacher, R.id.nav_account)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int id = destination.getId();
                if(id == R.id.nav_account){
                    nowFragment = NowFragment.Account;
                }
                else if(id == R.id.nav_teacher){
                    nowFragment = NowFragment.Teacher;
                }
                else if(id == R.id.nav_simple_user){
                    nowFragment = NowFragment.SimpleUser;
                }
            }

        });
    }



    private void sendToFrangment(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("searchQuery", query);

        if(nowFragment == NowFragment.SimpleUser){
            SimpleUserFragment fragmentObj = new SimpleUserFragment();
            fragmentObj.setArguments(bundle);
        }
        else if(nowFragment == NowFragment.Teacher){
            TeacherFragment fragmentObj = new TeacherFragment();
            fragmentObj.setArguments(bundle);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.signOut:
                signOut();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }
    private void signOut(){
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
    enum NowFragment{
        SimpleUser,
        Teacher,
        Account
    }

}
