package com.metehanbolat.thinqeek.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.metehanbolat.thinqeek.adapter.MoviesRecyclerAdapter
import com.metehanbolat.thinqeek.model.Movies

class MoviesFragmentViewModel : ViewModel() {

    var isClickable = MutableLiveData(false)

    fun getFilm(firestore: FirebaseFirestore, movieList: ArrayList<Movies>, adapter: MoviesRecyclerAdapter){
        firestore.collection("Movies").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
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
                                movie["downloadUrl"].toString(),
                                movie["date"].toString()
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