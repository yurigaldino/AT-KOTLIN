package com.example.aulafirebase06052020myproject.apiservice

import com.example.aulafirebase06052020myproject.Model.Pais
import retrofit2.Call
import retrofit2.http.GET

interface PaisesService {

    @GET("rest/v2/all?fields=name")
    fun all() : Call<List<Pais>>

//    // n sei link para pegar apenas um kkk
//    @GET("api/livros/{id}")
//    fun show(@Path("id") id: Int): Call<Livro>
}