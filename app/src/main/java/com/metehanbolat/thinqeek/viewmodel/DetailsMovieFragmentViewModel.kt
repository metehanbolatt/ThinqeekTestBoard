package com.metehanbolat.thinqeek.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.metehanbolat.thinqeek.adapter.MovieChatAdapter
import com.metehanbolat.thinqeek.model.ChatUser

class DetailsMovieFragmentViewModel : ViewModel() {

    val listIsNotEmpty = MutableLiveData(false)

    fun getChat(movieName: String, firestore: FirebaseFirestore, chatList: ArrayList<ChatUser>, adapter: MovieChatAdapter){
        firestore.collection("Movies").document(movieName).collection("Chat").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error == null){
                if (!value?.isEmpty!!){
                    val chats = value.documents
                    chatList.clear()
                    listIsNotEmpty.value = true
                    for (chat in chats){
                        val myChat = ChatUser(
                            chat["email"].toString(),
                            chat["chat"].toString(),
                            chat["date"].toString()
                        )
                        chatList.add(myChat)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }

    }

}