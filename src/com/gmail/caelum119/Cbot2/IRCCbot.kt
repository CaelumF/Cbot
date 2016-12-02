package com.gmail.caelum119.Cbot2

import com.gmail.caelum119.Cbot2.message_structure.IRCContext
import com.gmail.caelum119.Cbot2.message_structure.Identity
import com.gmail.caelum119.Cbot2.message_structure.Message
import com.gmail.caelum119.Cbot2.modules.Welcomer
import org.jibble.pircbot.IrcException
import org.jibble.pircbot.PircBot
import java.util.*

/**
 * First created 9/26/2016 in Cbot
 * For IRC bots.
 * each bot would be connected to several channels and servers
 */
open class IRCCBot(val serverName: String = "irc.freenode.org",
                   val channelName: String = "##CoderdojoBots",
                   val loginName: String = "Unnamed",
                   val loiginPassword: String = "") : PircBot(), Runnable, CBot {

    override val onMessageOutCallbacks: ArrayList<(String) -> Unit> = ArrayList()
    override val onMessageCallbacks = ArrayList<(message: Message) -> Unit>()
    override val onJoinCallbacks = ArrayList<(channel: String, sender: String, login: String, hostname: String) -> Unit>()

    /**                                 Server, channel **/
    val connectedServers = HashMap<Pair<String, String>, IRCContext>()

    val botName = "Cbot"


    init {
        this.name = "xxxadeefg"
    }

    override fun run() {
        Welcomer
        Identity
        connect(serverName)
        joinChannel(channelName)
    }

    override fun sendMessage(message: String) {
        sendMessage(channelName, message)
    }

    override fun onMessage(channel: String?, sender: String?, login: String?, hostname: String?, message: String?) {
        super.onMessage(channel, sender, login, hostname, message)
        val context = connectedServers[Pair(server, channel)]
        if (context != null) {
            onMessageCallbacks.forEach { it.invoke(Message.createMessage(sender ?: "Null", message ?: "", context)) }
        } else {
            /**This must have been connected to a channel not using [connectToChannel] **/

        }
    }

    override fun onJoin(channel: String, sender: String, login: String, hostname: String) {
        super.onJoin(channel, sender, login, hostname)
        onJoinCallbacks.forEach { it.invoke(channel, sender, login, hostname) }
    }

    fun connectToChannel(server: String, channel: String, serverPort: Int = 6667, password: String?) {
        try {
            if (password == null) connect(server, port)
            if (password != null) connect(server, port, password)
        } catch (e: IrcException) {
            throw e
        }
        val newContext = IRCContext(server, channel, this)
        connectedServers.put(Pair(server, channel), newContext)
    }

    /**
     * TODO: Fix this hack
     */
    companion object globalIRCBot : IRCCBot() {
        /**
         * So that future implementations will
         */
    }
}