package com.gmail.caelum119.Cbot2.ui

import com.gmail.caelum119.Cbot2.IRCCBot
import com.gmail.caelum119.utils.FXUtils.FXUtils
import javafx.scene.layout.AnchorPane
import tornadofx.View
import tornadofx.textarea
import tornadofx.textfield

/**
 * First created 10/19/2016 in Cbot
 */
class Console : View() {
    override val root = AnchorPane()

    init {
        with(root) {
            textarea {
                isEditable = false
                FXUtils.anchor(top = 0.0, bottom = 20.0, left = 0.0, right = 0.0, node = this)

                IRCCBot.globalIRCBot.addMessageOutCallback { this@textarea.text += "\n Sent: $it" }
                IRCCBot.globalIRCBot.addMessageCallback {
                    val domain = it.context.domain
                    val server = it.context.server
                    val channel = it.context.channel
                    this@textarea.text += "\n $domain:$server:$channel: ${it.sender.name}: ${it.originalMessageString}"
                    this@textarea.scrollTop = this.maxHeight
                }
            }
            textfield {
                maxHeight = 20.0
                FXUtils.anchor(bottom = 0.0, left = 0.0, right = 0.0, node = this)

                setOnAction {
                    IRCCBot.globalIRCBot.sendMessage(this.text)
                    this.clear()
                }
            }
        }
    }

}
