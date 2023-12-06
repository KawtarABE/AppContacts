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
import com.example.appcontacts.databinding.RowContactBinding;
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

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.HolderContact>{
    private Context context;
    private ArrayList<ModelContact> contactArrayList;
    private FirebaseAuth firebaseAuth;
    private RowContactBinding binding;


    public AdapterContact(Context context, ArrayList<ModelContact> contactArrayList) {
        this.context = context;
        this.contactArrayList = contactArrayList;
    }


    @NonNull
    @Override
    public AdapterContact.HolderContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowContactBinding.inflate(LayoutInflater.from(context),parent,false);

        return new HolderContact(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContact.HolderContact holder, int position) {
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

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


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


        holder.singleContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailContact.class);
                intent.putExtra("name", contactArrayList.get(holder.getAdapterPosition()).getContact_name());
                intent.putExtra("email_contact", contactArrayList.get(holder.getAdapterPosition()).getContact_email());
                intent.putExtra("number", contactArrayList.get(holder.getAdapterPosition()).getContact_number());
                intent.putExtra("id", contactArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("service", contactArrayList.get(holder.getAdapterPosition()).getService());
                intent.putExtra("img_uri",contactArrayList.get(holder.getAdapterPosition()).getImg_uri());
                intent.putExtra("favorite",contactArrayList.get(holder.getAdapterPosition()).getFavourite());
                intent.putExtra("recommended",contactArrayList.get(holder.getAdapterPosition()).getRecommended());
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                String email = currentUser.getEmail();
                intent.putExtra("email",email);
                context.startActivity(intent);
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

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("smsto:" + contact_number));
                context.startActivity(intent);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference itemRef = database.getReference("Contacts").child(id);
                itemRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context,"Delete successfuly..",Toast.LENGTH_SHORT).show();
                    }
                });
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
        CardView singleContact;
        ImageView imgUser;
        ImageView call;
        ImageView message;
        ImageView remove;
        public HolderContact(@NonNull View itemView) {
            super(itemView);
            contact =binding.contact;
            num = binding.num;
            singleContact = binding.singleContact;
            imgUser = binding.imgUser;
            call = binding.call;
            message = binding.message;
            remove = binding.remove;
        }
    }
}
