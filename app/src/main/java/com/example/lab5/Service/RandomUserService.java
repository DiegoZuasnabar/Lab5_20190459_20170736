package com.example.lab5.Service;

import com.example.lab5.dto.ListarRandomUser;
import com.example.lab5.dto.RandomUser;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RandomUserService {
    @GET("/api/")
    Call<ListarRandomUser> random();
}
