package com.example.banderas

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banderas.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable
import java.security.Provider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var intentLaunch:ActivityResultLauncher<Intent>
    private lateinit var nombre:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvBanderas.layoutManager=LinearLayoutManager(this)
        binding.rvBanderas.adapter=BanderaAdapter(BanderaProvider.banderas){ fruta ->
            onItemSelected(fruta)
        }

        binding.rvBanderas.setHasFixedSize(true)


        intentLaunch = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
                result: ActivityResult ->
            if(result.resultCode== RESULT_OK){
                nombre = result.data?.extras?.getString("nombre").toString()
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
        when {
            item.itemId == R.id.recargar -> recargar()
            item.itemId == R.id.limpiar -> limpiar()
            else -> super.onOptionsItemSelected(item)
        }

        return true
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        var banderaAfectada: Bandera = BanderaProvider.banderas[item.groupId]
        when(item.itemId){
            0-> {
                val alert =
                    androidx.appcompat.app.AlertDialog.Builder(this).setTitle("Eliminar ${banderaAfectada.nombre}")
                        .setMessage("¿Estas seguro de que quiere eliminar ${banderaAfectada.nombre}?")
                        .setNeutralButton("Cerrar",null)
                        .setPositiveButton("Aceptar"){_,_ ->
                            display("Se ha eliminado ${banderaAfectada.nombre}")
                            BanderaProvider.banderas.removeAt(item.groupId)
                            binding.rvBanderas.adapter!!.notifyItemRemoved(item.groupId)
                            binding.rvBanderas.adapter = BanderaAdapter(BanderaProvider.banderas){
                                    bandera ->  onItemSelected(bandera)
                            }
                        }.create()
                alert.show()
            }
            1-> {
                val intent = Intent(this, EditarBanderaActivity::class.java)
                intent.putExtra("imagen",banderaAfectada.imagen)
                intent.putExtra("textoParaModificar",banderaAfectada.nombre)
                startActivity(intent)
                BanderaProvider.banderas[item.groupId] = Bandera("hola",banderaAfectada.imagen)
                binding.rvBanderas.adapter!!.notifyItemChanged(item.groupId)
            }

            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    private fun display(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

    }


    private fun recargar(){
        BanderaProvider.banderas.addAll(BanderaProvider.banderasCopia)
        binding.rvBanderas.adapter?.notifyDataSetChanged()
    }

    private fun limpiar(){
        BanderaProvider.banderas.removeAll(BanderaProvider.banderas)
        binding.rvBanderas.adapter?.notifyDataSetChanged()
    }
}