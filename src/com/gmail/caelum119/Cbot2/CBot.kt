package com.gmail.caelum119.Cbot2

import com.gmail.caelum119.Cbot2.message_structure.Message
import java.util.*

/**
 * First created 10/20/2016 in Cbot
 *
 * Interface for communicating to *any*
 */
interface CBot {

    val onMessageOutCallbacks: ArrayList<(message: String) -> Unit>
    val onMessageCallbacks: ArrayList<(message: Message) -> Unit>
    val onJoinCallbacks: ArrayList<(channel: String, sender: String, login: String, hostname: String) -> Unit>

    fun addMessageOutCallback(callback: (String) -> Unit) = onMessageOutCallbacks.add(callback)

    fun addMessageCallback(callback: (Message) -> Unit) = onMessageCallbacks.add(callback)

    fun addJoinCallback(callback: (channel: String, sender: String, login: String, hostname: String) -> Unit) = onJoinCallbacks.add(callback)

    fun sendMessage(message: String){
        onMessageOutCallbacks.forEach { it.invoke(message) }
    }
}