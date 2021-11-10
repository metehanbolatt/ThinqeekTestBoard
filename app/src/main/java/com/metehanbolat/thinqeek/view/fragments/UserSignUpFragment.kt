package com.metehanbolat.thinqeek.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.metehanbolat.thinqeek.databinding.FragmentUserSignUpBinding
import com.metehanbolat.thinqeek.viewmodel.UserSignUpFragmentViewModel
import javax.inject.Inject

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
        binding.signUpButton.setOnClickListener {
            viewModel.signUpUser(binding.emailText.text.toString(), binding.passwordText.text.toString(), auth, requireContext(), requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}