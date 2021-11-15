package com.metehanbolat.thinqeek.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.thinqeek.R
import com.metehanbolat.thinqeek.adapter.MovieChatAdapter
import com.metehanbolat.thinqeek.databinding.FragmentDetailsMovieBinding
import com.metehanbolat.thinqeek.model.ChatUser
import com.metehanbolat.thinqeek.viewmodel.DetailsMovieFragmentViewModel
import com.squareup.picasso.Picasso

class DetailsMovieFragment : Fragment() {

    private var _binding : FragmentDetailsMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var viewModel : DetailsMovieFragmentViewModel

    private lateinit var chatList : ArrayList<ChatUser>
    private lateinit var chatAdapter : MovieChatAdapter

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

        firestore = Firebase.firestore
        auth = Firebase.auth
        viewModel = DetailsMovieFragmentViewModel()

        chatList = ArrayList()
        chatAdapter = MovieChatAdapter(chatList)

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

        viewModel.listIsNotEmpty.observe(viewLifecycleOwner){
            if (it){
                binding.emptyRecyclerText.visibility = View.INVISIBLE
            }else{
                binding.emptyRecyclerText.visibility = View.VISIBLE
            }
        }

        viewModel.getChat(movieName, firestore, chatList, chatAdapter)
        binding.chatRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.chatRecycler.adapter = chatAdapter

        Picasso.get().load(movieDownloadUrl).into(binding.detailsMovieImageView)
        binding.detailsMovieComment.text = movieComment
        binding.detailsMovieName.text = movieName

        starCount(movieRate.toDouble())

        binding.detailsMovieImageView.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.detailsMovieImageView to "image_big")
            val action = DetailsMovieFragmentDirections.actionDetailsMovieFragmentToBigMovieImageFragment(movieDownloadUrl)
            findNavController().navigate(action,extras)
        }

        binding.addChatButton.setOnClickListener {
            if (binding.addChat.text.toString() == ""){
                Snackbar.make(it, "Lütfen yorumunuzu yazın..", Snackbar.LENGTH_SHORT).show()
            }else{
                addChat(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addChat(view: View){
        val chatMap = hashMapOf<String, Any>()
        chatMap["chat"] = binding.addChat.text.toString()
        chatMap["email"] = auth.currentUser?.email.toString()
        chatMap["date"] = Timestamp.now()
        firestore.collection("Movies").document(movieName).collection("Chat").document(auth.currentUser?.email.toString()).set(chatMap).addOnSuccessListener {
            binding.addChat.setText("")
            Snackbar.make(view, "Yorumun eklendi.", Snackbar.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Snackbar.make(view, "Bir hata meydana geldi", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun starCount(rate : Double){

        if (rate <= 2 && rate > 0){
            if (rate == 2.0){
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
            }else{
                binding.firstStar.setImageResource(R.drawable.ic_half_star)
            }
        }else if (rate <= 4 && rate > 2){
            if (rate == 4.0){
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_orange_star)
            }else{
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_half_star)
            }
        }else if (rate <= 6 && rate > 4){
            if (rate == 6.0){
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_orange_star)
                binding.thirdStar.setImageResource(R.drawable.ic_orange_star)
            }else{
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_orange_star)
                binding.thirdStar.setImageResource(R.drawable.ic_half_star)
            }
        }else if (rate <= 8 && rate > 6){
            if (rate == 8.0){
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_orange_star)
                binding.thirdStar.setImageResource(R.drawable.ic_orange_star)
                binding.fourthStar.setImageResource(R.drawable.ic_orange_star)
            }else{
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_orange_star)
                binding.thirdStar.setImageResource(R.drawable.ic_orange_star)
                binding.fourthStar.setImageResource(R.drawable.ic_half_star)
            }
        }else if (rate <= 10 && rate > 8){
            if (rate == 10.0){
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_orange_star)
                binding.thirdStar.setImageResource(R.drawable.ic_orange_star)
                binding.fourthStar.setImageResource(R.drawable.ic_orange_star)
                binding.fifthStar.setImageResource(R.drawable.ic_orange_star)
            }else{
                binding.firstStar.setImageResource(R.drawable.ic_orange_star)
                binding.secondStar.setImageResource(R.drawable.ic_orange_star)
                binding.thirdStar.setImageResource(R.drawable.ic_orange_star)
                binding.fourthStar.setImageResource(R.drawable.ic_orange_star)
                binding.fifthStar.setImageResource(R.drawable.ic_half_star)
            }
        }
    }

}