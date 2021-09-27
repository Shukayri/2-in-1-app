
package com.example.rvapp

import kotlinx.android.synthetic.main.edit_rv.view.*

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.edit_rv.view.*

class MessageAdapter(private val context: Context, private val messages: ArrayList<String>):
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.edit_rv,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        holder.itemView.apply {
            Message.text = message
            if(message.startsWith("Found")){
                Message.setTextColor(Color.GREEN)
            }else if(message.startsWith("No")||message.startsWith("Wrong")){
                Message.setTextColor(Color.RED)
            }else{
                Message.setTextColor(Color.BLACK)
            }
        }
    }

    override fun getItemCount() = messages.size
}