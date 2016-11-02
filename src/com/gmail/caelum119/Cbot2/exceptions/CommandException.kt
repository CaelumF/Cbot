package com.gmail.caelum119.Cbot2.exceptions

/**
 * First created 10/15/2016 in Cbot
 *
 * Thrown when user input results in an exception
 */
open class CommandException(override val message: String): Exception() {

}