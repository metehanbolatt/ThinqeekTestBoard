package com.metehanbolat.thinqeek.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.metehanbolat.thinqeek.databinding.MoviesRecyclerRowBinding
import com.metehanbolat.thinqeek.model.Movies
import com.metehanbolat.thinqeek.view.fragments.MoviesFragmentDirections
import com.metehanbolat.thinqeek.viewmodel.MoviesFragmentViewModel
import com.squareup.picasso.Picasso

class MoviesRecyclerAdapter(var context: Context, var movieList: ArrayList<Movies>, var viewModel: MoviesFragmentViewModel) :  RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesViewHolder>() {
    class MoviesViewHolder(val binding : MoviesRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = MoviesRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.binding.movieName.text = movieList[position].name
        holder.binding.movieYear.text = movieList[position].year.toString()
        holder.binding.movieRate.text = movieList[position].rate.toString()
        Picasso.get().load(movieList[position].downloadUrl).into(holder.binding.movieImage)

        holder.binding.recyclerRowLinear.setOnClickListener {
            if (viewModel.isClickable.value == true){
                val action = MoviesFragmentDirections.actionMoviesFragmentToDetailsMovieFragment(
                    movieList[position].name,
                    movieList[position].comment,
                    movieList[position].downloadUrl,
                    movieList[position].rate.toString()
                )
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

}