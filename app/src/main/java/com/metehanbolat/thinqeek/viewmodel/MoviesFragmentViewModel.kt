package com.metehanbolat.thinqeek.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.metehanbolat.thinqeek.adapter.MoviesRecyclerAdapter
import com.metehanbolat.thinqeek.model.Movies

class MoviesFragmentViewModel : ViewModel() {

    var isClickable = MutableLiveData(false)

    fun getFilm(query: String, firestore: FirebaseFirestore, movieList: ArrayList<Movies>, adapter: MoviesRecyclerAdapter){
        firestore.collection("Movies").orderBy(query, Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error == null){
                if (value != null){
                    if (!value.isEmpty){
                        val movies = value.documents
                        movieList.clear()
                        for (movie in movies){
                            val myMovie = Movies(
                                movie["name"].toString(),
                                movie["year"].toString().toInt(),
                                movie["comment"].toString(),
                                movie["rate"].toString().toDouble(),
                                movie["downloadUrl"].toString(),
                                movie["date"].toString()
                            )
                            movieList.add(myMovie)
                        }
                        adapter.notifyDataSetChanged()
                        isClickable.value = true
                    }
                }
            }
        }
    }
}