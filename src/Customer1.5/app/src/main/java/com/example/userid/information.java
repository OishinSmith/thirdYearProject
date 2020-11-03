package com.example.userid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.userid.Utils.TopNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        setupTopNavigationView();
        TextView tv = findViewById(R.id.instructionstv);
        TextView tv2 = findViewById(R.id.instructionstv2);
        TextView tv3 = findViewById(R.id.instructionstv3);
        TextView tv4 = findViewById(R.id.instructionstv4);
        TextView tv5 = findViewById(R.id.instructionstv5);
        TextView tv6 = findViewById(R.id.instructionstv6);

        tv2.setText("1 Registration:");
        tv3.setText("You must bring your physical ID in order to store your virtual ID on your phone. The Bouncer will ask for identification, and at that point you may ask to register your ID on the app. the Bouncer will verify your physical ID and take a picture of it, in order to be able to store it online. Then the Bouncer will go to you and send a code through NFC. When you receive the code, you will be able to name the ID (for example 'Passport') and the ID will now be automatically stored on your phone for future use.");
        tv4.setText("2 Show ID:");
        tv5.setText("Now that your ID has been stored, you've probably noticed that a button with your chosen ID name is appearing on the Main Screen. If you ever want any Bouncer to check your ID, press the button with your chosen ID name and notice that the NFC image will appear. The Bouncer will now ask to see your phone and only then, can you send your ID code through NFC to the bouncer.");
        tv6.setText("And that's it! Enjoy.");

    }

    private void setupTopNavigationView(){
        Log.d("nav", "setupTopNavigationView: ");
        BottomNavigationViewEx bottomNavigationViewEX = (BottomNavigationViewEx) findViewById(R.id.containerMenu);
        TopNavigationViewHelper.setupTopNavigationView(bottomNavigationViewEX);

        TopNavigationViewHelper.enableNavigation(information.this, bottomNavigationViewEX);

        Menu menus = bottomNavigationViewEX.getMenu();
        MenuItem menuItem = menus.getItem(2);
        menuItem.setChecked(true);

    }
}
