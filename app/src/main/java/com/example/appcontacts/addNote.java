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

public class addNote extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button button_add;
    private EditText title;
    private EditText content;
    private Button back;
    private TextView subtitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mAuth = FirebaseAuth.getInstance();
        button_add = findViewById(R.id.button1);

        title = findViewById(R.id.name);
        content = findViewById(R.id.numero);

        subtitle = findViewById(R.id.subtitle);

        back = findViewById(R.id.retour);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            subtitle.setText(bundle.getString("email"));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notes.class);
                startActivity(intent);
                finish();
            }
        });


        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

    }

    private void addNote() {
        long timestamp;
        String title_1, content_1;
        timestamp = System.currentTimeMillis();
        title_1 = String.valueOf(title.getText());
        content_1 = String.valueOf(content.getText());



        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("id", ""+timestamp);
        hashmap.put("title",""+title_1);
        hashmap.put("content",""+content_1);
        hashmap.put("uid",""+mAuth.getUid());


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notes");
        ref.child(""+timestamp).setValue(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(addNote.this,"Note successfuly added...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addNote.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}