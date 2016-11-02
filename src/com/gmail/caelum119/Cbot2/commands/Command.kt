package com.gmail.caelum119.Cbot2.commands

import com.gmail.caelum119.Cbot2.BotSettings
import com.gmail.caelum119.Cbot2.CBot
import com.gmail.caelum119.Cbot2.IRCCBot
import com.gmail.caelum119.Cbot2.exceptions.CommandException
import com.gmail.caelum119.Cbot2.exceptions.CommandNotFoundException
import com.gmail.caelum119.Cbot2.exceptions.MalformedCommandCallException
import com.gmail.caelum119.Cbot2.exceptions.TooFewCommandParametersException
import com.gmail.caelum119.Cbot2.message_structure.Message
import com.gmail.caelum119.util.OldStringOperations
import com.sun.javafx.collections.ImmutableObservableList
import java.util.*
import kotlin.reflect.KCallable
import kotlin.reflect.KParameter

/**
 * First created 9/26/2016 in Cbot
 */
class Command(val name: String, val method: KCallable<*>, val botInterface: CBot) {

    init {

    }

    fun getUsage(): String {
        val usage = StringBuilder()
        method.parameters.forEach {
            val bracketType = if(it.isOptional) '[' else '<'
            usage.append("$bracketType${it.name} : ${it.kind}$bracketType")
        }

        return usage.toString()
    }


    companion object {

        private val commandList = ImmutableObservableList<Command>()
        private val commandMap = HashMap<String, Command>()

        init {
            IRCCBot.globalIRCBot.addMessageCallback { message ->
                val sender = message.sender
                if (message.originalMessageString.startsWith(BotSettings.commandPrefix)) {
                    try {
                        callCommand(message)
                    } catch (e: CommandException) {
                        sender.sendMessagePublicly(e.message)
                    }
                }
            }
        }

        fun addCommand(command: Command) {
            commandList.add(command)
            commandMap.put(command.name, command)
        }

        fun callCommand(message: Message) {
            val commandMinusPreix = message.originalMessageString.drop(1)
            val commandName: String = commandMinusPreix.substringBefore(" ", "none found")
            if (commandName === "none found") throw MalformedCommandCallException("Uhh... what?")
            val command = commandMap.getOrElse(commandName, {
                throw CommandNotFoundException("$commandName is not a command.")
            })
            try {
                val commandParameters: Array<Any> = parameterStringToParameters(command, commandMinusPreix.substringAfter(commandName))
            } catch (e: CommandException) {
                throw CommandException("Error: \"${e.message}\". \nCommand usage: ${command.getUsage()}")
            }
            println("test")
        }

        private fun parameterStringToParameters(command: Command, parameterString: String): Array<Any> {
            val parameterStrings: List<String> = parameterString.split(" ")
            val commandParameterTypes: List<KParameter> = command.method.parameters
            if (commandParameterTypes.size > parameterStrings.size) {
                throw TooFewCommandParametersException("You are missing ${commandParameterTypes.size - parameterStrings.size} parameters.")
            }
            val typedParameters = ArrayList<Any>()

            parameterStrings.forEachIndexed { index, string ->
                val associatedTypeName = commandParameterTypes[index].name
                typedParameters.add(OldStringOperations.stringToType(associatedTypeName, string))
            }

            return typedParameters.toTypedArray()
        }
    }
}