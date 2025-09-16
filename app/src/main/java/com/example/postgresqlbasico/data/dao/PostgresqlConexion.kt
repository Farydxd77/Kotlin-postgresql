package com.example.postgresqlbasico.data.dao
import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties

object PostgresqlConexion {
    fun getConexion(): Connection {
        return DriverManager.getConnection(
            "jdbc:postgresql://10.0.2.2:5432/tienda-emprendedor",
            "postgres",
            ""  // Sin contraseña
        )
    }
}