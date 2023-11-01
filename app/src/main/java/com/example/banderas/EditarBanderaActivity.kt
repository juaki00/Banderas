package com.example.banderas

import android.R.*
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.banderas.databinding.ActivityEditarBanderaBinding


class EditarBanderaActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityEditarBanderaBinding
    private lateinit var btnCambiar: Button
    private lateinit var btnCancelar: Button
    private lateinit var infoNombre: EditText
    private lateinit var infoImagen: ImageView
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEditarBanderaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nombre = intent.getStringExtra("textoParaModificar")
        val imagen = intent.getIntExtra("imagen", 0)
        btnCambiar = binding.buttonCambiar
        btnCambiar.setOnClickListener(this)
        btnCancelar = binding.buttonCancelar
        btnCancelar.setOnClickListener(this)
        infoNombre = binding.textoEditarNombre
        infoImagen = binding.ivCambiarBandera
        infoImagen.setImageResource(imagen)
        infoNombre.hint = nombre
        id = intent.getIntExtra("indice", 0)
    }


    override fun onClick(v: View?) {
        when (v?.id){
            R.id.buttonCambiar -> {
                val intent = Intent(this, MainActivity::class.java)
                val name = infoNombre.text.toString()

                if(name != "") {
                    intent.putExtra("nombre", name)
                    intent.putExtra("id", id)
                    setResult(RESULT_OK, intent)
                }
                finish()
            }
            R.id.buttonCancelar -> {
                finish()
            }
        }
    }
}