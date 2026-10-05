package pe.edu.senati.barrera.modaapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pe.edu.senati.barrera.modaapp.model.Usuario
import pe.edu.senati.barrera.modaapp.model.Categoria
import pe.edu.senati.barrera.modaapp.model.Ropa

class DBHelper(ctx: Context) : SQLiteOpenHelper(ctx, "modaapp.db", null, 1) {

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""CREATE TABLE usuario(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            usuario TEXT UNIQUE NOT NULL, clave TEXT NOT NULL,
            rol TEXT NOT NULL, telefono TEXT)""")
        db.execSQL("""CREATE TABLE categoria(
            id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT UNIQUE NOT NULL)""")
        db.execSQL("""CREATE TABLE ropa(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            modelo TEXT NOT NULL, id_categoria INTEGER NOT NULL,
            talla TEXT, marca TEXT, color TEXT,
            precio REAL CHECK(precio > 0), cantidad INTEGER CHECK(cantidad >= 0),
            foto TEXT,
            FOREIGN KEY(id_categoria) REFERENCES categoria(id))""")

        db.execSQL("INSERT INTO usuario(usuario,clave,rol,telefono) VALUES('admin','1234','ADMIN','962515407')")
        listOf("Polos", "Pantalones", "Vestidos", "Casacas", "Zapatillas").forEach {
            db.execSQL("INSERT INTO categoria(nombre) VALUES(?)", arrayOf(it))
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) { /* Sprint 3 */ }

    fun validarUsuario(u: String, c: String): Usuario? {
        readableDatabase.rawQuery(
            "SELECT id, usuario, rol FROM usuario WHERE usuario=? AND clave=?", arrayOf(u, c)
        ).use { cur ->
            return if (cur.moveToFirst()) Usuario(cur.getInt(0), cur.getString(1), cur.getString(2)) else null
        }
    }
}