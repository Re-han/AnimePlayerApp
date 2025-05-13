package com.example.seekhotask

import com.example.seekhotask.Seekho.Companion.retrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    var apiInterface: ApiInterface? = retrofit?.create(ApiInterface::class.java)
}