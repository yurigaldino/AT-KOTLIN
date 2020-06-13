package com.example.aulafirebase06052020myproject.apiservice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var instance : Retrofit? = null
    private val url : String = "https://restcountries.eu/"
    private fun getInstance(): Retrofit {
        if (instance == null){
            instance = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance as Retrofit
    }

    fun getPaisesService() : PaisesService
            = getInstance().create(PaisesService::class.java)
}