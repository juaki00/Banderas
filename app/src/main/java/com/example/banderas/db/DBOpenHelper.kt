package com.example.banderas.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception
import com.example.banderas.BanderaProvider

class DBOpenHelper private constructor(context: Context?) : SQLiteOpenHelper(context, BanderaContract.NOMBRE_BD, null, BanderaContract.VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        try {

            sqLiteDatabase?.execSQL(
                "CREATE TABLE IF NOT EXISTS ${BanderaContract.Companion.Entrada.NOMBRE_TABLA}"
                +"(${BanderaContract.Companion.Entrada.COLUMNA_ID} INT NOT NULL"
                +",${BanderaContract.Companion.Entrada.COLUMNA_NOMBRE} NVARCHAR(20) NOT NULL"
                +",${BanderaContract.Companion.Entrada.COLUMNA_IMAGEN} INT NOT NULL);"
            )
            //Insertar datos en la tabla
            inicializarBBDD(sqLiteDatabase)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun inicializarBBDD(sqLiteDatabase: SQLiteDatabase?) {

        val listaBanderas = BanderaProvider.banderas
        for(bandera in listaBanderas){
            sqLiteDatabase?.execSQL(
                "INSERT INTO ${BanderaContract.Companion.Entrada.NOMBRE_TABLA}(" +
                    "${BanderaContract.Companion.Entrada.COLUMNA_ID}," +
                    "${BanderaContract.Companion.Entrada.COLUMNA_NOMBRE}," +
                    "${BanderaContract.Companion.Entrada.COLUMNA_IMAGEN})" +
                    " VALUES (${bandera.id},'${bandera.nombre}',${bandera.imagen});"
            )
        }
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        sqLiteDatabase?.execSQL("DROP TABLE IF EXISTS ${BanderaContract.Companion.Entrada.NOMBRE_TABLA};")
        onCreate(sqLiteDatabase)
    }

    companion object {
        private var dbOpen: DBOpenHelper? = null
        fun getInstance(context: Context?): DBOpenHelper? {
            if (dbOpen == null) dbOpen = DBOpenHelper(context)
            return dbOpen
        }
    }

}