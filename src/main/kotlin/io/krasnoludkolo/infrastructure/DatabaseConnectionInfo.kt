package io.krasnoludkolo.infrastructure

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


internal class DatabaseConnectionInfo {

    private val username: String = System.getenv("DATA_USER")
    private val password: String = System.getenv("DATA_PASSWORD")
    private val url: String = System.getenv("DATA_URL")

    @Throws(SQLException::class)
    fun createConnection(): Connection {
        return DriverManager.getConnection(url, username, password)
    }


}