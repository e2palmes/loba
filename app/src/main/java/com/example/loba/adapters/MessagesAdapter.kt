package com.example.loba.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.loba.R
import com.example.loba.models.Message
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessagesAdapter(private val context: Context, private val messages: MutableList<Message>) : BaseAdapter() {

    override fun getCount(): Int {
        return messages.size
    }

    override fun getItem(position: Int): Any {
        return messages[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = messages[position]

        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.bind(message)

        return view
    }

    private class ViewHolder(view: View) {
        private val senderNameTextView: TextView = view.findViewById(R.id.tvSender)
        private val messageTextView: TextView = view.findViewById(R.id.tvMessage)
        private val dateTextView: TextView = view.findViewById(R.id.tvDate)

        fun bind(message: Message) {
            senderNameTextView.text = message.sender
            messageTextView.text = message.message
            dateTextView.text = convertTimestampToDate(message.createdAt)
        }
        fun convertTimestampToDate(timestamp: Long): String {
            val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val date = Date(timestamp)
            return dateFormat.format(date)
        }
    }

}
