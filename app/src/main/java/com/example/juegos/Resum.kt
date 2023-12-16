package com.example.juegos
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Resum : AppCompatActivity() {

    private lateinit var fallosNumerosActual: TextView
    private lateinit var aciertosNumerosActual: TextView
    private lateinit var porcentajeAciertoActual: TextView
    private lateinit var regresarBtn: Button

    private lateinit var fallosAnterior: TextView
    private lateinit var aciertosAnterior: TextView
    private lateinit var porcentajeAnterior: TextView
    private val PREFS_NAME = "ConfiguracionPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)

        // Inicializar vistas
        fallosNumerosActual = findViewById(R.id.fallosNumerosActual)
        aciertosNumerosActual = findViewById(R.id.acertadasNumeroActual)
        porcentajeAciertoActual = findViewById(R.id.porcentajeActual)
        regresarBtn = findViewById(R.id.regresar)

        fallosAnterior = findViewById(R.id.acertadasNumero)
        aciertosAnterior = findViewById(R.id.fallosNumeros)
        porcentajeAnterior = findViewById(R.id.porcentaje)

        // Cargar datos previos
        cargarDatosPrevios()

        // Obtener resultados actuales
        val aciertosActuales = intent.getIntExtra("ACIERTOS", 0)
        val fallosActuales = intent.getIntExtra("FALLOS", 0)

        // Mostrar resultados actuales
        aciertosNumerosActual.text = "$aciertosActuales"
        fallosNumerosActual.text = "$fallosActuales"

        // Calcular porcentaje de aciertos
        val totalIntentos = aciertosActuales + fallosActuales
        val porcentaje = if (totalIntentos > 0) {
            (aciertosActuales.toFloat() / totalIntentos * 100).toInt()
        } else {
            0
        }

        // Mostrar porcentaje de aciertos
        porcentajeAciertoActual.text = "$porcentaje%"

        // Guardar resultados y configurar el botón de regreso
        regresarBtn.setOnClickListener {
            guardarResultados(aciertosActuales, fallosActuales)
            volver()
        }
    }

    // Función para cargar datos previos
    private fun cargarDatosPrevios() {
        val preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Obtener los resultados previos
        val aciertosPrevios = preferences.getInt("aciertasNumero", 0)
        val fallosPrevios = preferences.getInt("fallosNumeros", 0)
        val porcentajePrevio = preferences.getInt("porcentaje", 0)

        // Mostrar los resultados previos
        aciertosAnterior.text = "$aciertosPrevios"
        fallosAnterior.text = "$fallosPrevios"
        porcentajeAnterior.text = "$porcentajePrevio%"
    }

    // Función para guardar resultados en SharedPreferences
    private fun guardarResultados(aciertos: Int, fallos: Int) {
        val preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()

        // Guardar los resultados actuales
        editor.putInt("aciertasNumero", aciertos)
        editor.putInt("fallosNumeros", fallos)

        // Calcular el porcentaje de aciertos
        val totalIntentos = aciertos + fallos
        val porcentajeAcierto = if (totalIntentos > 0) {
            (aciertos.toFloat() / totalIntentos * 100).toInt()
        } else {
            0
        }

        // Guardar el porcentaje de aciertos
        editor.putInt("porcentaje", porcentajeAcierto)

        editor.apply()
    }

    // Función para volver al juego
    private fun volver() {
        // Cambia la actividad a la que deseas volver
        val intent = Intent(this, Juego2::class.java)
        startActivity(intent)
        // Finaliza esta actividad para que no permanezca en la pila
        finish()
    }
}


