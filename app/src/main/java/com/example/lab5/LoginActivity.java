package com.example.lab5;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.lab5.databinding.ActivityLoginBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    GoogleApiClient googleApiClient;
    private static final int RC_SIGN_INT = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        binding.registrate.setOnClickListener(view ->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        binding.ingresar.setOnClickListener(view -> {
            String correo = binding.editTextTextPersonName.getText().toString();
            String contrasenia = binding.editTextTextPersonName3.getText().toString();
            Query query = userRef.orderByChild("correo").equalTo(correo);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = new User();
                    if (snapshot !=null){
                        for (DataSnapshot userSnapshot: snapshot.getChildren()){
                            user = userSnapshot.getValue(User.class);
                            user.setKey(userSnapshot.getKey());
                            Log.d("user",user.getCorreo());
                            if (user.getPasswd().equals(contrasenia)) {
                                Log.d("msg","logueo exitoso");
                                SharedPreferences sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                editor.putString("user",gson.toJson(user));
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Log.d("msg", "no encontrado");
                        Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
        FirebaseAuth auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        binding.loginGoogle.setOnClickListener(view -> {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, RC_SIGN_INT);
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_INT){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("acct", "Usuario logueado con google");
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("users");
            User user = new User();
            user.setKey(acct.getId());
            user.setNombre(acct.getGivenName());
            user.setApellido(acct.getFamilyName());
            user.setCorreo(acct.getEmail());
            databaseReference.child(user.getKey()).setValue(user);
            SharedPreferences sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            editor.putString("user",gson.toJson(user));
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

        }
    }
}