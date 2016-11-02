package com.gmail.caelum119.Cbot2.events

import com.gmail.caelum119.Cbot2.message_structure.Identity
import com.gmail.caelum119.utils.event.Event

/**
 * First created 10/16/2016 in Cbot
 */
class UserWelcomeEvent(val login: String, val identity: Identity, val channelJoined: String): Event()