package com.example.appcontacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

public class AddContact extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button button_add;
    private EditText contact_name;
    private EditText contact_number;
    private EditText contact_email;
    private EditText contact_service;
    private ImageView contact_profil;
    private Button back;
    private Uri img_path = Uri.parse("");
    private TextView subtitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        mAuth = FirebaseAuth.getInstance();
        button_add = findViewById(R.id.button1);

        contact_name = findViewById(R.id.name);
        contact_number = findViewById(R.id.numero);
        contact_email = findViewById(R.id.email);
        contact_profil = findViewById(R.id.img_user);
        contact_service = findViewById(R.id.service);
        subtitle = findViewById(R.id.subtitle);

        back = findViewById(R.id.retour);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            subtitle.setText(bundle.getString("email"));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });

        contact_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null) {
            img_path = data.getData();
            getImageInImageView();
        }

    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),img_path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        contact_profil.setImageBitmap(bitmap);

    }

    private void addContact() {
        long timestamp;
        String name, number, email, uri, service;
        timestamp = System.currentTimeMillis();
        name = String.valueOf(contact_name.getText());
        number = String.valueOf(contact_number.getText());
        email = String.valueOf(contact_email.getText());
        service = String.valueOf(contact_service.getText());
        uri = UUID.randomUUID().toString();


        FirebaseStorage.getInstance().getReference("images/"+ uri).putFile(img_path).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                }
            }
        });



        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("id", ""+timestamp);
        hashmap.put("contact_name",""+name);
        hashmap.put("contact_number",""+number);
        hashmap.put("contact_email",""+email);
        hashmap.put("img_uri",""+ uri);
        hashmap.put("uid",""+mAuth.getUid());
        hashmap.put("service",""+service);
        hashmap.put("favourite",0);
        hashmap.put("recommended",0);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Contacts");
        ref.child(""+timestamp).setValue(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddContact.this,"Contact successfuly added...", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddContact.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}