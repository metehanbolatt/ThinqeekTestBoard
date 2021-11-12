package com.metehanbolat.thinqeek.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.metehanbolat.thinqeek.R
import com.metehanbolat.thinqeek.databinding.FragmentDetailsMovieBinding
import com.squareup.picasso.Picasso

class DetailsMovieFragment : Fragment() {

    private var _binding : FragmentDetailsMovieBinding? = null
    private val binding get() = _binding!!

    private var movieName = ""
    private var movieComment = ""
    private var movieDownloadUrl = ""
    private var movieRate = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsMovieBinding.inflate(inflater, container, false)
        val view = binding.root

        arguments?.let {
            movieName = DetailsMovieFragmentArgs.fromBundle(it).name!!
            movieComment = DetailsMovieFragmentArgs.fromBundle(it).comment!!
            movieDownloadUrl = DetailsMovieFragmentArgs.fromBundle(it).downloadUrl!!
            movieRate = DetailsMovieFragmentArgs.fromBundle(it).rate!!
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get().load(movieDownloadUrl).into(binding.detailsMovieImageView)
        binding.detailsMovieComment.text = movieComment
        binding.detailsMovieName.text = movieName

        when(movieRate.toInt()){
            1 -> {
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
            }
            2 -> {
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_orange_star)
            }
            3 -> {
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_orange_star)
                binding.thirdStar.setImageResource(R.drawable.ic_orange_star)

            }
            4 -> {
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_orange_star)
                binding.thirdStar.setImageResource(R.drawable.ic_orange_star)
                binding.fourthStar.setImageResource(R.drawable.ic_orange_star)
            }
            5 -> {
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_orange_star)
                binding.thirdStar.setImageResource(R.drawable.ic_orange_star)
                binding.fourthStar.setImageResource(R.drawable.ic_orange_star)
                binding.fifthStar.setImageResource(R.drawable.ic_orange_star)
            }
        }

        binding.detailsMovieImageView.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.detailsMovieImageView to "image_big")
            val action = DetailsMovieFragmentDirections.actionDetailsMovieFragmentToBigMovieImageFragment(movieDownloadUrl)
            findNavController().navigate(action,extras)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}