package com.example.juegos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // boton juego 2
        val inicioGame2: Button = findViewById(R.id.game2)
        inicioGame2.setOnClickListener {
            iniciarGame2()
        }

        val confi: Button = findViewById(R.id.confi)
        confi.setOnClickListener {
            iniciarConfi()

        }

    }


    // inicar juego 1

    fun iniciarGame1 (view : View){
        val intent = Intent(this, Juego1::class.java).apply {  }
        startActivity(intent)
    }

    //inicar configuracion
    fun iniciarGame2 (){
        val intent = Intent(this, Juego2::class.java).apply {  }
        startActivity(intent)
    }
    //inicar configuracion
    fun iniciarConfi() {
        val intent = Intent(this, Configuracion::class.java).apply {  }
        startActivity(intent)
    }

}

