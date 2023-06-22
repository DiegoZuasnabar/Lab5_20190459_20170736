package com.example.lab5;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab5.dto.DoctorModel;

import java.util.List;

public class MainAdapter  extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<DoctorModel> listaDoctores;
    private Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoctorModel doctor = listaDoctores.get(position);
        System.out.println("POSICIIONNNNN" + position);
        holder.doctor = doctor;
        View view = holder.itemView.findViewById(R.id.rl2);
        view.setBackgroundColor(Color.GRAY);
        TextView nametext = holder.itemView.findViewById(R.id.nametext);
        nametext.setText("Dr. "+ doctor.getNombre());
        TextView ciudad = holder.itemView.findViewById(R.id.ciudad);
        ciudad.setText(doctor.getPais()+ " - "+doctor.getEstado()+" - "+doctor.getCiudad());
        Log.d("msg", "DOCTOR: "+doctor.getCiudad());
        ImageView img1 = holder.itemView.findViewById(R.id.img1);
        Glide.with(context).load(doctor.getImagen()).into(img1);
        //boton para la info
        Button button2 = holder.itemView.findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            //Bundle para enviar datos entre fragmentos
            Bundle bundle = new Bundle();
            String id=String.valueOf(doctor.getId());
            bundle.putString("id", id);
            navController.navigate(R.id.action_listaDoctoresFragment_to_informacionDoctorFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return listaDoctores.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        DoctorModel doctor;
        public ViewHolder(@NonNull View itemView){

            super(itemView);
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<DoctorModel> getListaDoctores() {
        return listaDoctores;
    }

    public void setListaDoctores(List<DoctorModel> listaDoctores) {
        this.listaDoctores = listaDoctores;
    }

    //nav controller
    private NavController navController;

    public NavController getNavController() {
        return navController;
    }

    public void setNavController(NavController navController) {
        this.navController = navController;
    }
}
