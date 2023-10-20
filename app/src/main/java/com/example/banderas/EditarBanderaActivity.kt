package com.example.banderas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banderas.databinding.ActivityEditarBanderaBinding
import com.example.banderas.databinding.ActivityMainBinding

class EditarBanderaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarBanderaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_bandera)

        val indice = intent.getIntExtra("indice",0)
        val textoHint = intent.getStringExtra("textoParaModificar")
        binding = ActivityEditarBanderaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this,"texto "+textoHint, Toast.LENGTH_SHORT).show()

        binding.ivCambiarBandera.setImageResource(intent.getIntExtra("imagen",0))
        binding.textoEditarNombre.hint = textoHint


        val nombre=intent.getStringExtra("nombre")
        val etNombre=findViewById<EditText>(R.id.textoEditarNombre)
        etNombre.hint=nombre
        val btnCambiar=findViewById<Button>(R.id.buttonCambiar)
        val btnCancelar=findViewById<Button>(R.id.buttonCancelar)
        btnCambiar.setOnClickListener{
            val intent = Intent()
            val name = etNombre.text.toString()
            intent.putExtra("nombre",name)
            intent.putExtra("indiceback",indice)
            setResult(RESULT_OK,intent)
            finish()
        }
        btnCancelar.setOnClickListener{
//            setResult(RESULT_OK,intent)
//            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }



}