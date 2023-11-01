package com.example.banderas

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banderas.databinding.ActivityMainBinding
import com.example.banderas.db.DBOpenHelper
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var intentLaunch:ActivityResultLauncher<Intent>
    private lateinit var listaBanderas:MutableList<Bandera>
    private lateinit var adapter: BanderaAdapter
    private var banderaDAO = BanderaDAO()
    private var nombre:String="Sin nombre"
    private var indice:Int=0
    private var textoHint:String="Comunidad"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaBanderas = banderaDAO.cargarLista(this)
        binding.rvBanderas.layoutManager=LinearLayoutManager(this)
        binding.rvBanderas.adapter=BanderaAdapter(listaBanderas){ bandera ->
            onItemSelected(bandera)
        }


        intentLaunch = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
                result: ActivityResult ->
            if(result.resultCode == RESULT_OK){
                nombre = result.data?.extras?.getString("nombre").toString()
                indice = result.data?.extras?.getInt("indice")!!
                listaBanderas[indice].nombre = nombre
                adapter = BanderaAdapter(listaBanderas){
                        bandera ->  onItemSelected(bandera)
                }
                adapter.notifyItemChanged(indice)
                banderaDAO.actualizarBandera(this,listaBanderas[indice])
                binding.rvBanderas.adapter = adapter
                textoHint = result.data?.extras?.getString("textoEditarNombre").toString()

            }
        }


    }
    private fun onItemSelected(bandera: Bandera){
        Toast.makeText(this,"Yo soy de ${bandera.nombre}",Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.limpiar -> {
                listaBanderas.clear()
                banderaDAO.borrarTodas(this)
                binding.rvBanderas.adapter?.notifyDataSetChanged()
                true
            }

            R.id.recargar -> {
                listaBanderas.clear()
                binding.rvBanderas.adapter?.notifyDataSetChanged()
                listaBanderas.addAll(BanderaProvider.banderasCopia)
                banderaDAO.recargarBanderas(this)
                binding.rvBanderas.adapter?.notifyItemRangeInserted(0,BanderaProvider.banderas.size)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val banderaAfectada: Bandera = BanderaProvider.banderas[item.groupId]
        when(item.itemId){
            //Opcion Eliminar
            0-> {
                val alert =
                    AlertDialog.Builder(this).setTitle("Eliminar ${banderaAfectada.nombre}")
                        .setMessage("¿Estas seguro de que quiere eliminar ${banderaAfectada.nombre}?")
                        .setNeutralButton("Cerrar",null)
                        .setPositiveButton("Aceptar"){_,_ ->
                            display("Se ha eliminado ${banderaAfectada.nombre}")
                            listaBanderas.removeAt(item.groupId)
                            binding.rvBanderas.adapter?.notifyItemRemoved(item.groupId)
                            binding.rvBanderas.adapter?.notifyItemRangeChanged(item.groupId, BanderaProvider.banderas.size)
                            binding.rvBanderas.adapter = BanderaAdapter(BanderaProvider.banderas){
                                    bandera ->  onItemSelected(bandera)
                            }
                            banderaDAO.eliminarBandera(this,banderaAfectada)
                        }.create()
                alert.show()
            }
            //Opcion Editar
            1-> {
                val intent = Intent(this, EditarBanderaActivity::class.java)
                display("id1 ${item.groupId}")//////////////////////////
                intent.putExtra("imagen",banderaAfectada.imagen)
                intent.putExtra("indice",item.groupId)
                intent.putExtra("textoParaModificar",banderaAfectada.nombre)
                intentLaunch.launch(intent)
            }

            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val indice = data?.getIntExtra("indiceback",0)
        Toast.makeText(this,"indice3 "+indice,Toast.LENGTH_SHORT).show()
        // Recupera el string del Intent devuelto
        if(indice!!>=0) {
            BanderaProvider.banderas[indice!!] = Bandera(
                data.getIntExtra("id", 0),
                data.getStringExtra("nombre")!!,
                BanderaProvider.banderas[indice].imagen
            )
            binding.rvBanderas.adapter!!.notifyItemChanged(indice)
        }
    }

    private fun display(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

    }
}