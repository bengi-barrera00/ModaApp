package pe.edu.senati.barrera.modaapp.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import pe.edu.senati.barrera.modaapp.R

object Whatsapp {
    private val paquetes = listOf("com.whatsapp", "com.whatsapp.w4b") // normal y Business

    fun abrir(ctx: Context, telefono: String, mensaje: String) {
        val uri = Uri.parse("https://wa.me/51$telefono?text=${Uri.encode(mensaje)}")
        for (pkg in paquetes) {
            try {
                ctx.startActivity(Intent(Intent.ACTION_VIEW, uri).setPackage(pkg))
                return
            } catch (e: ActivityNotFoundException) {
                // prueba con el siguiente paquete
            }
        }
        Toast.makeText(ctx, R.string.sin_whatsapp, Toast.LENGTH_LONG).show()
    }
}