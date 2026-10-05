package pe.edu.senati.barrera.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pe.edu.senati.barrera.modaapp.data.DBHelper
import pe.edu.senati.barrera.modaapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var b: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnIngresar.setOnClickListener {

            val u = b.etUsuario.text.toString().trim()
            val c = b.etClave.text.toString().trim()

            b.tilUsuario.error = if (u.isEmpty()) getString(R.string.err_campo) else null
            b.tilClave.error = if (c.isEmpty()) getString(R.string.err_campo) else null

            if (u.isEmpty() || c.isEmpty()) return@setOnClickListener

            val dbHelper = DBHelper(this)
            val usuario = dbHelper.validarUsuario(u, c)

            if (usuario != null) {
                val intent = Intent(this, MenuActivity::class.java).apply {
                    putExtra("usuario", usuario.usuario)
                    putExtra("rol", usuario.rol)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, R.string.cred_incorrectas, Toast.LENGTH_SHORT).show()
            }
        }

        b.btnCatalogo.setOnClickListener {
            startActivity(Intent(this, CatalogoActivity::class.java))
        }
    }
}