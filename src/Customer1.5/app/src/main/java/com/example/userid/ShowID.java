package com.example.userid;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ShowID extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback{
//need to override NfcAdapter.CreateNdefMessageCallback or ill have to declare ShowID abstract
    private TextView textViews;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_id);

        sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        textViews = findViewById(R.id.textView3);
        Toast.makeText(this, "Ready to send ID to Bouncer.", Toast.LENGTH_LONG).show();
        //textViews.setText(sessionId);

        //Sending stored URL to Bouncer.
        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(ShowID.this, ShowID.this);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        String message = sessionId;
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
