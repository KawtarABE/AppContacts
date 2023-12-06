package com.example.appcontacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailContact extends AppCompatActivity {

    private EditText contact_name;
    private EditText contact_email;

    private EditText contact_number;
    private EditText service;
    private ImageView no_favourite;
    private ImageView favourite;
    private ImageView no_recommended;
    private ImageView recommended;
    private ImageView imgUser;
    private DatabaseReference mDatabase;
    private Button button_update;
    private Button button_back;
    private TextView subtitle;
    private ImageView share;
    private int favorite;
    private int recommended_int;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        String img_uri;

        contact_name = findViewById(R.id.contact_name);
        contact_email = findViewById(R.id.contact_email);
        contact_number = findViewById(R.id.contact_number);
        service = findViewById(R.id.contact_service);
        button_update = findViewById(R.id.button_update);
        button_back = findViewById(R.id.button_return);
        no_favourite = findViewById(R.id.no_favourite);
        favourite = findViewById(R.id.favourite);
        no_recommended = findViewById(R.id.no_recommended);
        recommended = findViewById(R.id.recommended);
        imgUser = findViewById(R.id.imgUser);
        subtitle = findViewById(R.id.subtitle);
        share = findViewById(R.id.share);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            contact_name.setText(bundle.getString("name"));
            contact_email.setText(bundle.getString("email_contact"));
            contact_number.setText(bundle.getString("number"));
            service.setText(bundle.getString("service"));
            img_uri = bundle.getString("img_uri");
            subtitle.setText(bundle.getString("email"));
            favorite = bundle.getInt("favorite");
            recommended_int = bundle.getInt("recommended");

            if (favorite==1) {
                no_favourite.setVisibility(View.GONE);
                favourite.setVisibility(View.VISIBLE);
            }
            else{
                favourite.setVisibility(View.GONE);
                no_favourite.setVisibility(View.VISIBLE);
            }

            if(recommended_int==1) {
                recommended.setVisibility(View.VISIBLE);
                no_recommended.setVisibility(View.GONE);
            }
            else {
                no_recommended.setVisibility(View.VISIBLE);
                recommended.setVisibility(View.GONE);
            }

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images/"+img_uri);

            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // The download URL was successfully retrieved
                    String downloadUrl = uri.toString();

                    // Load the image with Glide
                    Glide.with(DetailContact.this)
                            .load(downloadUrl)
                            .placeholder(R.drawable.user_profil)
                            .into(imgUser);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Failed to retrieve the download URL
                    // Handle the error
                }
            });


            button_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the values from the EditText fields
                    String name = contact_name.getText().toString();
                    String email = contact_email.getText().toString();
                    String number = contact_number.getText().toString();
                    String serv = service.getText().toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Contacts");
                    DatabaseReference contactRef = ref.child(bundle.getString("id"));

                    // Update the contact data in Firebase Realtime Database
                    contactRef.child("contact_name").setValue(name);
                    contactRef.child("contact_email").setValue(email);
                    contactRef.child("contact_number").setValue(number);
                    contactRef.child("service").setValue(serv);

                    // Show a success message to the user
                    Toast.makeText(DetailContact.this, "Contact updated successfully", Toast.LENGTH_SHORT).show();

                }
            });


            button_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            no_favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Contacts");
                    DatabaseReference contactRef = ref.child(bundle.getString("id"));
                    contactRef.child("favourite").setValue(1);
                    // Hide the login layout
                    no_favourite.setVisibility(View.GONE);

                    // Show the logup layout
                    favourite.setVisibility(View.VISIBLE);

                }
            });

            no_recommended.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Contacts");
                    DatabaseReference contactRef = ref.child(bundle.getString("id"));
                    contactRef.child("recommended").setValue(1);
                    // Hide the login layout
                    no_recommended.setVisibility(View.GONE);

                    // Show the logup layout
                    recommended.setVisibility(View.VISIBLE);

                }
            });

            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Contacts");
                    DatabaseReference contactRef = ref.child(bundle.getString("id"));
                    contactRef.child("favourite").setValue(0);

                    favourite.setVisibility(View.GONE);


                    no_favourite.setVisibility(View.VISIBLE);
                }
            });

            recommended.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Contacts");
                    DatabaseReference contactRef = ref.child(bundle.getString("id"));
                    contactRef.child("recommended").setValue(0);

                    recommended.setVisibility(View.GONE);


                    no_recommended.setVisibility(View.VISIBLE);
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = getIntent().getExtras();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String contact = "Name: " + bundle.getString("name") + "\nPhone: " + bundle.getString("number");
                    intent.putExtra(Intent.EXTRA_TEXT, contact);
                    startActivity(Intent.createChooser(intent, "Share contact via"));
                }
            });
        }
    }
}