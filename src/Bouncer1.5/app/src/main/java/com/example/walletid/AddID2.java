package com.example.walletid;

import android.Manifest;
        import android.app.ProgressDialog;
        import android.content.ContentValues;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.Build;
        import android.provider.MediaStore;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.Toast;

import com.example.walletid.Utils.TopNavigationViewHelper;
import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.OnProgressListener;
        import com.google.firebase.storage.StorageMetadata;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.UUID;

public class AddID2 extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    Button mCaptureBtn;
    ImageView mImageView;
    Uri image_uri;

    private Uri filePath;
    private String downloadUrlStr = "a";
    private String keyStr;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    FirebaseFirestore db;

    private static final String TAG = AddID2.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_id2);

        mImageView = findViewById(R.id.image_view);
        mCaptureBtn = findViewById(R.id.capture_image_btn);

        setupTopNavigationView();

        Button btnUpload = findViewById(R.id.btnUpload);
        Button nfcTransfer2 = findViewById(R.id.nfc_transfer2);

        //button click
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if system os is >= marshmallow, request runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED){
                        //permission not enabled, request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to request permissions
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        //permission already granted
                        openCamera();
                    }
                }
                else {
                    //system os < marshmallow
                    openCamera();
                }
            }
        });

        // Reference to Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Reference to Firestore (Real Time Database)
        db = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        // Reference to Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            keyStr = user.getUid();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        nfcTransfer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // downloadUrlStr is initialized as "a", so if its length is above 1, it means a URL exists
                if (downloadUrlStr.length() > 1) {
                    Intent intent = new Intent(AddID2.this, NFCActivity.class);

                    // Supply String URL to intent
                    intent.putExtra("DOWNLOAD_URL", downloadUrlStr);

                    // Switch to intent
                    startActivity(intent);
                }
            }
        });
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); //this is where the image is stored
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera();
                }
                else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //called when image was captured from camera
        if (resultCode == RESULT_OK){
            //set the image captured to our ImageView
            mImageView.setImageURI(image_uri);
        }
    }

    private void uploadImage() {
        // File path to image is stored in image_uri
        filePath = image_uri;

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString()); //this is where the image is gotten.
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddID2.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            // Task for uploaded image
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());

                            // Get Uri of Task
                            Uri downloadUrl = urlTask.getResult();

                            // Convert Uri to String. This variable gets passed to NFCActivity.
                            downloadUrlStr = String.valueOf(downloadUrl);

                            // Checking whether signed in user is a bouncer
                            db.collection("bouncers")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                // For loop goes through all documents in a specific collection.
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //Log.d(TAG, document.getId() + " => " + document.getData());

                                                    String s = document.getData().toString();
                                                    String bouncer_uid = s.substring(5, s.length()-1);

                                                    // If logged in user is a bouncer, then sign the image (ID).
                                                    if (keyStr.equals(bouncer_uid)) {

                                                        // Adding metadata
                                                        StorageMetadata key = new StorageMetadata.Builder()
                                                                .setContentType("image/jpg")
                                                                .setCustomMetadata("key", keyStr)
                                                                .build();

                                                        // Updating metadata
                                                        ref.updateMetadata(key)
                                                                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                                                    @Override
                                                                    public void onSuccess(StorageMetadata storageMetadata) {
                                                                        // Updated metadata is in storageMetadata
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception exception) {
                                                                        // Uh-oh, an error occurred!
                                                                    }
                                                                });
                                                        break;
                                                    }
                                                }
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddID2.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void setupTopNavigationView(){
        Log.d("nav", "setupTopNavigationView: ");
        BottomNavigationViewEx bottomNavigationViewEX = (BottomNavigationViewEx) findViewById(R.id.containerMenu);
        TopNavigationViewHelper.setupTopNavigationView(bottomNavigationViewEX);

        TopNavigationViewHelper.enableNavigation(AddID2.this, bottomNavigationViewEX);

        Menu menus = bottomNavigationViewEX.getMenu();
        MenuItem menuItem = menus.getItem(0);
        menuItem.setChecked(true);



    }

}