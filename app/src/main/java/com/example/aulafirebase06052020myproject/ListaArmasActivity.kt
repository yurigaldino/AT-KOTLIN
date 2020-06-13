package com.example.aulafirebase06052020myproject

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.aulafirebase06052020myproject.Model.Armas
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_lista_armas.*

class ListaArmasActivity : AppCompatActivity() {

    private lateinit var listaViewModel: ListaViewModel
    private lateinit var firebaseFirestore: FirebaseFirestore
    //como inicializar?
    lateinit var armas: MutableList<Armas>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_armas)

         listaViewModel = ViewModelProviders.of(this).get(ListaViewModel::class.java)

        listaViewModel.setupRecyclerView(rcyVwListaArmas,this)
//aqui
        armas = mutableListOf<Armas>()

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                DeleteListTask(viewHolder, rcyVwListaArmas, listaViewModel.lista_armas).execute(listaViewModel.lista_armas[viewHolder.adapterPosition])
            }
        })
        itemTouchHelper.attachToRecyclerView(rcyVwListaArmas)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.atualizar_test, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item3 -> {
                Toast.makeText(this, "Câmera ainda em implementação.", Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }

    inner class DeleteListTask (var view: RecyclerView.ViewHolder, var recyclerView: RecyclerView, var MutableList: MutableList<Armas>): AsyncTask<
            Armas,
            Unit,
            Unit>() {

        override fun onPreExecute() {
            super.onPreExecute()
            Toast.makeText(
                applicationContext,
                "Atualizando...",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun doInBackground(vararg params: Armas?): Unit {
            firebaseFirestore = FirebaseFirestore.getInstance()

            val collection = firebaseFirestore.collection("armas")

            //não sei se estou apagando corretamente
            val document = collection.document(params[0]!!.nome!!)

            val task = document.delete()

            task.addOnCompleteListener {
                Toast.makeText(this@ListaArmasActivity, "Tarefa realizada sem erros.", Toast.LENGTH_SHORT).show()
            }
            task.addOnFailureListener {
                Toast.makeText(this@ListaArmasActivity, "Erro em realizar tarefa.", Toast.LENGTH_LONG).show()
            }
            task.addOnSuccessListener {
                Toast.makeText(this@ListaArmasActivity, "Arma deletada com SUCESSO.", Toast.LENGTH_LONG).show()
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            val position = view.adapterPosition
            MutableList!!.removeAt(position)
            recyclerView.adapter!!.notifyItemRemoved(position)
        }
    }

}