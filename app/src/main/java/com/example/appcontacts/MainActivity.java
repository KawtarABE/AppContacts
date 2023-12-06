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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.appcontacts.databinding.ActivityMainBinding;
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

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ImageButton button_logout;
    private Button button_add;
    private ActivityMainBinding binding;
    private ArrayList<ModelContact> contactsArrayList;
    private AdapterContact adapterContact;
    private SearchView search;
    private DrawerLayout main_page;
    private ImageView menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadContacts();
        button_logout = findViewById(R.id.logout);
        button_add = findViewById(R.id.add);
        search = findViewById(R.id.search);
        main_page = findViewById(R.id.main_page);
        menu = findViewById(R.id.menu_button);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_page.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        break;

                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_favorite:
                        Intent intent_1 = new Intent(getApplicationContext(), favorite_contacts.class);
                        startActivity(intent_1);
                        finish();
                        break;
                    case R.id.nav_recommended:
                        Intent intent_2 = new Intent(getApplicationContext(), recommended_contacts.class);
                        startActivity(intent_2);
                        finish();
                        break;
                    case R.id.nav_notes:
                        Intent intent_3 = new Intent(getApplicationContext(), Notes.class);
                        startActivity(intent_3);
                        finish();
                        break;
                }

                DrawerLayout drawerLayout = findViewById(R.id.main_page);
                drawerLayout.closeDrawers();

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

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddContact.class);
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                String email = currentUser.getEmail();
                intent.putExtra("email",email);
                startActivity(intent);
                finish();
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
    }

    private void loadContacts() {
        contactsArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Contacts");
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        Query query = ref.orderByChild("uid").equalTo(currentUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactsArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ModelContact model = ds.getValue(ModelContact.class);
                    contactsArrayList.add(model);
                }

                adapterContact = new AdapterContact(MainActivity.this, contactsArrayList);
                binding.contacts.setAdapter(adapterContact);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

}