package com.example.juegos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.app.AppCompatActivity

class Configuracion : AppCompatActivity() {

    private lateinit var tiempoTxt: TextInputEditText
    private lateinit var minimoTxt: TextInputEditText
    private lateinit var maximoTxt: TextInputEditText
    private lateinit var vidasTxt: TextInputEditText

    private lateinit var tiempoConfi: TextInputLayout
    private lateinit var minimoConfi: TextInputLayout
    private lateinit var maximoConfi: TextInputLayout
    private lateinit var vidaConfi : TextInputLayout

    private lateinit var guardarButton: Button


    private val sharedPreferences by lazy {
        getSharedPreferences("ConfiguracionPrefs", Context.MODE_PRIVATE)
    }

    private lateinit var opcionSuma: CheckBox
    private lateinit var opcionResta: CheckBox
    private lateinit var opcionMulti: CheckBox

    // Nombre del archivo de preferencias compartidas
    private val PREFS_NAME = "ConfiguracionPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        // Inicializar las vistas
        tiempoTxt = findViewById(R.id.confiTxt)
        minimoTxt = findViewById(R.id.minimoTxt)
        maximoTxt = findViewById(R.id.maximoTxt)
        vidasTxt = findViewById(R.id.VidaTxt)

        tiempoConfi = findViewById(R.id.tiempo_confi)
        minimoConfi = findViewById(R.id.minimo)
        maximoConfi = findViewById(R.id.maximo)
        vidaConfi = findViewById(R.id.vida)

        guardarButton = findViewById(R.id.guardar)



        // Obtener referencias a los CheckBox de la actividad anterior
        opcionSuma = findViewById(R.id.opcionSuma)
        opcionResta = findViewById(R.id.opcionResta)
        opcionMulti = findViewById(R.id.opcionMulti)

        // Obtener SharedPreferences sin utilizar by lazy
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


        // Cargar valores desde SharedPreferences
        opcionSuma.isChecked = sharedPreferences.getBoolean("SUMA", true)
        opcionResta.isChecked = sharedPreferences.getBoolean("RESTA", false)
        opcionMulti.isChecked = sharedPreferences.getBoolean("MULTI", false)

        // Restaurar la configuración anterior
        cargarConfiguracion()

        // Configurar el clic del botón de guardar
        guardarButton.setOnClickListener {
            guardarConfiguracion()
        }
    }

    private fun guardarConfiguracion() {
        // Obtener los valores ingresados por el usuario
        val tiempo = tiempoTxt.text.toString()
        val minimo = minimoTxt.text.toString()
        val maximo = maximoTxt.text.toString()
        val vidas = vidasTxt.text.toString()

        // Validar las entradas
        if (tiempo.isEmpty() || minimo.isEmpty() || maximo.isEmpty()) {
            // Mostrar un mensaje de error si algún campo está vacío
            mostrarMensajeError("Todos los campos deben ser llenados")
            return
        }

        // solo se permiten números
        try {
            val tiempoValor = tiempo.toInt()
            val minimoValor = minimo.toInt()
            val maximoValor = maximo.toInt()
            val vidasValor = if (vidas.isNotEmpty()) vidas.toInt() else 10 // Valor predeterminado 10 si no se proporciona

            // Validar que el mínimo no sea mayor al máximo
            if (minimoValor > maximoValor) {
                mostrarMensajeError("El valor mínimo no puede ser mayor al valor máximo")
                return
            }

            // Obtener las opciones seleccionadas
            val sumaSeleccionada = opcionSuma.isChecked
            val restaSeleccionada = opcionResta.isChecked
            val multiSeleccionada = opcionMulti.isChecked

            // Validar que al menos una opción esté seleccionada
            if (!sumaSeleccionada && !restaSeleccionada && !multiSeleccionada) {
                mostrarMensajeError("Seleccione al menos una operación")
                return
            }

            // Almacenar la configuración en SharedPreferences
            val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("TIEMPO", tiempoValor)
            editor.putInt("MINIMO", minimoValor)
            editor.putInt("MAXIMO", maximoValor)
            editor.putInt("VIDAS", vidasValor)
            editor.putBoolean("SUMA", sumaSeleccionada)
            editor.putBoolean("RESTA", restaSeleccionada)
            editor.putBoolean("MULTI", multiSeleccionada)
            editor.apply()

            // Finalizar la actividad actual (Configuracion) y volver a MainActivity
            finish()

        } catch (e: NumberFormatException) {
            // mensaje error entrada
            mostrarMensajeError("Ingrese números válidos en los campos")
        }
    }

    private fun cargarConfiguracion() {
        // Cargar la configuración anterior desde SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Configurar las vistas con los valores almacenados
        tiempoTxt.setText(sharedPreferences.getInt("TIEMPO", 20).toString())
        minimoTxt.setText(sharedPreferences.getInt("MINIMO", 0).toString())
        maximoTxt.setText(sharedPreferences.getInt("MAXIMO", 20).toString())
        vidasTxt.setText(sharedPreferences.getInt("VIDAS", 10).toString())

        opcionSuma.isChecked = sharedPreferences.getBoolean("SUMA", true)
        opcionResta.isChecked = sharedPreferences.getBoolean("RESTA", false)
        opcionMulti.isChecked = sharedPreferences.getBoolean("MULTI", false)
    }

    private fun mostrarMensajeError(mensaje: String) {
        // mensaje error en el botón de guardar
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
