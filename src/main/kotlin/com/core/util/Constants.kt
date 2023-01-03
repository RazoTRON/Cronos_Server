package com.core.util

object Constants {
    const val DATABASE_NAME = "bs"
    val REGISTER = System.getenv("REG").toBoolean()

    const val ACCESS_TOKEN_SEC = 100
    const val REFRESH_TOKEN_SEC = 1500

    fun getInfo() {
        println("""
            Register state: $REGISTER
            Database: $DATABASE_NAME
            Access token (sec): $ACCESS_TOKEN_SEC
            Refresh token (sec): $REFRESH_TOKEN_SEC
        """.trimIndent())
    }
}

