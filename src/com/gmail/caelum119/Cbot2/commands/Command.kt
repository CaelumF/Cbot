package com.gmail.caelum119.Cbot2.commands

import com.gmail.caelum119.Cbot2.CBot
import kotlin.reflect.KCallable

/**
 * First created 9/26/2016 in Cbot
 */
class Command(val name: String, val method: KCallable<*>, val botInterface: CBot) {

    init {

    }

    fun getUsage(): String {
        val usage = StringBuilder()
        method.parameters.forEach {
            val bracketType = if(it.isOptional) '[' else '<'
            usage.append("$bracketType${it.name} : ${it.kind}$bracketType")
        }

        return usage.toString()
    }
}
