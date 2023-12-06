package com.example.appcontacts;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appcontacts.databinding.RowRecommendedBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterRecommended extends RecyclerView.Adapter<AdapterRecommended.HolderContact>{
    private Context context;
    private ArrayList<ModelContact> contactArrayList;
    private FirebaseAuth firebaseAuth;
    private RowRecommendedBinding binding;


    public AdapterRecommended(Context context, ArrayList<ModelContact> contactArrayList) {
        this.context = context;
        this.contactArrayList = contactArrayList;
    }


    @NonNull
    @Override
    public AdapterRecommended.HolderContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowRecommendedBinding.inflate(LayoutInflater.from(context),parent,false);

        return new HolderContact(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecommended.HolderContact holder, int position) {
        ModelContact model = contactArrayList.get(position);
        String id = model.getId();
        String contact_name = model.getContact_name();
        String contact_number = model.getContact_number();
        String contact_email = model.getContact_email();
        String uid = model.getUid();
        String service = model.getService();
        String img_uri = model.getImg_uri();

        holder.contact.setText(contact_name);
        holder.num.setText(contact_number);
        holder.service.setText(service);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        StorageReference imageRef = storageRef.child("images/"+img_uri);

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.imgUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });


        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contact_number));
                context.startActivity(intent);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hashmap = new HashMap<>();
                long timestamp;
                timestamp = System.currentTimeMillis();
                String uid_user = firebaseAuth.getUid();
                if( !uid_user.equals(uid)) {
                    hashmap.put("id", "" + timestamp);
                    hashmap.put("contact_name", "" + contact_name);
                    hashmap.put("contact_number", ""+contact_number);
                    hashmap.put("contact_email", "" + contact_email);
                    hashmap.put("img_uri", "" + img_uri);
                    hashmap.put("uid", "" + uid_user);
                    hashmap.put("service", "" + service);
                    hashmap.put("favourite", 0);
                    hashmap.put("recommended", 1);


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Contacts");
                ref.child(""+timestamp).setValue(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(view.getContext(), "Contact successfuly added...", Toast.LENGTH_SHORT).show();
                    }
                }); }
                else {
                    Toast.makeText(view.getContext(), "Contact Alrady exist...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }

    public void filterList(ArrayList<ModelContact> filteredContacts) {
        contactArrayList = filteredContacts;
        notifyDataSetChanged();
    }

    class HolderContact extends RecyclerView.ViewHolder {
        TextView contact;
        TextView num;
        TextView service;
        CardView singleRecommended;
        ImageView imgUser;
        ImageView call;
        ImageView remove;
        public HolderContact(@NonNull View itemView) {
            super(itemView);
            contact =binding.contact;
            num = binding.num;
            service = binding.service;
            singleRecommended = binding.singleRecommended;
            imgUser = binding.imgUser;
            call = binding.call;
            remove = binding.remove;
        }
    }
}
