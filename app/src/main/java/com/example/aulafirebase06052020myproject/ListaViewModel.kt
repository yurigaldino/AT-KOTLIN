package com.example.aulafirebase06052020myproject

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aulafirebase06052020myproject.Adapter.ArmasRecyclerAdapter
import com.example.aulafirebase06052020myproject.Model.Armas
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class ListaViewModel : ViewModel() {

    var lista_armas = mutableListOf<Armas>()

    fun setupRecyclerView(
        //field: String, valor: Any,
        recyclerView: RecyclerView, context: Context
    ){
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val collection = firebaseFirestore.collection("armas")

        collection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null){
                Log.e("Firestore", firebaseFirestoreException.message)
            } else {
                if (querySnapshot != null){
                    lista_armas = querySnapshot.toObjects(Armas::class.java)


//                    firebaseFirestore.collection("armas")
//                        .document("documento").get()
//                        .addOnSuccessListener {
//                            (it != null)
//                            var arma = it.toObject(Armas::class.java)
//                        }

                    recyclerView.adapter = ArmasRecyclerAdapter(lista_armas)
                    recyclerView.layoutManager = LinearLayoutManager(context)


                    querySnapshot.documentChanges.forEach {
                        val verify = it.type == DocumentChange.Type.REMOVED

                    }

                }
            }
        }
    }

}