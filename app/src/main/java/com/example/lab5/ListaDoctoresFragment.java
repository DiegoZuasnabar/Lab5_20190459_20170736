package com.example.lab5;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab5.Service.RandomUserService;
import com.example.lab5.databinding.FragmentListaDoctoresBinding;
import com.example.lab5.dto.DoctorModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListaDoctoresFragment extends Fragment {

    FragmentListaDoctoresBinding binding;
    FirebaseDatabase firebaseDatabase;
    MainAdapter mainAdapter;
    private List<DoctorModel> listaDoctoresCompleta = new ArrayList<>();
    String baseurl="https://randomuser.me";

    //uso del service
    RandomUserService doctorService = new Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RandomUserService.class);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        binding = FragmentListaDoctoresBinding.inflate(inflater, container, false);
        mainAdapter = new MainAdapter();
        mainAdapter.setContext(getContext());
        mainAdapter.setListaDoctores(new ArrayList<>());
        NavController navController = NavHostFragment.findNavController(ListaDoctoresFragment.this);
        mainAdapter.setNavController(navController);
        databaseReference.child("laboratorio5").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaDoctoresCompleta.clear();
                for (DataSnapshot children : snapshot.getChildren()) {
                    DoctorModel doctormodel = children.getValue(DoctorModel.class);
                    listaDoctoresCompleta.add(doctormodel);
                }
                mainAdapter.setListaDoctores(listaDoctoresCompleta);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.rv.setAdapter(mainAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }
}