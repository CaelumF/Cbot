package com.gmail.caelum119.Cbot2.message_structure

import com.gmail.caelum119.Cbot2.IRCCBot
import com.gmail.caelum119.Cbot2.events.UserWelcomeEvent
import com.gmail.caelum119.utils.event.EventHandler
import java.util.*

/**
 * First created 9/28/2016 in Cbot
 */
class Identity(val name: String, val login: String, val hostname: String) {
    val currentNick: String = name
    val online: Boolean = true
    val connectedChannels = HashSet<String>()

    init {

    }

    companion object IdentityFactory {
        //                       Nickname
        val identities = HashMap<String, Identity>()

        init {
            IRCCBot.globalIRCBot.addJoinCallback { channel, sender, login, hostname ->
                //Create new Identity if they're not registered.
                val identity = identities[sender]
                if(identity == null){
                    val newIdentity = Identity(sender, login, hostname)
                    newIdentity.connectedChannels.add(channel)
                    EventHandler.throwEvent(UserWelcomeEvent(login, newIdentity, channel))
                    identities[sender] = newIdentity
                }
            }
        }

        fun getIdentityFromSenderString(sender: String): Identity? {
            return identities[sender]
        }
    }

    fun sendMessagePublicly(message: String) {
        IRCCBot.sendMessage("$currentNick: $message")
    }

    data class JoinEvent(val channel: String?, val sender: String?, val login: String?, val hostname: String?)
}