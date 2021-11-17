package com.metehanbolat.thinqeek.view.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.thinqeek.R
import com.metehanbolat.thinqeek.adapter.SliderAdapter
import com.metehanbolat.thinqeek.databinding.FragmentUserSignInBinding
import com.metehanbolat.thinqeek.model.SliderItem
import com.metehanbolat.thinqeek.view.activities.ApplicationActivity
import com.metehanbolat.thinqeek.viewmodel.UserSignInFragmentViewModel
import kotlin.math.abs

class UserSignInFragment : Fragment() {

    private var _binding: FragmentUserSignInBinding? = null
    private val binding get() = _binding!!

    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var viewModel: UserSignInFragmentViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firestore: FirebaseFirestore
    private lateinit var navController: NavController

    private lateinit var viewPager2: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = UserSignInFragmentViewModel()
        auth = Firebase.auth
        firestore = Firebase.firestore
        viewPager2 = binding.viewPagerImageSlider

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({

        },2000)

        val sliderItems: MutableList<SliderItem> = ArrayList()

        firestore.collection("SlideImage").addSnapshotListener { value, error ->
            if (error == null){
                if (value != null){
                    if (!value.isEmpty){
                        val images = value.documents
                        for (image in images){
                            val comingImage = SliderItem(image["image"] as String)
                            sliderItems.add(comingImage)
                        }
                        viewPager2.adapter = SliderAdapter(sliderItems)
                        viewPager2.clipToPadding = false
                        viewPager2.clipChildren = false
                        viewPager2.offscreenPageLimit = 3
                        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                        val compositePageTransformer = CompositePageTransformer()
                        compositePageTransformer.addTransformer(MarginPageTransformer(30))
                        compositePageTransformer.addTransformer { page, position ->
                            val r = 1 - abs(position)
                            page.scaleY = 0.56f + r * 0.25f
                        }
                        viewPager2.setPageTransformer(compositePageTransformer)
                    }
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            if (isLoading){
                invisible()
            }else{
                visible()
            }
        }

        binding.button.setOnClickListener {
            signIn()
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

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SingInFragment", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SingInFragment", "Google sign in failed", e)
                }
            }else{
                Log.w("SingInFragment", exception.toString())
                println("else çöktü")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SingInFragment", "signInWithCredential:success")
                    val intent = Intent(requireContext(),ApplicationActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SingInFragment", "signInWithCredential:failure", task.exception)
                }
            }
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