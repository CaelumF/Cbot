package com.gmail.caelum119.Cbot2.modules

import com.gmail.caelum119.Cbot2.IRCCBot
import com.gmail.caelum119.Cbot2.events.UserWelcomeEvent
import com.gmail.caelum119.utils.event.EventHandler
import com.gmail.caelum119.utils.event.EventSettings

/**
 * First created 10/16/2016 in Cbot
 */
object Welcomer: Module() {
    override val moduleSettings: Module.() -> Unit = {

    }

    init {
        EventHandler.addEventListener(this, this)
    }

    @EventSettings()
    fun onUserJoin(event: UserWelcomeEvent){
        IRCCBot.sendMessage(event.channelJoined, "Welcome ${event.identity.currentNick}")
        println("test")
    }
}