package com.example.napat.glide
import io.reactivex.Observable
import retrofit2.http.GET

interface API {

    @GET("/movie")
    fun  getMovie() : Observable<MovieResponse>
}