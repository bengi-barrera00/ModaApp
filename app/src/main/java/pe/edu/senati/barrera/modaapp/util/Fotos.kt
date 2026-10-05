package pe.edu.senati.barrera.modaapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

object Fotos {
    fun cargar(ruta: String?, lado: Int = 300): Bitmap? {
        if (ruta.isNullOrEmpty() || !File(ruta).exists()) return null

        return try {
            val op = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(ruta, op)

            op.inSampleSize = maxOf(1, maxOf(op.outWidth, op.outHeight) / lado)
            op.inJustDecodeBounds = false

            BitmapFactory.decodeFile(ruta, op)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}