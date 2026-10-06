package pe.edu.senati.barrera.modaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.senati.barrera.modaapp.R
import pe.edu.senati.barrera.modaapp.databinding.ItemFilaBinding
import pe.edu.senati.barrera.modaapp.model.Fila

class FilaAdapter(
    private var items: List<Fila>,
    private val onClick: ((Fila) -> Unit)? = null
) : RecyclerView.Adapter<FilaAdapter.VH>() {

    class VH(val b: ItemFilaBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(ItemFilaBinding.inflate(LayoutInflater.from(p.context), p, false))

    override fun onBindViewHolder(h: VH, i: Int) {
        val f = items[i]
        val ctx = h.b.root.context
        h.b.tvTitulo.text = f.titulo
        h.b.tvDetalle.text = f.detalle
        h.b.tvDerecha.text = f.derecha
        h.b.tvDerecha.setTextColor(ctx.getColor(if (f.alerta) R.color.rojo else R.color.rosa_oscuro))
        h.b.root.setCardBackgroundColor(ctx.getColor(if (f.alerta) R.color.rosa_claro else R.color.blanco))
        h.b.root.setOnClickListener { onClick?.invoke(f) }
    }

    override fun getItemCount() = items.size

    fun actualizar(nuevos: List<Fila>) {
        items = nuevos
        notifyDataSetChanged()
    }
}