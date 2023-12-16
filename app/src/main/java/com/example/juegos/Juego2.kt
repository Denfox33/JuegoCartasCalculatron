package com.example.juegos

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStreamWriter


class Juego2 : AppCompatActivity() {

    private lateinit var tiempo: TextView
    private lateinit var Actual: TextView
    private lateinit var Pasada: TextView
    private lateinit var Siguiente: TextView
    private lateinit var respuesta: EditText
    private lateinit var aciertos: TextView
    private lateinit var fallos: TextView

    private lateinit var btnInsertar: Button
    private lateinit var btnBorrarDigito: Button
    private lateinit var btnBorrarNumero: Button

    private var segundosRestantes: Long = 0
    private var countDownTimer: CountDownTimer? = null
    private var aciertosCount: Int = 0
    private var fallosCount: Int = 0

    private var maximo=20
    private var minimo=0

    // pulsado
    private var haPulsado =true

    private lateinit var tiempoTxt: TextView


    private lateinit var num0: Button
    private lateinit var num1: Button
    private lateinit var num2: Button
    private lateinit var num3: Button
    private lateinit var num4: Button
    private lateinit var num5: Button
    private lateinit var num6: Button
    private lateinit var num7: Button
    private lateinit var num8: Button
    private lateinit var num9: Button

    private lateinit var opcionSuma: CheckBox
    private lateinit var opcionResta: CheckBox
    private lateinit var opcionMulti: CheckBox

    private lateinit var sharedPreferences: SharedPreferences


    private lateinit var operadoresDisponibles: MutableList<String>




    private lateinit var cuentaActualTextView: TextView
    private lateinit var cuentaSiguienteTextView: TextView

    private val PREFS_NAME = "ConfiguracionPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora)

        tiempo = findViewById(R.id.tiempo)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Obtener los operadores habilitados desde SharedPreferences
        val tiempoInicial = sharedPreferences.getInt("TIEMPO", 20)




        // Llamada a la función para cargar la configuración
        cargarConfiguracion()
        // Llamada a la función para generar operadores
        val operadores = generarOperador()



        cuentaActualTextView = findViewById(R.id.cuentaActual)  // Asegúrate de que el ID sea el correcto
        cuentaSiguienteTextView = findViewById(R.id.cuentaSiguiente)  // Asegúrate de que el ID sea el correcto




        maximo = sharedPreferences.getInt("maximoTxt", 20)
        minimo = sharedPreferences.getInt("minimoTxt", 0)

        // Inicializar vistas
        tiempo = findViewById(R.id.tiempo)
        Actual = findViewById(R.id.cuentaActual)
        Pasada = findViewById(R.id.cuentaPasada)
        Siguiente = findViewById(R.id.cuentaSiguiente)

        respuesta = findViewById(R.id.resUsuario)
        aciertos = findViewById(R.id.aciertos)
        fallos = findViewById(R.id.fallos)

        btnInsertar = findViewById(R.id.btnIgual)
        btnBorrarDigito = findViewById(R.id.btnCE)
        btnBorrarNumero = findViewById(R.id.btnC)

        operadoresDisponibles = mutableListOf()


        // Configurar clic del botón insertar
        btnInsertar.setOnClickListener {
            verificarRespuesta()
        }


        Siguiente = findViewById(R.id.cuentaSiguiente)

        aciertos = findViewById(R.id.aciertos)
        fallos = findViewById(R.id.fallos)


        num0 = findViewById(R.id.btn0)
        num1 = findViewById(R.id.btn1)
        num2 = findViewById(R.id.btn2)
        num3 = findViewById(R.id.btn3)
        num4 = findViewById(R.id.btn4)
        num5 = findViewById(R.id.btn5)
        num6 = findViewById(R.id.btn6)
        num7 = findViewById(R.id.btn7)
        num8 = findViewById(R.id.btn8)
        num9 = findViewById(R.id.btn9)







        // Configurar el TextView con el tiempo inicial
        tiempoTxt = findViewById(R.id.tiempo)
        tiempoTxt.text = "Tiempo: $tiempoInicial segundos"



        countDownTimer = object : CountDownTimer((tiempoInicial * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                segundosRestantes = millisUntilFinished / 1000
                tiempoTxt.text = "Tiempo: $segundosRestantes segundos"
            }

            override fun onFinish() {
                tiempoTxt.text = "Tiempo: 0 segundos"
                // Lógica para iniciar la actividad Resumen
                iniciarActividadResumen()
            }
        }

        btnInsertar.setOnClickListener {
            if (haPulsado) {
                // Iniciar el temporizador solo en el primer clic en btnInsertar
                iniciarTiempo()
                haPulsado = false
            }

            verificarRespuesta()
        }

        btnBorrarDigito.setOnClickListener {
            borrarUltimoDigito()
        }

        btnBorrarNumero.setOnClickListener {
            borrarNumero()
        }

        num0.setOnClickListener {
            agregarValorAlEditText("0")
        }

        num1.setOnClickListener {
            agregarValorAlEditText("1")
        }

        num2.setOnClickListener {
            agregarValorAlEditText("2")
        }

        num3.setOnClickListener {
            agregarValorAlEditText("3")
        }

        num4.setOnClickListener {
            agregarValorAlEditText("4")
        }

        num5.setOnClickListener {
            agregarValorAlEditText("5")
        }

        num6.setOnClickListener {
            agregarValorAlEditText("6")
        }

        num7.setOnClickListener {
            agregarValorAlEditText("7")
        }

        num8.setOnClickListener {
            agregarValorAlEditText("8")
        }

        num9.setOnClickListener {
            agregarValorAlEditText("9")
        }


     generarCuentas()




    }

    private fun iniciarActividadResumen() {
        val intent = Intent(this, Resum::class.java)
        intent.putExtra("ACIERTOS", aciertosCount)
        intent.putExtra("FALLOS", fallosCount)
        startActivity(intent)
        finish() // Opcional: puedes finalizar la actividad actual si lo deseas
    }


    private fun agregarValorAlEditText(valor: String) {
        val textoActual = respuesta.text.toString()
        respuesta.setText(textoActual + valor)
    }


    private fun generarCuentas() {
        Actual.text = generarCuentaAleatoria()
        Siguiente.text = generarCuentaAleatoria()

    }

    private fun mostrarCuentas() {
        cuentaActualTextView.text = Actual.text
        cuentaSiguienteTextView.text = Siguiente.text
    }
    private fun cargarConfiguracion() {
        try {
            // Obtener los operadores habilitados desde SharedPreferences
            val checkSuma = sharedPreferences.getBoolean("SUMA", false)
            val checkResta = sharedPreferences.getBoolean("RESTA", false)
            val checkMulti = sharedPreferences.getBoolean("MULTI", false)

            // También puedes realizar otras configuraciones según sea necesario
            val tiempoInicial = sharedPreferences.getInt("TIEMPO", 20)
            tiempoTxt.text = "Tiempo: $tiempoInicial segundos"

            // Puedes agregar más configuraciones aquí según tus necesidades

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MiApp", "Error al cargar configuración: ${e.message}")
            // Puedes mostrar un mensaje de log o lanzar una excepción según sea necesario
        }
    }



    private fun verificarRespuesta() {
        val respuestaUsuario = respuesta.text.toString()

        if (validarRespuesta(respuestaUsuario)) {
            // El usuario ha acertado
            aciertosCount++
            cambiarImagenResultado(R.drawable.check)
        } else {
            // El usuario ha fallado
            fallosCount++
            cambiarImagenResultado(R.drawable.fail)
        }

        // Mostrar la cuenta actual y el resultado del usuario
        Pasada.text = "${Actual.text}  = $respuestaUsuario"

        // Actualizar aciertos y fallos
        aciertos.text = "$aciertosCount"
        fallos.text = "$fallosCount"

        // Cambiar la visibilidad de la ImageView verificar
        val imageView = findViewById<ImageView>(R.id.verificar)
        imageView.visibility = View.VISIBLE  // Para hacerla visible

        // Cambiar a la siguiente cuenta
        Actual.text = Siguiente.text
        Siguiente.text = generarCuentaAleatoria()

        // Mostrar las nuevas cuentas
        mostrarCuentas()

        // Borrar la respuesta actual
        respuesta.setText("")
    }


    private fun cambiarImagenResultado(drawableResId: Int) {
        // Cambiar la imagen según el resultado
        // Supongamos que tienes un ImageView llamado 'imagenResultado' en tu diseño
        val imageView = findViewById<ImageView>(R.id.verificar)
        imageView.setImageResource(drawableResId)
    }



    // crear la clase SharePreferencen


    private fun generarOperador(): MutableList<String> {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val checkSuma = sharedPreferences.getBoolean("SUMA", false)
        val checkResta = sharedPreferences.getBoolean("RESTA", false)
        val checkMulti = sharedPreferences.getBoolean("MULTI", false)

        val operadoresDisponibles = mutableListOf<String>()

        if (checkSuma) {
            operadoresDisponibles.add("+")
        }

        if (checkResta) {
            operadoresDisponibles.add("-")
        }

        if (checkMulti) {
            operadoresDisponibles.add("*")
        }

        return operadoresDisponibles
    }


    private fun generarCuentaAleatoria(): String {
        // Ver los operadores disponibles
        val operadores = generarOperador()

        // Seleccionar un operador aleatorio
        val operador = operadores.random()

        // Generar números aleatorios
        var num1 = (minimo..maximo).random()
        var num2 = (minimo..maximo).random()

        // Asegurarse de que num1 sea el número más grande
        if (num1 < num2) {
            val temp = num1
            num1 = num2
            num2 = temp
        }

        return "$num1 $operador $num2"
    }



    private fun validarRespuesta(respuesta: String): Boolean {
        try {
            // Obtén la expresión completa (números y operador)
            val expresionCompleta = Actual.text.toString()

            // Divide la expresión en números y operador
            val partes = expresionCompleta.split(" ")

            if (partes.size == 3) {
                // Extrae los números y el operador
                val num1 = partes[0].toInt()
                val operador = partes[1]
                val num2 = partes[2].toInt()

                // Evaluar la expresión y comparar con la respuesta del usuario
                val resultadoReal = when (operador) {
                    "+" -> num1 + num2
                    "-" -> num1 - num2
                    "*" -> num1 * num2
                    else -> 0 // Manejar operadores desconocidos o futuros
                }

                val resultadoUsuario = respuesta.toInt()

                return resultadoReal == resultadoUsuario
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MiApp", "Error al validar respuesta: ${e.message}")
        }

        return false
    }


    private fun evaluarExpresion(expresion: String): Int {

        val expresionSinOperadores = expresion.replace("[+\\-*/]".toRegex(), "")
        return expresionSinOperadores.toInt()
    }


    private fun iniciarTiempo() {
        countDownTimer?.start()
    }



    private fun borrarUltimoDigito() {
        val textoActual = respuesta.text.toString()
        if (textoActual.isNotEmpty()) {
            respuesta.setText(textoActual.substring(0, textoActual.length - 1))
        }
    }

    private fun borrarNumero() {
        respuesta.setText("")
    }

    private fun guardarResultadosEnArchivo(aciertos: Int, fallos: Int) {
        try {
            val fileOutput = openFileOutput("resultados.txt", Context.MODE_PRIVATE)
            val outputStreamWriter = OutputStreamWriter(fileOutput)
            outputStreamWriter.write("$aciertos,$fallos")
            outputStreamWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}