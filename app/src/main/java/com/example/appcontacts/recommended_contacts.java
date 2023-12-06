package com.example.appcontacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.appcontacts.databinding.ActivityRecommendedContactsBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class recommended_contacts extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DrawerLayout recommended_page;
    private ImageView menu;
    private ActivityRecommendedContactsBinding binding;
    private ArrayList<ModelContact> contactsArrayList;
    private AdapterRecommended adapterContact;
    private ImageButton button_logout;
    private SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_contacts);

        binding = ActivityRecommendedContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadrecommendedContacts();
        recommended_page = findViewById(R.id.recommended_page);
        menu = findViewById(R.id.menu_button);
        search = findViewById(R.id.search);
        button_logout = findViewById(R.id.logout);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recommended_page.openDrawer(GravityCompat.START);
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<ModelContact> filteredContacts = new ArrayList<>();
                for (ModelContact contact : contactsArrayList) {
                    if (contact.getContact_name().toLowerCase().contains(newText.toLowerCase())) {
                        filteredContacts.add(contact);
                    }
                }

                adapterContact.filterList(filteredContacts);
                return true;
            }
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        NavigationView navigationView = findViewById(R.id.menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent intent_1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent_1);
                        finish();
                        break;

                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_favorite:
                        Intent intent_2 = new Intent(getApplicationContext(), favorite_contacts.class);
                        startActivity(intent_2);
                        finish();
                        break;

                    case R.id.nav_recommended:
                        break;

                    case R.id.nav_notes:
                        Intent intent_3 = new Intent(getApplicationContext(), Notes.class);
                        startActivity(intent_3);
                        finish();
                        break;
                }

                DrawerLayout drawerLayout = findViewById(R.id.recommended_page);
                drawerLayout.closeDrawers();

                return true;
            }
        });
    }

    private void checkUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null) {
            startActivity(new Intent(this, Login.class));
            finish();
        }
        else {
            String email = currentUser.getEmail();
            binding.subtitle.setText(email);
        }
    }

    private void loadrecommendedContacts() {
        contactsArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Contacts");
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        Query query = ref.orderByChild("contact_number");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactsArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ModelContact model = ds.getValue(ModelContact.class);
                    if(model.getRecommended() == 1 && !contactsWithPhoneNumberExist(model.getContact_number())) {
                        contactsArrayList.add(model);
                    }
                }


                adapterContact = new AdapterRecommended(recommended_contacts.this, contactsArrayList);
                binding.recommendedContacts.setAdapter(adapterContact);
            }

            private boolean contactsWithPhoneNumberExist(String phoneNumber) {
                for (ModelContact contact : contactsArrayList) {
                    if (contact.getContact_number().equals(phoneNumber)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}