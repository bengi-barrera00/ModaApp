package pe.edu.senati.barrera.modaapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pe.edu.senati.barrera.modaapp.model.Usuario

class DBHelper(ctx: Context) : SQLiteOpenHelper(ctx, "modaapp.db", null, 2) {

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

        crearTablasPedidos(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        if (old < 2) crearTablasPedidos(db)
    }

    private fun crearTablasPedidos(db: SQLiteDatabase) {
        db.execSQL("""CREATE TABLE cliente(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            telefono TEXT UNIQUE NOT NULL, nombres TEXT NOT NULL,
            apellidos TEXT NOT NULL, fecha_registro TEXT)""")
        db.execSQL("""CREATE TABLE pedido(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            id_cliente INTEGER NOT NULL, fecha TEXT, total REAL,
            estado TEXT NOT NULL DEFAULT 'PENDIENTE', fecha_atencion TEXT,
            FOREIGN KEY(id_cliente) REFERENCES cliente(id))""")
        db.execSQL("""CREATE TABLE detalle_pedido(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            id_pedido INTEGER NOT NULL, id_ropa INTEGER NOT NULL,
            cantidad INTEGER CHECK(cantidad > 0), precio_unit REAL, subtotal REAL,
            FOREIGN KEY(id_pedido) REFERENCES pedido(id) ON DELETE CASCADE,
            FOREIGN KEY(id_ropa) REFERENCES ropa(id))""")
    }

    fun validarUsuario(u: String, c: String): Usuario? {
        readableDatabase.rawQuery(
            "SELECT id, usuario, rol FROM usuario WHERE usuario=? AND clave=?", arrayOf(u, c)
        ).use { cur ->
            return if (cur.moveToFirst()) Usuario(cur.getInt(0), cur.getString(1), cur.getString(2)) else null
        }
    }
}