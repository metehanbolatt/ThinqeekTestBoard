package com.metehanbolat.thinqeek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metehanbolat.thinqeek.databinding.MoviesRecyclerRowBinding
import com.metehanbolat.thinqeek.model.Movies

class MoviesRecyclerAdapter(var movieList: ArrayList<Movies>) :  RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesViewHolder>() {
    class MoviesViewHolder(val binding : MoviesRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = MoviesRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.binding.movieName.text = movieList[position].name
        holder.binding.movieYear.text = movieList[position].year
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

}