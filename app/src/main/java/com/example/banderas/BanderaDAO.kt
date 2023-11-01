package com.example.banderas

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.banderas.db.BanderaContract
import com.example.banderas.db.DBOpenHelper

class BanderaDAO {
    fun cargarLista (context: Context?): MutableList<Bandera> {
        lateinit var res: MutableList<Bandera>
        lateinit var c: Cursor
        try{
            val db = DBOpenHelper.getInstance(context)!!.readableDatabase
//             val sql = "SELECT * FROM bandera;"
//             c = db.rawQuery(sql, null)
            val columnas = arrayOf(
                BanderaContract.Companion.Entrada.COLUMNA_ID,
                BanderaContract.Companion.Entrada.COLUMNA_NOMBRE,
                BanderaContract.Companion.Entrada.COLUMNA_IMAGEN)
            c = db.query(BanderaContract.Companion.Entrada.NOMBRE_TABLA,
                columnas,null,null,null,null,null)
            res = mutableListOf()
            // Leer resultados del cursor e insertarlos en la lista
            while (c.moveToNext()) {
                val bandera = Bandera(c.getInt(0),c.getString(1),
                    c.getInt(2))

                res.add(bandera)

            }
        } finally {
            c.close()
        }
        return res
    }

    fun eliminarBandera(context: Context?, bandera: Bandera){
        val db = DBOpenHelper.getInstance(context)!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(BanderaContract.Companion.Entrada.COLUMNA_ID, bandera.id)
        contentValues.put(BanderaContract.Companion.Entrada.COLUMNA_NOMBRE,bandera.nombre)
        contentValues.put(BanderaContract.Companion.Entrada.COLUMNA_IMAGEN,bandera.imagen)
        db.delete(BanderaContract.Companion.Entrada.NOMBRE_TABLA, "id=?", arrayOf(bandera.id.toString()))

        db.close()
    }

    fun actualizarBandera(context: Context?, bandera: Bandera) {
        val db = DBOpenHelper.getInstance(context)!!.writableDatabase
        /*db.execSQL(
            "UPDATE bandera "
                    + "SET nombre='${bandera.nombre}' " +
                    "SET descripcion='${bandera.descripcion}'" +
                    "SET imagen='${bandera.imagen}'" +
                    "WHERE id=${bandera.id};"
        )
        */
        val values = ContentValues()
        values.put(BanderaContract.Companion.Entrada.COLUMNA_ID,bandera.id)
        values.put(BanderaContract.Companion.Entrada.COLUMNA_NOMBRE,bandera.nombre)
        values.put(BanderaContract.Companion.Entrada.COLUMNA_IMAGEN,bandera.imagen)
        db.update(BanderaContract.Companion.Entrada.NOMBRE_TABLA,values,"id=?",arrayOf(bandera.id.toString()))
        db.close()
    }

    fun borrarTodas(context: Context?) {
        val db = DBOpenHelper.getInstance(context)!!.writableDatabase

        db.delete(BanderaContract.Companion.Entrada.NOMBRE_TABLA, null, null)
        db.close()
    }

    fun recargarBanderas(context: Context?) {
        borrarTodas(context)
        val db = DBOpenHelper.getInstance(context)!!.writableDatabase

        val listaBanderas = BanderaProvider.banderasCopia
        for(bandera in listaBanderas){
            db.execSQL(
                "INSERT INTO ${BanderaContract.Companion.Entrada.NOMBRE_TABLA}(" +
                        "${BanderaContract.Companion.Entrada.COLUMNA_ID}," +
                        "${BanderaContract.Companion.Entrada.COLUMNA_NOMBRE}," +
                        "${BanderaContract.Companion.Entrada.COLUMNA_IMAGEN})" +
                        " VALUES (${bandera.id},'${bandera.nombre}',${bandera.imagen});"
            )
        }
        db.close()
    }
}