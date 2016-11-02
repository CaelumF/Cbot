package com.gmail.caelum119.Cbot2.message_structure

import com.gmail.caelum119.Cbot2.CBot
import java.util.*

/**
 * First created 9/28/2016 in Cbot
 */
abstract class Context(val domain: String, val server: String, val channel: String): CBot {
    val chatHistory = ArrayList<Message>()
    override val onMessageOutCallbacks = ArrayList<(String) -> Unit>()
    override val onMessageCallbacks = ArrayList<(Message) -> Unit>()
    override val onJoinCallbacks = ArrayList<(String, String, String, String) -> Unit>()

    init {
        Context.allContexts[Triple(domain, server, channel)] = this
    }

    companion object {
        val allContexts = HashMap<Triple<String, String, String>, Context>()
    }
}