package com.metehanbolat.thinqeek.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.thinqeek.R
import com.metehanbolat.thinqeek.adapter.MoviesRecyclerAdapter
import com.metehanbolat.thinqeek.databinding.FragmentMoviesBinding
import com.metehanbolat.thinqeek.model.Movies
import com.metehanbolat.thinqeek.viewmodel.MoviesFragmentViewModel

class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MoviesFragmentViewModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var movieList: ArrayList<Movies>
    private lateinit var movieAdapter: MoviesRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = MoviesFragmentViewModel()
        firestore = Firebase.firestore
        movieList = ArrayList()
        movieAdapter = MoviesRecyclerAdapter(movieList)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFilm(firestore, requireContext(), movieList, movieAdapter)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = movieAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}