package pe.edu.senati.barrera.modaapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Fechas {
    fun ahora(): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
}