package com.gmail.caelum119.Cbot2.message_structure

import com.gmail.caelum119.Cbot2.CBot
import com.gmail.caelum119.Cbot2.IRCCBot
import java.util.*

/**
 * First created 10/14/2016 in Cbot
 *
 * For individual channels and servers
 */
class IRCContext internal constructor(server: String, channel: String, val cbot: IRCCBot): Context("IRC", server, channel){

    init {
        cbot.onMessageCallbacks.add {
            if(it.context == this)
            chatHistory.add(it)
        }
    }

    companion object{
        val onNewIRCContext = ArrayList<(IRCContext) -> Unit>()

        fun createIRCContext(server: String, channel: String, cbot: CBot): IRCContext{
            val newContext = IRCContext(server, channel, cbot as IRCCBot)
            onNewIRCContext.forEach { it(newContext) }
            return newContext
        }
    }
}
