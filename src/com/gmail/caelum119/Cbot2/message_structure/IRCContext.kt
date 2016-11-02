package com.gmail.caelum119.Cbot2.message_structure

import com.gmail.caelum119.Cbot2.CBot
import com.gmail.caelum119.Cbot2.IRCCBot

/**
 * First created 10/14/2016 in Cbot
 *
 * For individual channels and servers
 */
class IRCContext internal constructor(server: String, channel: String, val cbot: IRCCBot): Context("IRC", server, channel){

    init {
        cbot.onMessageCallbacks.add {
            if(it.context == this)so have
            chatHistory.add(it)
        }
    }

    companion object{
        fun createIRCContext(server: String, channel: String): IRCContext{
            return IRCContext(server, channel)
        }
    }
}
