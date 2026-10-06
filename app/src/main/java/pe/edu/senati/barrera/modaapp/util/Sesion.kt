package pe.edu.senati.barrera.modaapp.util

import android.content.Context

object Sesion {
    private const val PREFS = "sesion"

    private fun prefs(ctx: Context) = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun guardar(ctx: Context, usuario: String, rol: String) {
        prefs(ctx).edit().putString("usuario", usuario).putString("rol", rol).apply()
    }

    fun usuario(ctx: Context): String? = prefs(ctx).getString("usuario", null)
    fun rol(ctx: Context): String? = prefs(ctx).getString("rol", null)

    fun cerrar(ctx: Context) {
        prefs(ctx).edit().clear().apply()
    }
}