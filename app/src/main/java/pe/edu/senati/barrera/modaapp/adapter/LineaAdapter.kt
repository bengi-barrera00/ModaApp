package pe.edu.senati.barrera.modaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.senati.barrera.modaapp.databinding.ItemCarritoBinding
import pe.edu.senati.barrera.modaapp.model.LineaPedido
import pe.edu.senati.barrera.modaapp.util.Fotos

class LineaAdapter(private var items: List<LineaPedido>) :
    RecyclerView.Adapter<LineaAdapter.VH>() {

    class VH(val b: ItemCarritoBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(ItemCarritoBinding.inflate(LayoutInflater.from(p.context), p, false))

    override fun onBindViewHolder(h: VH, i: Int) {
        val l = items[i]
        h.b.tvTitulo.text = "${l.modelo} · ${l.talla ?: "-"}"
        h.b.tvDetalle.text = "${l.color ?: "-"} · Cantidad: ${l.cantidad}"
        h.b.tvSubtotal.text = "S/ %.2f".format(l.subtotal)

        val bmp = Fotos.cargar(l.foto, 150)
        if (bmp != null) h.b.ivFoto.setImageBitmap(bmp)
        else h.b.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery)
    }

    override fun getItemCount() = items.size

    fun actualizar(nuevos: List<LineaPedido>) {
        items = nuevos
        notifyDataSetChanged()
    }
}