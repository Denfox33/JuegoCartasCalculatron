package com.example.juegos

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class Juego1 : AppCompatActivity() {

    private lateinit var resultadoTextView: TextView
    private lateinit var intentosTextView: TextView
    private lateinit var cartas: Array<ImageView>
    private lateinit var valoresCartas: List<Int>

    private var animacion : Boolean = false

    private val cartasDadasLaVuelta = BooleanArray(12)
    private var primeraCarta: ImageView? = null


    private var isAnimating = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cartas)

        resultadoTextView = findViewById(R.id.resultado)
        intentosTextView = findViewById(R.id.intentos)


        cartas = arrayOf(
            findViewById(R.id.img1), findViewById(R.id.img2), findViewById(R.id.img3),
            findViewById(R.id.img4), findViewById(R.id.img5), findViewById(R.id.img6),
            findViewById(R.id.img7), findViewById(R.id.img8), findViewById(R.id.img9),
            findViewById(R.id.img10), findViewById(R.id.img11), findViewById(R.id.img12)
        )

        for (carta in cartas) {
            carta.setOnClickListener { onCardClick(carta) }
        }
        // volver al menu
        val volverButton: Button = findViewById(R.id.Volver)
        volverButton.setOnClickListener {
            volverAlMain()
        }

        val reiniciarButton: Button = findViewById(R.id.Reinicio)
        reiniciarButton.setOnClickListener {
            reiniciarPartida()
        }

        // se reinicia cuando inicias actividad
        reiniciarPartida()

    }
    fun volverAlMain() {
        // Crear un Intent para volver a MainActivity
        val intent = Intent(this, MainActivity::class.java)

        // Iniciar la actividad MainActivity
        startActivity(intent)

        // Opcional: Finalizar la actividad actual para que no quede en la pila de actividades
        finish()
    }
    // usar semaforo para evitar que se rompa el programa
    // crear animacion de giro de cartas
    fun onCardClick(carta: ImageView) {
        if (!isAnimating) {
            val indice = cartas.indexOf(carta)
            if (vidas > 0 && !cartasDadasLaVuelta[indice]) {
                isAnimating = true

                realizarVolteo(carta, valoresCartas[indice])

                if (primeraCarta == null) {
                    primeraCarta = carta
                } else {
                    if (primeraCarta == carta) {
                        isAnimating = false
                        return
                    }

                    Handler().postDelayed({
                        if (esIgual(primeraCarta!!, carta)) {
                            cartasDadasLaVuelta[cartas.indexOf(primeraCarta!!)] = true
                            cartasDadasLaVuelta[indice] = true
                            primeraCarta = null

                            hasGanado()
                        } else {
                            // Si no son pareja, voltear nuevamente al dorso
                            realizarVolteo(primeraCarta!!, 0)
                            realizarVolteo(carta, 0)

                            restarVida()

                            if (vidas == 0) {
                                hasPerdido()
                            }

                            primeraCarta = null
                        }
                    }, 1000)
                }
            }
        }
    }

    // funcion opcion animacion cartas



    private fun realizarVolteo(carta: ImageView, valor: Int) {
        val flip = ObjectAnimator.ofFloat(carta, "rotationY", 0f, 180f)
        flip.interpolator = AccelerateInterpolator()
        flip.duration = 500

        flip.addUpdateListener {
            val animatedFraction = it.animatedFraction

            if (animatedFraction >= 0.5f) {
                // Cambiar la imagen a la mitad de la animación
                if (valor == 0) {
                    carta.setImageResource(R.drawable.dorso)
                } else {
                    carta.setImageResource(valor)
                }
            }

            // Ajustar la rotación para que no se vea invertida durante la animación
            carta.rotationY = if (animatedFraction < 0.5f) 180f else 0f
        }

        flip.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isAnimating = false
            }
        })

        flip.start()
    }
    private fun mostrarCarta(carta: ImageView, valor: Int) {
        if (valor == 0) {
            carta.setImageResource(R.drawable.dorso)
        } else {
            carta.setImageResource(valor)
        }
    }

    private fun esIgual(carta1: ImageView, carta2: ImageView): Boolean {
        val drawableCarta1 = carta1.drawable
        val drawableCarta2 = carta2.drawable

        return drawableCarta1.constantState == drawableCarta2.constantState
    }

    private fun ocultarCarta(carta: ImageView) {
        carta.setImageResource(R.drawable.dorso)
    }

    private fun reiniciarPartida() {
        // Obtener el valor de las vidas desde SharedPreferences
        val sharedPreferences = getSharedPreferences("ConfiguracionPrefs", Context.MODE_PRIVATE)
        vidas = sharedPreferences.getInt("VIDAS", 10)

        // Después de obtener las vidas, asignar valores aleatorios a las cartas
        valoresCartas = asignarValoresAleatorios()

        for (i in cartas.indices) {
            mostrarCarta(cartas[i], 0)
            cartasDadasLaVuelta[i] = false
        }

        // No necesitas volver a asignar vidas = 10 aquí
        primeraCarta = null
        resultadoTextView.text = ""
        intentosTextView.text = vidas.toString()
    }

    private fun asignarValoresAleatorios(): List<Int> {
        val valores = mutableListOf<Int>()
        val maxRepeticiones = 2

        for (i in 1..6) {
            for (j in 1..maxRepeticiones) {
                valores.add(resources.getIdentifier("n$i", "drawable", packageName))
            }
        }
            //shuffled bartaja las cartas
        return valores.shuffled()
    }

    private var vidas: Int
        get() {
            // Obtener el valor de las vidas desde SharedPreferences
            val sharedPreferences = getSharedPreferences("ConfiguracionPrefs", Context.MODE_PRIVATE)
            return sharedPreferences.getInt("VIDAS", 10)
        }
        set(value) {
            // Establecer el nuevo valor de vidas en las preferencias compartidas
            val sharedPreferences = getSharedPreferences("ConfiguracionPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("VIDAS", value)
            editor.apply()
        }

    private fun restarVida() {
        // Restar una vida utilizando la propiedad personalizada
        vidas = vidas - 1

        // Actualizar el TextView
        intentosTextView.text = vidas.toString()
    }

    private fun hasGanado() {
        val ganado = cartasDadasLaVuelta.all { dadaLaVuelta -> dadaLaVuelta }
                && valoresCartas.all { valor -> valor != 0 }

        if (ganado) {
            // Llevar al jugador a la actividad Victoria
            val intentVictoria = Intent(this, Victoria::class.java)
            startActivity(intentVictoria)
            finish() // Cerrar la actividad actual
        }
    }

    private fun hasPerdido() {
        val perdido = vidas == 0

        if (perdido) {
            // Llevar al jugador a la actividad Derrota
            val intentDerrota = Intent(this, Derrota::class.java)
            startActivity(intentDerrota)
            finish() // Cerrar la actividad actual
        }
    }

    private fun mostrarResultado(mensaje: String) {
        resultadoTextView.text = mensaje
        resultadoTextView.visibility = if (mensaje == "¡Has ganado!" || mensaje == "Has perdido :(") {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
