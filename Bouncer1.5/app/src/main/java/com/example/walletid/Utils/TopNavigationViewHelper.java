package com.example.walletid.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.walletid.AddID2;
import com.example.walletid.AdminLogin;
import com.example.walletid.CheckIds;
import com.example.walletid.R;
import com.google.firebase.auth.FirebaseAuth;
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
                    case R.id.action_addIds: //1
                        Intent intent1 = new Intent(context, AddID2.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.action_checkID: //2
                        Intent intent2 = new Intent(context, CheckIds.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.action_logoutMenu: //0
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        context.startActivity(new Intent(context, AdminLogin.class));
                        break;
                    case R.id.action_requestPermissions:
                        Uri urlToGoogleForm = Uri.parse("https://goo.gl/forms/zPCIaCke6J9rB7bH3");
                        context.startActivity(new Intent(Intent.ACTION_VIEW, urlToGoogleForm));
                        break;
                }
                return false;
            }
        });
    }

}
