package com.example.userid.Utils;
import com.example.userid.LoginActivity;
import com.example.userid.customerMainScreen;
import com.example.userid.information;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.userid.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class TopNavigationViewHelper {

    public static void setupTopNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d("bottom", "setupTopNavigationView: setting up TopView");
        bottomNavigationViewEx.setTextVisibility(true);

    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_ids:
                        Intent intent1 = new Intent(context, customerMainScreen.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.action_information:
                        Intent intent2 = new Intent(context, information.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.action_profile:
                        final GoogleSignInClient mGoogleSignInClient;

                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) //use google sign in information from login Activity to sign out.
                                .requestIdToken("1062534548149-ksfp63kf6c7eh8nfrssbej0jb7qj5bmj.apps.googleusercontent.com") //could use getDefaultToken
                                .requestEmail()
                                .build();

                        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
                        mGoogleSignInClient.signOut();
                        Intent intent3 = new Intent(context, LoginActivity.class);
                        context.startActivity(intent3);

                        break;
                }
                return false;
            }
        });
    }

}
