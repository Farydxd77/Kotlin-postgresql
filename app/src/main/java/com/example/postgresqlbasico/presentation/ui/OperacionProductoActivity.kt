package com.example.postgresqlbasico.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.postgresqlbasico.R
import com.example.postgresqlbasico.data.dao.ProductoDao
import com.example.postgresqlbasico.data.model.ProductModel
import com.example.postgresqlbasico.databinding.ActivityOperacionProductoBinding
import com.example.postgresqlbasico.presentation.common.UiState
import com.example.postgresqlbasico.presentation.common.makeCall
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class OperacionProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOperacionProductoBinding
    private var _id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperacionProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()

        if (intent.extras != null)
            obtenerProducto()
    }

    private fun initListener() {
        binding.includeToolbar.toolbar.apply {
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

            subtitle = "Registrar | Editar producto"
            navigationIcon = AppCompatResources.getDrawable(
                this@OperacionProductoActivity,
                R.drawable.baseline_arrow_back_24
            )
        }

        binding.includeToolbar.ibAccion.setImageResource(R.drawable.baseline_done_all_24)

        binding.includeToolbar.ibAccion.setOnClickListener {
            if (binding.etDescripcion.text.toString().trim().isEmpty() ||
                binding.etCodigoBarra.text.toString().trim().isEmpty() ||
                binding.etPrecio.text.toString().trim().isEmpty()
            ) {
                // Reemplazado UtilsMessage por MaterialAlertDialogBuilder
                MaterialAlertDialogBuilder(this)
                    .setTitle("ADVERTENCIA")
                    .setMessage("Debe llenar todos los campos")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
                return@setOnClickListener
            }

            grabar(
                ProductModel(
                    id = _id,
                    descripcion = binding.etDescripcion.text.toString(),
                    codigoBarra = binding.etCodigoBarra.text.toString(),
                    precio = binding.etPrecio.text.toString().toDouble()
                )
            )
        }
    }

    private fun obtenerProducto() {
        _id = intent.extras?.getInt("id", 0) ?: 0
        binding.etDescripcion.setText(intent.extras?.getString("descripcion"))
        binding.etCodigoBarra.setText(intent.extras?.getString("codigobarra"))
        binding.etPrecio.setText(intent.extras?.getDouble("precio").toString())
    }

    private fun grabar(producto: ProductModel) = lifecycleScope.launch {
        binding.progressBar.isVisible = true

        makeCall { ProductoDao.grabar(producto) }.let {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    // Reemplazado UtilsMessage por MaterialAlertDialogBuilder
                    MaterialAlertDialogBuilder(this@OperacionProductoActivity)
                        .setTitle("ERROR")
                        .setMessage(it.message)
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                }

                is UiState.Success -> {
                    binding.progressBar.isVisible = false
                    // Reemplazado UtilsMessage por Toast nativo
                    Toast.makeText(this@OperacionProductoActivity, "Datos grabados", Toast.LENGTH_SHORT).show()
                    // Limpiar campos manualmente en lugar de UtilsCommon
                    binding.etDescripcion.setText("")
                    binding.etCodigoBarra.setText("")
                    binding.etPrecio.setText("")
                    binding.etDescripcion.requestFocus()
                    _id = 0
                    MainActivity.existeCambio = true
                }
            }
        }
    }

}