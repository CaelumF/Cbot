package com.gmail.caelum119.Cbot2.message_structure

/**
 * First created 9/28/2016 in Cbot
 */
class Message(val sender: Identity, val originalMessageString: String, val context: Context) {

    init {
    }

    companion object MessageFactory {

        fun createMessage(sender: String, messageString: String, context: Context): Message {

            val identity: Identity = Identity.getIdentityFromSenderString(sender) ?: throw Exception("ss")

            val message: Message = Message(identity, messageString, context)
            return message
        }
    }
}