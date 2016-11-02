package com.gmail.caelum119.implementations.Skype

import com.samczsun.skype4j.SkypeBuilder
import com.samczsun.skype4j.events.EventHandler
import com.samczsun.skype4j.events.Listener
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent

/**
 * First created 10/4/2016 in Cbot
 */
class SkypeBot{
    init {
        val skype = SkypeBuilder("live:caelum1192", "Surrogateofboxes/1").withAllResources().build()
        skype.login()
        skype.eventDispatcher.registerListener(object : Listener {
            @EventHandler
            fun onMessage(e: MessageReceivedEvent) {
                System.out.println("Got message: " + e.message.content)
            }
        })
        skype.subscribe()

        skype.logout()
    }
}

fun main(args: Array<String>) {
    SkypeBot()
}