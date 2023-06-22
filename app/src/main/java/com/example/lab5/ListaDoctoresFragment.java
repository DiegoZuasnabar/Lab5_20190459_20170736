package com.example.lab5;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab5.Service.RandomUserService;
import com.example.lab5.databinding.FragmentListaDoctoresBinding;
import com.example.lab5.dto.DoctorModel;
import com.example.lab5.dto.ListarRandomUser;
import com.example.lab5.dto.RandomUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ListaDoctoresFragment extends Fragment {

    FragmentListaDoctoresBinding binding;
    FirebaseDatabase firebaseDatabase;
    MainAdapter mainAdapter;
    private List<DoctorModel> listaDoctoresCompleta = new ArrayList<>();
    String baseurl="https://randomuser.me";

    //uso del service
    RandomUserService randomUserService = new Retrofit.Builder()
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

        //AÑADIR A DOCTOR
        binding.addDoctor.setOnClickListener(view ->{
            System.out.println("SE PRESIONÓO EL BOTON1");
            randomUserService.random().enqueue(new Callback<ListarRandomUser>(){
                @Override
                public void onResponse(Call<ListarRandomUser> call, Response<ListarRandomUser> response){
                    System.out.println("CONSULTANDO 2");
                    if (response.isSuccessful()){
                        RandomUser randomUser = response.body().getResults().get(0);
                        DoctorModel doctor = new DoctorModel();
                        doctor.setNombre(randomUser.getName().getFirst());
                        doctor.setApellido(randomUser.getName().getLast());
                        doctor.setNacionalidad(randomUser.getNat());
                        doctor.setEmail(randomUser.getEmail());
                        doctor.setEdad(45);
                        doctor.setImagen(randomUser.getPicture().getLarge());
                        doctor.setGenero(randomUser.getGender());
                        doctor.setPais(randomUser.getLocation().getCountry());
                        doctor.setEstado(randomUser.getLocation().getState());
                        doctor.setCiudad(randomUser.getLocation().getCity());
                        doctor.setTelefono(randomUser.getPhone());
                        doctor.setUsuario(randomUser.getLogin().getUsername());
                        doctor.setId(listaDoctoresCompleta.size()+1);
                        System.out.println("DOCTOR RESULTADO" + doctor.getGenero());
                        databaseReference.child("laboratorio5").child(doctor.getUsuario()).setValue(doctor)
                                .addOnSuccessListener(aVoid ->{
                                    Log.d("a", "se añadio");
                                })
                                .addOnFailureListener(e->{
                                    Log.d("msg",e.getMessage());
                                });
                    }
                }
                @Override
                public void onFailure(Call<ListarRandomUser> call, Throwable t) {

                }
            });


        });


        return binding.getRoot();
    }
}