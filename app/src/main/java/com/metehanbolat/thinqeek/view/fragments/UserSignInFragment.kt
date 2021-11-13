package com.metehanbolat.thinqeek.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.metehanbolat.thinqeek.R
import com.metehanbolat.thinqeek.databinding.FragmentUserSignInBinding
import com.metehanbolat.thinqeek.viewmodel.UserSignInFragmentViewModel

class UserSignInFragment : Fragment() {

    private var _binding: FragmentUserSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UserSignInFragmentViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = UserSignInFragmentViewModel()
        auth = Firebase.auth

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            if (isLoading){
                invisible()
            }else{
                visible()
            }
        }

        binding.signUpText.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_userSignInFragment_to_userSignUpFragment)
        }

        binding.signInButton.setOnClickListener {
            if (binding.emailText.text.toString() == "" || binding.passwordText.text.toString() == ""){
                Snackbar.make(it, "Lütfen boş alan bırakmayınız..", Snackbar.LENGTH_SHORT).show()
            }else{
                viewModel.isLoading.value = true
                viewModel.signInUser(it, binding.emailText.text.toString(), binding.passwordText.text.toString(), auth, requireContext(), requireActivity())
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun visible(){
        binding.emailText.visibility = View.VISIBLE
        binding.passwordText.visibility = View.VISIBLE
        binding.lottieWait.visibility = View.INVISIBLE
        binding.signInButton.visibility = View.VISIBLE
        binding.areYouNewText.visibility = View.VISIBLE
        binding.signUpText.visibility = View.VISIBLE
    }

    private fun invisible(){
        binding.emailText.visibility = View.INVISIBLE
        binding.passwordText.visibility = View.INVISIBLE
        binding.lottieWait.visibility = View.VISIBLE
        binding.signInButton.visibility = View.INVISIBLE
        binding.areYouNewText.visibility = View.INVISIBLE
        binding.signUpText.visibility = View.INVISIBLE
    }

}