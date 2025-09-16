package com.example.postgresqlbasico.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.postgresqlbasico.data.model.ProductModel
import com.example.postgresqlbasico.databinding.ItemsProductoBinding

class ProductoAdapter(
    private val onClickListner: IOnClickListner
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    private var lista = emptyList<ProductModel>()

    interface IOnClickListner {
        fun clickEditar(producto: ProductModel)
        fun clickEliminar(producto: ProductModel)
    }

    inner class ProductoViewHolder(private val binding: ItemsProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun enlazar(producto: ProductModel) {
            binding.tvTitulo.text = producto.descripcion
            binding.tvCodigoBarra.text = producto.codigoBarra
            binding.tvPrecio.text = String.format("%.2f", producto.precio)

            binding.ibEditar.setOnClickListener { onClickListner.clickEditar(producto) }
            binding.ibEliminar.setOnClickListener { onClickListner.clickEliminar(producto) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        return ProductoViewHolder(
            ItemsProductoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.enlazar(lista[position])
    }

    fun setList(listaProducto: List<ProductModel>) {
        this.lista = listaProducto
        notifyDataSetChanged()
    }
}