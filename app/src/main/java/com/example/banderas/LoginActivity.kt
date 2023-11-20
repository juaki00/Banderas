package com.example.banderas

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.banderas.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var bindin: ActivityLoginBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        bindin = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindin.root)
        Thread.sleep(2000)
        splashScreen.setKeepOnScreenCondition{false}
        prefs = getSharedPreferences("app", MODE_PRIVATE)
        establecerValoresSiExisten()
        //Listener del boton
        bindin.buttonLogin.setOnClickListener{
            val email = bindin.email.text.toString()
            val password = bindin.password.text.toString()
            if(login(email, password)) goToMain()
            guardarPreferencias(email, password)
        }

        //SplashScreen
        bindin.motionLayout.setTransitionListener( object: MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                bindin.motionLayout.visibility = View.GONE
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        })
    }

    private fun login(email: String, password: String): Boolean {
        var valido = false
        if(!emailValido(email)){
            Toast.makeText(this, "e-mail no válido, introduzca un e-mail correcto", Toast.LENGTH_SHORT).show()
        }else if (!passwordValida(password)){
            Toast.makeText(this, "Password no válida, debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
        }else{
            valido = true
        }
        return valido
    }

    private fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun guardarPreferencias(email: String, password: String){
        val editor = prefs.edit()
        if(bindin.recordar.isChecked){
            editor.putString("email",email)
            editor.putString("password",password)
            editor.putBoolean("recordar",true)
            editor.apply()
        }else{
            editor.clear()
            editor.putBoolean("recordar", false)
            editor.apply()
        }
    }

    private fun establecerValoresSiExisten(){
        val email = prefs.getString("email", "")
        val password = prefs.getString("password", "")
        val recordar = prefs.getBoolean("recordar", false)
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            bindin.email.setText(email)
            bindin.password.setText(password)
            bindin.recordar.isChecked = recordar
        }
    }

    private fun emailValido(email: String): Boolean{
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun passwordValida(password: String): Boolean{
        return !TextUtils.isEmpty(password) && password.length > 7
    }

    
}