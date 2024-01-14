package com.example.banderas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class BanderaAdapter(private var banderaLista: List<Bandera>,
     private val onClickListener: (Bandera)->Unit): RecyclerView.Adapter<BanderaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BanderaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BanderaViewHolder(layoutInflater.inflate(R.layout.item_bandera,parent,false))
    }

    fun updateList(newList:List<Bandera>){
        val banderaDiff = BanderaDiffUtil(banderaLista.toMutableList(), newList.toMutableList())
        val result = DiffUtil.calculateDiff(banderaDiff)
        banderaLista = newList
        result.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return banderaLista.size
    }

    override fun onBindViewHolder(holder: BanderaViewHolder, position: Int) {
        val item = banderaLista[position]
        holder.render(item,onClickListener)
    }
}