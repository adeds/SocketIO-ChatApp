package com.github.nkzawa.socketio.androidchat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*

class MessageAdapter(context: Context,
                     private val mMessages: MutableList<Message>) :
        RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private val mUsernameColors: IntArray = context.resources.getIntArray(R.array.username_colors)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout =
                when (viewType) {
                    Message.TYPE_MESSAGE -> R.layout.item_message
                    Message.TYPE_ACTION -> R.layout.item_action
                    else -> R.layout.item_log
                }

        val v = LayoutInflater
                .from(parent.context)
                .inflate(layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val message = mMessages[position]
        Log.e("mess$position:", message.toString())
        message.message?.let { viewHolder.setMessage(it) }
        message.username?.let { viewHolder.setUsername(it) }
    }

    override fun getItemCount(): Int {
        return mMessages.size
    }

    override fun getItemViewType(position: Int): Int {
        return mMessages[position].type
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setUsername(username: String) {
            itemView.username_sender.apply {
                text = username
                setTextColor(getUsernameColor(username))
            }
        }

        fun setMessage(message: String) {
            itemView.message.text = message
        }

        private fun getUsernameColor(username: String?): Int {
            var hash = 7
            var i = 0
            val len = username!!.length
            while (i < len) {
                hash = username.codePointAt(i) + (hash shl 5) - hash
                i++
            }
            val index = Math.abs(hash % mUsernameColors.size)
            return mUsernameColors[index]
        }


    }

}