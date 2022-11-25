package com.server.security.token

import java.util.*

object TokenBlockList : TimerTask() {

    val blockList = mutableListOf<String>()

    override fun run() {
        blockList.clear()
    }

    val timer = Timer()

    init{
        println("Start timer")
        timer.schedule(this, 0L, 1000 * 10)
    }
}