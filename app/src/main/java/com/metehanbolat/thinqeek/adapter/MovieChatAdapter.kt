package com.metehanbolat.thinqeek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metehanbolat.thinqeek.databinding.ChatRecyclerRowBinding
import com.metehanbolat.thinqeek.model.ChatUser

class MovieChatAdapter(var chatList : ArrayList<ChatUser>) : RecyclerView.Adapter<MovieChatAdapter.ChatViewHolder>() {
    class ChatViewHolder(val binding: ChatRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ChatRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.binding.chatComment.text = chatList[position].comment
        holder.binding.chatEmailText.text = chatList[position].email
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}