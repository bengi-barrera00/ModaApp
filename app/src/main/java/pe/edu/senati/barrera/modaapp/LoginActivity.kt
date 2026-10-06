package pe.edu.senati.barrera.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pe.edu.senati.barrera.modaapp.data.DBHelper
import pe.edu.senati.barrera.modaapp.databinding.ActivityLoginBinding
import pe.edu.senati.barrera.modaapp.util.Sesion
import pe.edu.senati.barrera.modaapp.util.Ui

class LoginActivity : AppCompatActivity() {

    private lateinit var b: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // HU-13: si ya hay sesión guardada, entra directo al menú
        val guardado = Sesion.usuario(this)
        if (guardado != null) {
            irAlMenu(guardado, Sesion.rol(this) ?: "")
            return
        }

        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)
        Ui.ajustar(this, b.root)

        b.btnIngresar.setOnClickListener {
            val u = b.etUsuario.text.toString().trim()
            val c = b.etClave.text.toString().trim()

            b.tilUsuario.error = if (u.isEmpty()) getString(R.string.err_campo) else null
            b.tilClave.error = if (c.isEmpty()) getString(R.string.err_campo) else null
            if (u.isEmpty() || c.isEmpty()) return@setOnClickListener

            val usuario = DBHelper(this).validarUsuario(u, c)
            if (usuario != null) {
                Sesion.guardar(this, usuario.usuario, usuario.rol)
                irAlMenu(usuario.usuario, usuario.rol)
            } else {
                Toast.makeText(this, R.string.cred_incorrectas, Toast.LENGTH_SHORT).show()
            }
        }

        b.btnCatalogo.setOnClickListener {
            startActivity(Intent(this, CatalogoActivity::class.java))
        }
    }

    private fun irAlMenu(usuario: String, rol: String) {
        startActivity(
            Intent(this, MenuActivity::class.java)
                .putExtra("usuario", usuario)
                .putExtra("rol", rol)
        )
        finish()
    }
}