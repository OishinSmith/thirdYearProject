package com.example.userid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.userid.Utils.TopNavigationViewHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class profile extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    private Button signOuts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupTopNavigationView();

        signOuts = findViewById(R.id.sign_out_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) //use google sign in information from login Activity to sign out.
                .requestIdToken("1062534548149-ksfp63kf6c7eh8nfrssbej0jb7qj5bmj.apps.googleusercontent.com") //could use getDefaultToken
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signOuts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOuts.setVisibility(View.GONE);
                mGoogleSignInClient.signOut();
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setupTopNavigationView(){
        Log.d("nav", "setupTopNavigationView: ");
        BottomNavigationViewEx bottomNavigationViewEX = (BottomNavigationViewEx) findViewById(R.id.containerMenu);
        TopNavigationViewHelper.setupTopNavigationView(bottomNavigationViewEX);

        TopNavigationViewHelper.enableNavigation(profile.this, bottomNavigationViewEX);

        Menu menus = bottomNavigationViewEX.getMenu();
        MenuItem menuItem = menus.getItem(0);
        menuItem.setChecked(true);



    }

}
