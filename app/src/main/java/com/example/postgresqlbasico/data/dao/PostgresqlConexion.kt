package com.example.postgresqlbasico.data.dao
import java.sql.Connection
import java.sql.DriverManager


object PostgresqlConexion {
    fun getConexion(): Connection {
        return DriverManager.getConnection(
            "jdbc:postgresql://192.168.18.23:5432/DemoDB",
            "postgres",
            "jackelprogramador"
        )
    }
}