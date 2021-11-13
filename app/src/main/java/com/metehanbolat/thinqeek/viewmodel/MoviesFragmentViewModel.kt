package com.metehanbolat.thinqeek.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.metehanbolat.thinqeek.adapter.MoviesRecyclerAdapter
import com.metehanbolat.thinqeek.model.Movies

class MoviesFragmentViewModel : ViewModel() {

    fun getFilm(firestore: FirebaseFirestore, context: Context, movieList: ArrayList<Movies>, adapter: MoviesRecyclerAdapter){
        firestore.collection("Movies").addSnapshotListener { value, error ->
            if (error == null){
                if (value != null){
                    if (!value.isEmpty){
                        val movies = value.documents
                        movieList.clear()
                        for (movie in movies){
                            val myMovie = Movies(
                                movie["name"].toString(),
                                movie["year"].toString(),
                                movie["comment"].toString(),
                                movie["rate"].toString(),
                                movie["downloadUrl"].toString()
                            )
                            movieList.add(myMovie)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}