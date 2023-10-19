package com.example.banderas

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.banderas.databinding.ActivityEditarBanderaBinding


class VolverAMenuPrincipalListener : View.OnClickListener{

    private lateinit var intentLaunch: ActivityResultLauncher<Intent>
    override fun onClick(v: View?) {
//        val intent = Intent(EditarBanderaActivity(), MainActivity::class.java)
//        intent.putExtra("Cancelar","cancelar")
//        startActivity(intent)


    }
}