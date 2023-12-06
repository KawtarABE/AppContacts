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
import com.example.appcontacts.databinding.RowNotesBinding;
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

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.HolderNote> {
    private Context context;
    private ArrayList<ModelNote> notesArrayList;
    private FirebaseAuth firebaseAuth;
    private RowNotesBinding binding;


    public AdapterNote(Context context, ArrayList<ModelNote> notesArrayList) {
        this.context = context;
        this.notesArrayList = notesArrayList;
    }


    @NonNull
    @Override
    public AdapterNote.HolderNote onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowNotesBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderNote(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNote.HolderNote holder, int position) {
        ModelNote model = notesArrayList.get(position);
        String id = model.getId();
        String title = model.getTitle();
        String content = model.getContent();
        String uid = model.getUid();

        holder.title.setText(title);
        holder.content.setText(content);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference itemRef = database.getReference("Notes").child(id);
                itemRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Delete successfuly..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

        @Override
        public int getItemCount () {
            return notesArrayList.size();
        }

        public void filterList (ArrayList<ModelNote> filteredNotes) {
            notesArrayList = filteredNotes;
            notifyDataSetChanged();
        }

        class HolderNote extends RecyclerView.ViewHolder {
            TextView title;
            TextView content;
            CardView singleNote;
            ImageView remove;

            public HolderNote(@NonNull View itemView) {
                super(itemView);
                title = binding.title;
                content = binding.content;
                singleNote = binding.singleNote;
                remove = binding.remove;
            }
        }
}
