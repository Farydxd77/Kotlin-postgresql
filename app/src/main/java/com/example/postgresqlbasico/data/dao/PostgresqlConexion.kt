package com.example.postgresqlbasico.data.dao
import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties

object PostgresqlConexion {
    fun getConexion(): Connection {
        val url = "jdbc:postgresql://10.0.2.2:5432/tienda-emprendedor"
        val props = Properties()
        props.setProperty("user", "postgres")
        props.setProperty("password", "123456")
        props.setProperty("ssl", "false")
        return DriverManager.getConnection(url, props)
    }
}