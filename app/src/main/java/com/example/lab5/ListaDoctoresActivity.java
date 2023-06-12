package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.lab5.databinding.ActivityListaDoctoresBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ListaDoctoresActivity extends AppCompatActivity {
    ActivityListaDoctoresBinding binding;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaDoctoresBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DoctorModel> options =
                new FirebaseRecyclerOptions.Builder<DoctorModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("laboratorio5"), DoctorModel.class)
                        .build();
        mainAdapter=new MainAdapter(options);
        binding.rv.setAdapter(mainAdapter);

    }
    @Override
    protected  void onStart(){
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected  void onStop(){
        super.onStop();
        mainAdapter.startListening();
    }
}