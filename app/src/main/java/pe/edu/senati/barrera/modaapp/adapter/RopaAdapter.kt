package pe.edu.senati.barrera.modaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.senati.barrera.modaapp.databinding.ItemRopaBinding
import pe.edu.senati.barrera.modaapp.model.Ropa
import pe.edu.senati.barrera.modaapp.util.Fotos

class RopaAdapter(
    private var items: List<Ropa>,
    private val onClick: (Ropa) -> Unit = {}
) : RecyclerView.Adapter<RopaAdapter.VH>() {

    class VH(val b: ItemRopaBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(ItemRopaBinding.inflate(LayoutInflater.from(p.context), p, false))

    override fun onBindViewHolder(h: VH, i: Int) {
        val r = items[i]
        h.b.tvModelo.text = r.modelo
        h.b.tvTalla.text = "Talla: ${r.talla ?: "-"}"
        h.b.tvColor.text = "Color: ${r.color ?: "-"}"
        h.b.tvCantidad.text = "Stock: ${r.cantidad}"

        val bmp = Fotos.cargar(r.foto, 150)
        if (bmp != null) h.b.ivFotoItem.setImageBitmap(bmp)
        else h.b.ivFotoItem.setImageResource(android.R.drawable.ic_menu_gallery)

        h.b.root.setOnClickListener { onClick(r) }
    }

    override fun getItemCount() = items.size

    fun actualizar(nuevos: List<Ropa>) {
        items = nuevos
        notifyDataSetChanged()
    }
}