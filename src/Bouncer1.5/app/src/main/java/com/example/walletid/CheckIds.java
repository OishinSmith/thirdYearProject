package com.example.walletid;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.walletid.Utils.TopNavigationViewHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class CheckIds extends AppCompatActivity {

    public String nfcUrl;
    private ImageView imgView;

    FirebaseStorage storage;

    // Storage Reference to the file
    StorageReference httpsReference;

    // Reference to real time database
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ids);

        imgView = findViewById(R.id.imageViewID);

        setupTopNavigationView();

        Intent intent = getIntent();
        //when android notices that another android is sending data, android beam.
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            //get the Ndef messages, turn them into parcelable, then turn
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            //Im assuming getRecords()[0].getPayload() gets the message 0
            // record 0 contains the MIME type, record 1 is the AAR, if present
            //textView.setText(new String(message.getRecords()[0].getPayload())); make text appear
            // store url in the form of a string from bouncer.
            nfcUrl = new String(message.getRecords()[0].getPayload());
            //I will now add an Imageview and try to display, using the sent url, the ID from firestore
        }

        // If a URL exists
        if (nfcUrl != null) {
            storage = FirebaseStorage.getInstance();
            db = FirebaseFirestore.getInstance();
            httpsReference = storage.getReferenceFromUrl(nfcUrl);

            httpsReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    // Metadata now contains the metadata for image.

                    // The metadata inside of the 'key' variable.
                    final String storageMetadataStr = storageMetadata.getCustomMetadata("key");

                    if (storageMetadataStr != null) {
                        db.collection("bouncers")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            boolean valid_id = false;
                                            // For loop goes through all documents in a specific collection.
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String s = document.getData().toString();
                                                String bouncer_uid = s.substring(5, s.length() - 1);

                                                // If metadata signature matches a bouncer's uid, then accept the ID.
                                                if (storageMetadataStr.equals(bouncer_uid)) {
                                                    Glide.with(CheckIds.this).load(nfcUrl).into(imgView);
                                                    Toast.makeText(CheckIds.this, "ID is valid.", Toast.LENGTH_LONG).show();
                                                    valid_id = true;
                                                    break;
                                                }
                                            }
                                            if (!valid_id) {
                                                Toast.makeText(CheckIds.this, "ID is invalid.", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(CheckIds.this, "ID is invalid.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(CheckIds.this, "ID is invalid.", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });
        }
    }

    private void setupTopNavigationView(){
        Log.d("nav", "setupTopNavigationView: ");
        BottomNavigationViewEx bottomNavigationViewEX = (BottomNavigationViewEx) findViewById(R.id.containerMenu);
        TopNavigationViewHelper.setupTopNavigationView(bottomNavigationViewEX);

        TopNavigationViewHelper.enableNavigation(this, bottomNavigationViewEX);

        Menu menus = bottomNavigationViewEX.getMenu();
        MenuItem menuItem = menus.getItem(0);
        menuItem.setChecked(true);

    }
}