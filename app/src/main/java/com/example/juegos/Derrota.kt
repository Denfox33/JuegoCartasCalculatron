package com.example.juegos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Derrota: AppCompatActivity() {

    private lateinit var reinicio : Button
    private lateinit var menu : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_derrota)

        reinicio=findViewById(R.id.reinicarV)
        menu = findViewById(R.id.VolverV)

        reinicio.setOnClickListener {
            reiniciarPartida()
        }

        menu.setOnClickListener {
            volverAlMain()
        }

    }
    fun volverAlMain() {
        // Crear un Intent para volver a MainActivity
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)


    }

    private fun reiniciarPartida() {

        val sharedPreferences = getSharedPreferences("ConfiguracionPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("VIDAS", 5)

        editor.apply()
        val intent = Intent(this, Juego1::class.java)
        startActivity(intent)

    }


}