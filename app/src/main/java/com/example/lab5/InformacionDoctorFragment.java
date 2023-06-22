package com.example.lab5;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lab5.databinding.FragmentInformacionDoctorBinding;
import com.example.lab5.dto.DoctorModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InformacionDoctorFragment extends Fragment {
    FragmentInformacionDoctorBinding binding;
    FirebaseDatabase firebaseDatabase;

    private String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInformacionDoctorBinding.inflate(inflater, container, false);
        id = getArguments().getString("id");
        System.out.println("usuariooooooooo"+ id);
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        NavController navController = NavHostFragment.findNavController(this);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Gson gson = new Gson();
        //User user = gson.fromJson(sharedPreferences.getString("user",""), User.class);


        databaseReference.child("laboratorio5").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue(DoctorModel.class)+ "AAAAAAAAAAAAA");
                DoctorModel doctor = snapshot.getValue(DoctorModel.class);
                Glide.with(getContext()).load(doctor.getImagen()).into(binding.img1);
                //textViewnombre,email,costo, usuario, ciudad2, edad, telefono, nacionalidad
                binding.textViewNombre.setText("Dr. "+doctor.getNombre());
                binding.email.setText(doctor.getEmail());
                binding.genero.setText(doctor.getGenero());
                //costo edad x3
                String edad=String.valueOf(doctor.getEdad());
                int e= 3*Integer.parseInt(edad);
                binding.edad.setText(edad);
                binding.costo.setText("S/ "+ e);
                binding.usuario.setText(doctor.getUsuario());
                binding.telefono.setText(doctor.getTelefono());
                binding.nacionalidad.setText(doctor.getNacionalidad());
                binding.ciudad2.setText(doctor.getPais() + " - "+
                        doctor.getEstado() + " - "+ doctor.getCiudad());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }
}