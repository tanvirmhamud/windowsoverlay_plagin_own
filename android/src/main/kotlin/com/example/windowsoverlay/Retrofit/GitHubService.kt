package com.example.windowsoverlay.Retrofit

import com.example.windowsoverlay.Model.Matchdetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path


interface GitHubService {
    @GET("id={matchid}")
    fun listRepos(@Path("matchid") matchid: String?, @Header("ab") ab : String): Call<ArrayList<Matchdetail?>?>?
}