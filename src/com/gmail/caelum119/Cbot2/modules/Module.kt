package com.gmail.caelum119.Cbot2.modules

/**
 * First created 11/10/2016 in Cbot
 */
abstract class Module {
    abstract val moduleSettings: Module.() -> Unit

    fun command(){

    }
}