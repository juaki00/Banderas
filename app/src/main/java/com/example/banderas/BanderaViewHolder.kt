package com.example.banderas

import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.banderas.databinding.ItemBanderaBinding

class BanderaViewHolder(view: View):ViewHolder(view), View.OnCreateContextMenuListener {

    val binding = ItemBanderaBinding.bind(view)
    private lateinit var bandera:Bandera

    fun render(item: Bandera, onClickListener:(Bandera)->Unit){
        bandera = item
        binding.tvBanderaNombre.text = item.nombre
        Glide.with(binding.ivBandera.context).load(item.imagen).fitCenter().into(binding.ivBandera)
        itemView.setOnClickListener{
            onClickListener(item)

        }
        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu!!.setHeaderTitle(bandera.nombre)
        menu.add(this.adapterPosition,0,0,"Eliminar")
        menu.add(this.adapterPosition,1,1,"Editar")
    }
}