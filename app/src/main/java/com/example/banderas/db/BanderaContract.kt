package com.example.banderas.db

import android.provider.BaseColumns

class BanderaContract {
    companion object{
        val NOMBRE_BD = "banderas"
        val VERSION = 1
        class Entrada: BaseColumns{
            companion object{
                val NOMBRE_TABLA = "bandera"
                val COLUMNA_ID = "id"
                val COLUMNA_NOMBRE = "nombre"
                val COLUMNA_IMAGEN = "imagen"
            }
        }
    }
}