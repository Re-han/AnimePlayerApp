package com.example.seekhotask

import android.telecom.Call.Details
import com.example.seekhotask.dto.AnimeDTO
import com.example.seekhotask.dto.Data
import com.example.seekhotask.dto.Detail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {
    @GET("top/anime")
    fun getTopAnimeList(): Call<AnimeDTO>?

    @GET("anime/{anime_id}")
    fun getAnimeDetails(@Path("anime_id") id: Int): Call<Detail>?
}