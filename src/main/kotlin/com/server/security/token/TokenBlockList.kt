package com.server.security.token

import com.core.util.Constants.REFRESH_TOKEN_SEC
import java.util.*

object TokenBlockList : TimerTask() {

    private var oldTokens = listOf<String>()
    private val newTokens = mutableListOf<String>()
    fun TokenBlockList.add(token: String) {
        newTokens.add(token)
    }
    fun TokenBlockList.get() = newTokens + oldTokens

    override fun run() {
        oldTokens = newTokens
        newTokens.clear()
        println("${Date(System.currentTimeMillis())} Blocklist of used tokens is cleared.")
    }

    private val timer = Timer()

    init {
        timer.schedule(this, 0L, 1000L * REFRESH_TOKEN_SEC)
    }
}