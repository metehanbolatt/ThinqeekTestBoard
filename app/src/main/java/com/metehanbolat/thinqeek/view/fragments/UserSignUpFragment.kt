package com.metehanbolat.thinqeek.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.metehanbolat.thinqeek.databinding.FragmentUserSignUpBinding
import com.metehanbolat.thinqeek.viewmodel.UserSignUpFragmentViewModel

class UserSignUpFragment : Fragment() {

    private var _binding: FragmentUserSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UserSignUpFragmentViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSignUpBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = UserSignUpFragmentViewModel()
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

        binding.signUpButton.setOnClickListener {
            if (binding.emailText.text.toString() == "" || binding.passwordText.text.toString() == ""){
                Snackbar.make(it,"Lütfen boş alan bırakmayınız..", Snackbar.LENGTH_SHORT).show()
            }else{
                viewModel.isLoading.value = true
                viewModel.signUpUser(it, binding.emailText.text.toString(), binding.passwordText.text.toString(), auth, requireContext(), requireActivity())
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
        binding.signUpButton.visibility = View.VISIBLE
    }

    private fun invisible(){
        binding.emailText.visibility = View.INVISIBLE
        binding.passwordText.visibility = View.INVISIBLE
        binding.lottieWait.visibility = View.VISIBLE
        binding.signUpButton.visibility = View.INVISIBLE
    }

}