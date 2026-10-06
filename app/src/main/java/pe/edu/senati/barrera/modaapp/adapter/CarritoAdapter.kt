package pe.edu.senati.barrera.modaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.senati.barrera.modaapp.databinding.ItemCarritoBinding
import pe.edu.senati.barrera.modaapp.model.ItemCarrito
import pe.edu.senati.barrera.modaapp.util.Fotos

class CarritoAdapter(
    private var items: List<ItemCarrito>,
    private val onLargo: (ItemCarrito) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.VH>() {

    class VH(val b: ItemCarritoBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(ItemCarritoBinding.inflate(LayoutInflater.from(p.context), p, false))

    override fun onBindViewHolder(h: VH, i: Int) {
        val item = items[i]
        val r = item.ropa

        h.b.tvTitulo.text = "${r.modelo} · ${r.talla ?: "-"}"
        h.b.tvDetalle.text = "${r.color ?: "-"} · ${item.cantidad} x S/ ${"%.2f".format(r.precio)}"
        h.b.tvSubtotal.text = "S/ %.2f".format(item.subtotal)

        val bmp = Fotos.cargar(r.foto, 150)
        if (bmp != null) h.b.ivFoto.setImageBitmap(bmp)
        else h.b.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery)

        h.b.root.setOnLongClickListener {
            onLargo(item)
            true
        }
    }

    override fun getItemCount() = items.size

    fun actualizar(nuevos: List<ItemCarrito>) {
        items = nuevos
        notifyDataSetChanged()
    }
}