package com.github.nkzawa.socketio.androidchat

data class Message(
        var type: Int = 0,
        var message: String? = null,
        var username: String? = null
) {

    companion object {
        const val TYPE_MESSAGE = 0
        const val TYPE_LOG = 1
        const val TYPE_ACTION = 2
    }
}