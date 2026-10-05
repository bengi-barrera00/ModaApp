package pe.edu.senati.barrera.modaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.senati.barrera.modaapp.databinding.ItemCatalogoBinding
import pe.edu.senati.barrera.modaapp.model.Ropa
import pe.edu.senati.barrera.modaapp.util.Fotos

class CatalogoAdapter(private var items: List<Ropa>) :
    RecyclerView.Adapter<CatalogoAdapter.VH>() {

    class VH(val b: ItemCatalogoBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(ItemCatalogoBinding.inflate(LayoutInflater.from(p.context), p, false))

    override fun onBindViewHolder(h: VH, i: Int) {
        val r = items[i]
        h.b.tvModelo.text = r.modelo
        h.b.tvTalla.text = "Talla ${r.talla ?: "-"} · ${r.color ?: "-"}"
        h.b.tvPrecio.text = "S/ %.2f".format(r.precio)

        val bmp = Fotos.cargar(r.foto, 300)
        if (bmp != null) h.b.ivFoto.setImageBitmap(bmp)
        else h.b.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery)
    }

    override fun getItemCount() = items.size

    fun actualizar(nuevos: List<Ropa>) {
        items = nuevos
        notifyDataSetChanged()
    }
}