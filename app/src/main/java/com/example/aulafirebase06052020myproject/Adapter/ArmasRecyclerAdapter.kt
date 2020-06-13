package com.example.aulafirebase06052020myproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aulafirebase06052020myproject.Model.Armas
import com.example.aulafirebase06052020myproject.R
import kotlinx.android.synthetic.main.recycler_lista_model.view.*

class ArmasRecyclerAdapter (
    private val armas : List<Armas>
) : RecyclerView.Adapter
<ArmasRecyclerAdapter.ArmasViewHolder>() {

    class ArmasViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        val txtVwArmaNome = itemView.armaTxtVw
        val txtVwTipo = itemView.tipoTxtVw
        val txtVwPais = itemView.paisTxtVw
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmasViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recycler_lista_model,
                parent,
                false
            )
        val armaViewHolder = ArmasViewHolder(view)
        return armaViewHolder
    }

    override fun getItemCount(): Int = armas.size

    override fun onBindViewHolder(holder: ArmasViewHolder, position: Int) {
        val arma = armas[position]
        //Log.d("size","${armas.size}")

        holder.txtVwArmaNome.text = arma.nome
        holder.txtVwTipo.text = arma.tipo
        holder.txtVwPais.text = arma.origem
    }
}