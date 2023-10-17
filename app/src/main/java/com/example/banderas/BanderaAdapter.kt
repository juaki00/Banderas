package com.example.banderas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BanderaAdapter(private val banderaLista: List<Bandera>, private val onClickListener: (Bandera)->Unit):RecyclerView.Adapter<BanderaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BanderaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BanderaViewHolder(layoutInflater.inflate(R.layout.item_bandera,parent,false))
    }

    override fun getItemCount(): Int {
        return banderaLista.size
    }

    override fun onBindViewHolder(holder: BanderaViewHolder, position: Int) {
        val item = banderaLista[position]
        holder.render(item,onClickListener)
    }
}