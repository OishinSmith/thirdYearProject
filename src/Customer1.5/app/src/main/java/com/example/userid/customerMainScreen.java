package com.example.userid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.userid.Utils.TopNavigationViewHelper;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.HashMap;

public class customerMainScreen extends AppCompatActivity{

    AutoCompleteTextView textIn;
    Button buttonAdd;
    LinearLayout container;

    //SharedPreferences
    public SharedPreferences sp;
    public String userAutoLogin;

    //TextView reList, info;
    public String nfcUrl = "";
    public String newNfcUrl = "";
    public HashMap<String,String> map = new HashMap<String,String>();
    public HashMap<String,String> maps = new HashMap<String,String>();
    public Gson gson = new Gson();

    //menu navigation

    //Database
    private String userID;
    //private Button buttonAdd;

    private static final String TAG = "ViewDatabase";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    public DataSnapshot dataSnapshot;
    public String prints;
    public String dynamicUserKeyValue;
    public String dynamicButtonInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check whether we're recreating a previously destroyed instance
        setContentView(R.layout.activity_customer_main_screen);

        //SharedPreferences Authentication
        sp = getSharedPreferences("myPref", MODE_PRIVATE);
        userAutoLogin = sp.getString("user_id", "");

        mAuth = FirebaseAuth.getInstance();                 //create an instance of auth to get Current userID
        mFirebaseDatabase = FirebaseDatabase.getInstance(); //make instance to read and write from database
        myRef = mFirebaseDatabase.getReference();           //get a reference to the database, basically the link to the firebase database that we are using to be able to write and read info from
        FirebaseUser user = mAuth.getCurrentUser();         //get current User
        userID = user.getUid();                             //get current UserID

        textIn = (AutoCompleteTextView) findViewById(R.id.textin); //this is the text box that a user can use to name their ID


        buttonAdd = (Button) findViewById(R.id.add);               //Button add can be pressed to generate an ID, BUT ONLY, if an Nfc Url has arrived
        container = (LinearLayout) findViewById(R.id.container);   //container refers to the box that the ID's go into.

        setupTopNavigationView();

        Intent intent = getIntent();
        //when android notices that another android is sending data, android beam.
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            //get the Ndef messages, turn them into parceable
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            //Im assuming getRecords()[0].getPayload() gets the message 0
            // record 0 contains the MIME type, record 1 is the AAR, if present
            Log.d("STATE","NFC message is received: "+message);
            // store url in the form of a string from bouncer.
            newNfcUrl = new String(message.getRecords()[0].getPayload());
            //NewNfcUrl will now store the Url sent from the second app. This message will now be stored by the customer.
        }
        Log.d("nfc", "onCreate: " + nfcUrl + " " + newNfcUrl);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //basically what Im wanting to do here is, user the content generated in row.xml and store it inside of R.layout.activity_customer_main_screen
                //throught the use of LayoutInflater

                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row, null);
                if(!nfcUrl.contains("://firebasestorage.googleapis")){ //if not valid URL, show this error message, used for flair.
                    Log.d("STATE","nfcURL is not in firebase");
                    Toast.makeText(customerMainScreen.this, "No ID code has arrived.", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("size", "onClick: " + (map.size()));
                if(map.size() > 4){ //error message is displayed if there are more than 6 ID's
                    Log.d("bate","ID limit reached");
                    Toast.makeText(customerMainScreen.this, "ID limit has been reached", Toast.LENGTH_LONG).show();
                    return;
                }

                Button buttonRemove = addView.findViewById(R.id.remove);        // make an instance of the remove button inside row.xml
                final Button IDbutton = addView.findViewById(R.id.IDbutton);    //make an instance of IDbutton button inside of row.xml
                IDbutton.setText(textIn.getText().toString());                  //set the text of the button
                IDbutton.setTag(nfcUrl);                                        //setTag is basically metadata, so when IDbutton is clicked, it will pass on this metadata to be used in the next activity.
                //Log.d("STATE", str);

                if(map.containsKey(IDbutton.getText().toString().toLowerCase())){ //if the map already has an ID with this name, then show an error message, this is just for flair.
                    Log.d("bate","Value already in map");
                    Toast.makeText(customerMainScreen.this, "An ID with that name already exists.", Toast.LENGTH_LONG).show();
                    return;
                }

                String savedInput = textIn.getText().toString().toLowerCase(); //when the user receives a URL, then they will save it to this variable
                map.put(savedInput, nfcUrl);                                   //map will now "put" saved input and nfcUrl. increasing map size by 1.

                //buttons like buttonRemove and the output text are being instantiated and put into addView, which is
                //the LinearLayout, inside the Linear Layout. A mini version of row.xml



                buttonRemove.setOnClickListener(new View.OnClickListener() { //when this button is clicked, the ID is removed and deleted from map
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(customerMainScreen.this);

                        //set a little for alert dialog
                        builder.setTitle("Are you sure?");

                        //ask question
                        builder.setMessage("Are you sure? The ID will be deleted permanently.");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final Button IDbuttons = addView.findViewById(R.id.IDbutton);
                                String str = IDbuttons.getText().toString(); // get the text from button, such as "Passport"
                                map.remove(str); // remove the ID from map and its coresponding Url.
                                Gson json = new Gson();
                                String hashMapStrings = json.toJson(map);
                                Log.d("remove", "onClick: removing button " + hashMapStrings); //testing purposes.
                                //Toast.makeText(customerMainScreen.this, i+" Trying to save", Toast.LENGTH_LONG).show();
                                save(); //save current map, which will be decreased in size by 1
                                ((LinearLayout) addView.getParent()).removeView(addView); // remove the ID from user.
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });

                        AlertDialog dialog = builder.create();
                        //display the dialogue
                        dialog.show();
                    }
                });




                IDbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String message = (String) IDbutton.getTag();                    //get URL from  from ID, when the ID button is pressed.
                        Intent intent = new Intent(getBaseContext(), ShowID.class);     //make an intent, to go from this activity to showID activity.
                        intent.putExtra("EXTRA_SESSION_ID", message);            //use this URL as a message for the next Activity to use.
                        save();                                                        //save the current map
                        startActivity(intent);                                         //go to the ShowID activity.

                    }
                });


                save(); //save the current map
                container.addView(addView); //add the button to the container.

            }

        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get a dataSnapshot from firebase database, then assign the key and value pairs.
                //dataSnapshot.child(userID).getValue().toString(); will equal to the HashMap information
                //dynamicUserKeyValue = dataSnapshot.child(userID).getKey().toString(); will equal the UserID
                Gson json = new Gson(); //this is used to convert a map to a String
                dynamicButtonInformation = dataSnapshot.child(userID).getValue().toString();
                dynamicUserKeyValue = dataSnapshot.child(userID).getKey().toString();
                Log.d("STATE", "onDataChange inside of firebase database: "+dynamicButtonInformation + " " + dynamicUserKeyValue);
                java.lang.reflect.Type types = new TypeToken<HashMap<String, String>>(){}.getType();
                HashMap<String, String> testHashMap2 = json.fromJson(dynamicButtonInformation, types); //turn the Hashmap String that was retrieved from firebase Database into a HashMap

                //nfcUrl = "https://firebasestorage.googleapis/test"; //testing

                Log.d("STATE",dynamicUserKeyValue + " checking UserID");

                if ((dynamicButtonInformation!=null) || (dynamicButtonInformation != "{}")) // this is used to generate the dynamically generated content once only.
                {
                    Log.d("STATE",dynamicButtonInformation + " generating Buttons");
                    Gson gson1 = new Gson();
                    java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                    HashMap<String, String> maps = gson1.fromJson(dynamicButtonInformation, type); //get the button information eg {passport:"www.firebase.com"} and turn it from string to HashMap
                    //generate Content.

                    //map = testHashMap3;
                    String hashMapStrings = gson.toJson(maps); //for Logcat purposes
                    Log.d("data", "onDataChange: map1" + hashMapStrings);

                    for (String key : maps.keySet()) { //generate the buttons
                        Log.d("STATE","key: " + key + " value: " + maps.get(key));
                        textIn.setText(key); // make TextIn box equal the key, i.e. the customer generated ID name.
                        nfcUrl = maps.get(key); //Make the button metadata equal the URL for the image.
                        buttonAdd.performClick(); //simulate a button click. with all the relevant data
                        // ...
                    }
                }
                // get current HashMap from database, store it in a local variable called map, update information in map, then set the information to database.
                //next time the activity is Destroyed or paused or recreated, all the buttons are generated again.
                textIn.setText("");
                map = testHashMap2; // get current HashMap from database, store it in a local variable called map
                nfcUrl = newNfcUrl; // newNfcUrl is the Url received from Nfc, it has to be stored in a separate valuebecause nfcUrl has a separate function. One is used to receive
                                    //the Nfc message, the other is used to store the Nfc messages in the buttons.

                Gson gson = new Gson();
                String hashMapString = gson.toJson(map);
                Log.d("STATE",hashMapString + " <- the hashMap string ie. DynamicButtonInformation"); //testing purposes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(customerMainScreen.this, "Couldn't retrieve ID's from Servers.", Toast.LENGTH_LONG).show(); //if something goes wrong, this Toast is displayed on phone screen
            }
        });
    }

    //NavBar
    private void setupTopNavigationView(){
        Log.d("nav", "setupTopNavigationView: ");
        BottomNavigationViewEx bottomNavigationViewEX = (BottomNavigationViewEx) findViewById(R.id.containerMenu);
        TopNavigationViewHelper.setupTopNavigationView(bottomNavigationViewEX);

        TopNavigationViewHelper.enableNavigation(customerMainScreen.this, bottomNavigationViewEX);

        Menu menus = bottomNavigationViewEX.getMenu();
        MenuItem menuItem = menus.getItem(1);
        menuItem.setChecked(true);
    }

    public void save(){ //every time a "map" is updated, it has to be saved in the database incase the customer closes the app afterwards.

        String hashMapString = gson.toJson(map);
        Log.d("STATE","in the saving function" + " dynamicButtonInformation="+dynamicButtonInformation+ " dynamicUserKeyValue="+dynamicUserKeyValue);
        Log.d("map", "onDataChange: map3" + hashMapString);
        nfcUrl = ""; //nfcUrl has to be reset to "", so that the same button cant be created with the same ID multiple times.
                     //this is just added for extra flair.
        myRef.child(userID).setValue(hashMapString); //store the updated Hashmap called map in the database
        //map = new HashMap<String, String>();

    }
}
