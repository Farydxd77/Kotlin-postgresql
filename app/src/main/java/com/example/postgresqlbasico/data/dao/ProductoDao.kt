package com.example.postgresqlbasico.data.dao

import com.example.postgresqlbasico.data.model.ProductModel
import java.sql.Connection
import java.sql.PreparedStatement

object ProductoDao {

    fun listar(dato: String): List<ProductModel> {
        var lista = mutableListOf<ProductModel>()

        PostgresqlConexion.getConexion().prepareStatement(
            "Select id, descripcion, codigobarra, precio FROM producto WHERE LOWER(descripcion) LIKE '%' || LOWER(?) || '%';"
        ).use { ps ->
            ps.setString(1, dato)
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    lista.add(
                        ProductModel(
                            rs.getInt("id"),
                            rs.getString("descripcion"),
                            rs.getString("codigobarra"),
                            rs.getDouble("precio")
                        )
                    )
                }
            }
        }

        return lista
    }

    private fun registrar(producto: ProductModel) {
        PostgresqlConexion.getConexion().prepareStatement(
            "INSERT INTO producto (descripcion, codigobarra, precio) VALUES (?, ?, ?);"
        ).use { ps ->

            ps.setString(1, producto.descripcion)
            ps.setString(2, producto.codigoBarra)
            ps.setDouble(3, producto.precio)
            ps.executeUpdate()
        }
    }

    private fun actualizar(producto: ProductModel) {
        PostgresqlConexion.getConexion().prepareStatement(
            "UPDATE producto SET descripcion = ?, codigobarra = ?, precio = ? WHERE id = ?;"
        ).use { ps ->

            ps.setString(1, producto.descripcion)
            ps.setString(2, producto.codigoBarra)
            ps.setDouble(3, producto.precio)
            ps.setInt(4, producto.id)
            ps.executeUpdate()
        }
    }

    fun eliminar(producto: ProductModel) {
        PostgresqlConexion.getConexion().prepareStatement(
            "DELETE FROM producto WHERE id = ?;"
        ).use { ps ->
            ps.setInt(1, producto.id)
            ps.executeUpdate()
        }
    }

    fun grabar(producto: ProductModel) {
        if (producto.id == 0) {
            registrar(producto)
        } else {
            actualizar(producto)
        }
    }
}