package com.example.postgresqlbasico.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postgresqlbasico.R
import com.example.postgresqlbasico.data.dao.ProductoDao
import com.example.postgresqlbasico.data.model.ProductModel
import com.example.postgresqlbasico.databinding.ActivityMainBinding
import com.example.postgresqlbasico.presentation.adapter.ProductoAdapter
import com.example.postgresqlbasico.presentation.common.UiState
import com.example.postgresqlbasico.presentation.common.makeCall
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ProductoAdapter.IOnClickListner {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()

        leerProducto("")
    }

    override fun onResume() {
        super.onResume()

        if (!existeCambio) return

        existeCambio = false
        leerProducto(binding.etBuscar.text.toString().trim())
    }

    private fun initListener() {
        binding.includeToolbar.ibAccion.setOnClickListener {
            startActivity(
                Intent(this, OperacionProductoActivity::class.java)
            )
        }

        binding.rvLista.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ProductoAdapter(this@MainActivity)
        }

        binding.tilBuscar.setEndIconOnClickListener {
            leerProducto(binding.etBuscar.text.toString().trim())
        }

        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if(binding.etBuscar.text.toString().trim().isEmpty()){
                    leerProducto("")
                    // Ocultar teclado - función nativa de Android
                    binding.etBuscar.clearFocus()
                }
            }
        })
    }

    override fun clickEditar(producto: ProductModel) {
        startActivity(
            Intent(this, OperacionProductoActivity::class.java).apply {
                putExtra("id", producto.id)
                putExtra("descripcion", producto.descripcion)
//                putExtra("codigobarra", producto.codigobarra)
                putExtra("precio", producto.precio)
            }
        )
    }

    override fun clickEliminar(producto: ProductModel) {
        MaterialAlertDialogBuilder(this).apply {
            setTitle("Eliminar")
            setMessage("¿Desea eliminar el registro: ${producto.descripcion}?")
            setCancelable(false)

            setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("SI") { dialog, _ ->
                eliminar(producto)
                leerProducto(binding.etBuscar.text.toString().trim())
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun leerProducto(dato: String) = lifecycleScope.launch {
        binding.progressBar.isVisible = true

        makeCall { ProductoDao.listar(dato) }.let {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    // Reemplazado UtilsMessage por MaterialAlertDialogBuilder
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle("ERROR")
                        .setMessage(it.message)
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                }

                is UiState.Success -> {
                    binding.progressBar.isVisible = false
                    (binding.rvLista.adapter as ProductoAdapter).setList(it.data)
                }
            }
        }
    }

    private fun eliminar(model: ProductModel) = lifecycleScope.launch {
        binding.progressBar.isVisible = true

        makeCall { ProductoDao.eliminar(model) }.let {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    // Reemplazado UtilsMessage por MaterialAlertDialogBuilder
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle("ERROR")
                        .setMessage(it.message)
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                }

                is UiState.Success -> {
                    binding.progressBar.isVisible = false
                    // Reemplazado UtilsMessage por Toast nativo
                    Toast.makeText(this@MainActivity, "Registro eliminado", Toast.LENGTH_SHORT).show()
                    leerProducto(binding.etBuscar.text.toString().trim())
                }
            }
        }
    }

    companion object {
        var existeCambio = false
    }
}