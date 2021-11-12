package com.metehanbolat.thinqeek.view.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.metehanbolat.thinqeek.R
import com.metehanbolat.thinqeek.databinding.FragmentBigMovieImageBinding
import com.squareup.picasso.Picasso

class BigMovieImageFragment : Fragment() {

    private var _binding : FragmentBigMovieImageBinding? = null
    private val binding get() = _binding!!

    private var downloadUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBigMovieImageBinding.inflate(inflater, container, false)
        val view = binding.root

        arguments?.let {
            downloadUrl = BigMovieImageFragmentArgs.fromBundle(it).downloadUrl!!
        }

        Picasso.get().load(downloadUrl).into(binding.bigMovieImage)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}