package com.example.appcontacts;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private LinearLayout loginLayout;
    private LinearLayout logupLayout;
    private TextView signUpTextView;
    private TextView signInTextView;
    //private EditText register_name;
    private EditText register_email;
    private EditText  register_password;
    private EditText confirmed_password;
    private Button button_register;
    private EditText login_email;
    private EditText login_password;
    private Button button_login;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginLayout = findViewById(R.id.section1);
        logupLayout = findViewById(R.id.section2);
        signUpTextView = findViewById(R.id.signup);

        //register_name = findViewById(R.id.name);
        register_email = findViewById(R.id.email_reg);
        register_password = findViewById(R.id.password_reg);
        confirmed_password = findViewById(R.id.confirm_password);
        button_register = findViewById(R.id.button1);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        button_login = findViewById(R.id.button_login);
        progressBar=findViewById(R.id.progress_bar);


        button_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email, password;
                email = String.valueOf(register_email.getText());
                password = String.valueOf(register_password.getText());


                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // If sign in success, display a message to the user.
                                    Toast.makeText(Login.this, "Account Created",
                                            Toast.LENGTH_SHORT).show();
                                    // Sign in success, update UI with the signed-in user's information
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Authentication Failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = login_email.getText().toString().trim();
                password = login_password.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "signInWithEmail:success");
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);
                                    Toast.makeText(Login.this, "Sign in successful",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Signin Failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the login layout
                loginLayout.setVisibility(View.GONE);

                // Show the logup layout
                logupLayout.setVisibility(View.VISIBLE);
            }
        });

        signInTextView = findViewById(R.id.login);

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the login layout
                logupLayout.setVisibility(View.GONE);

                // Show the logup layout
                loginLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}