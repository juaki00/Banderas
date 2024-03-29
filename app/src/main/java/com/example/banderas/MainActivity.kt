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
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
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
    private lateinit var listaBanderas:List<Bandera>
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: BanderaAdapter
    private lateinit var banderaDAO: BanderaDAO
    private var nombre:String="Sin nombre"
    private var indice:Int=0
    private var textoHint:String="Comunidad"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        banderaDAO = BanderaDAO()

        listaBanderas = banderaDAO.cargarLista(this)
        layoutManager=LinearLayoutManager(this)
        binding.rvBanderas.layoutManager = layoutManager
        adapter=BanderaAdapter(listaBanderas){ bandera ->
            onItemSelected(bandera)
        }
        binding.rvBanderas.adapter=adapter


        intentLaunch = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
                result: ActivityResult ->
            if(result.resultCode == RESULT_OK){
                nombre = result.data?.extras?.getString("nombre").toString()
                indice = result.data?.extras?.getInt("id")!!
                listaBanderas[indice].nombre = nombre
                adapter = BanderaAdapter(listaBanderas){
                        bandera ->  onItemSelected(bandera)
                }
//                adapter.notifyItemChanged(indice)
                adapter.updateList(listaBanderas)
                banderaDAO.actualizarBandera(this,listaBanderas[indice])
                binding.rvBanderas.adapter = adapter
                textoHint = result.data?.extras?.getString("textoEditarNombre").toString()

            }
        }

        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finish()
            }
        })
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
                val nuevaLista:List<Bandera> = listaBanderas.minus(listaBanderas)
                banderaDAO.borrarTodas(this)
                adapter.updateList(nuevaLista)
                binding.rvBanderas.adapter = adapter
//                binding.rvBanderas.adapter =BanderaAdapter(listaBanderas){ bandera ->
//                    onItemSelected(bandera)
//                }
                true
            }

            R.id.recargar -> {
                listaBanderas.minus(listaBanderas)
                val nuevaLista:List<Bandera> = listaBanderas.plus(BanderaProvider.banderas.toSet())
                banderaDAO.recargarBanderas(this, BanderaProvider.banderas)
                adapter.updateList(nuevaLista)
                binding.rvBanderas.adapter = adapter
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val banderaAfectada: Bandera = listaBanderas[item.groupId]
        when(item.itemId){
            //Opcion Eliminar
            0-> {
                val alert =
                    AlertDialog.Builder(this).setTitle("Eliminar ${banderaAfectada.nombre}")
                        .setMessage("¿Estas seguro de que quiere eliminar ${banderaAfectada.nombre}?")
                        .setNeutralButton("Cerrar",null)
                        .setPositiveButton("Aceptar"){_,_ ->
                            display("Se ha eliminado ${banderaAfectada.nombre}")
                            val nuevaLista:List<Bandera> = ArrayList<Bandera>(listaBanderas.minus(banderaAfectada))
                            adapter.updateList(nuevaLista)
                            binding.rvBanderas.adapter = adapter
//                            binding.rvBanderas.adapter = BanderaAdapter(listaBanderas){
//                                    bandera ->  onItemSelected(bandera)
//                            }
                            banderaDAO.eliminarBandera(this,banderaAfectada)
                        }.create()
                alert.show()
            }
            //Opcion Editar
            1-> {
                val intent = Intent(this, EditarBanderaActivity::class.java)
                intent.putExtra("imagen",banderaAfectada.imagen)
                intent.putExtra("indice",item.groupId)
                intent.putExtra("textoParaModificar",banderaAfectada.nombre)
                intentLaunch.launch(intent)
            }

            else -> return super.onContextItemSelected(item)
        }
        return true
    }


    private fun display(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

    }
}