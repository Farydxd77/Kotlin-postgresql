package com.example.postgresqlbasico.data.model

data class ProductModel(
    var id: Int = 0,
    var descripcion: String = "",
    var codigoBarra: String = "",
    var precio: Double = 0.0
)