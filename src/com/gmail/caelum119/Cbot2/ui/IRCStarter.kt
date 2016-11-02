package com.gmail.caelum119.Cbot2.ui

import com.gmail.caelum119.Cbot2.IRCCBot
import com.gmail.caelum119.Cbot2.message_structure.Identity
import com.gmail.caelum119.Cbot2.modules.Welcomer

/**
 * First created 10/20/2016 in Cbot
 */
class IRCStarter: Runnable{
    override fun run() {
        try {

        }catch (e: Exception){
            e.printStackTrace()
        } finally {
            IRCCBot.disconnect()
        }
    }


    init {

    }
}

fun main(args: Array<String>) {

}