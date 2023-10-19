package com.example.banderas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banderas.databinding.ActivityEditarBanderaBinding
import com.example.banderas.databinding.ActivityMainBinding

class EditarBanderaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarBanderaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_bandera)

        binding = ActivityEditarBanderaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivCambiarBandera.setImageResource(intent.getIntExtra("imagen",0))
        binding.textoEditarNombre.hint = intent.getStringExtra("textoParaModificar")

        binding.buttonCancelar.setOnClickListener(VolverAMenuPrincipalListener())

        val nombre=intent.getStringExtra("nombre")
        val etNombre=findViewById<EditText>(R.id.textoEditarNombre)
        etNombre.hint=nombre
        val btnCambiar=findViewById<Button>(R.id.buttonCambiar)
        btnCambiar.setOnClickListener{
            val intent = Intent()
            val name = etNombre.text.toString()
            intent.putExtra("nombre",name)
            setResult(RESULT_OK,intent)
            finish()
        }
    }



}