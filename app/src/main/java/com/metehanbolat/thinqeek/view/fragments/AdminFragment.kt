package com.metehanbolat.thinqeek.view.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.metehanbolat.thinqeek.R
import com.metehanbolat.thinqeek.databinding.FragmentAdminBinding
import com.metehanbolat.thinqeek.model.Movies
import com.metehanbolat.thinqeek.viewmodel.AdminFragmentViewModel
import java.util.*
import kotlin.collections.ArrayList

class AdminFragment : Fragment() {

    private var _binding : FragmentAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AdminFragmentViewModel

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedPicture : Uri? = null

    private lateinit var navController: NavController

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var movieList: ArrayList<Movies>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        viewModel = AdminFragmentViewModel()

        movieList = ArrayList()

        registerLauncher(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner){
            if (it){
                visibleLottie()
            }else{
                invisibleLottie()
            }
        }

        binding.movieImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view,"Galeriye erişmek için izin gerekli!", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver"){
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
                }else{
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }else{
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }

        binding.addButton.setOnClickListener { buttonView ->
            if (binding.movieRate.text.toString() == "" || binding.movieComment.text.toString() == "" || binding.movieName.text.toString() == "" || binding.movieYear.text.toString() == ""){
                Snackbar.make(buttonView, "Boş alan bırakmayın...", Snackbar.LENGTH_SHORT).show()
            }else{
                if (binding.movieRate.text.toString().toDouble() <= 0 || binding.movieRate.text.toString().toDouble() > 10){
                    Snackbar.make(buttonView, "Lütfen 0 ile 10 arasında bir puanlama yapınız.", Snackbar.LENGTH_SHORT).show()
                }else{
                    val uuid = UUID.randomUUID()
                    val imageName = "$uuid.jpg"
                    val reference = storage.reference
                    val imageReference = reference.child("images").child(imageName)

                    if (selectedPicture != null){
                        viewModel.isLoading.value = true
                        imageReference.putFile(selectedPicture!!).addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                val uploadPictureReference = storage.reference.child("images").child(imageName)
                                uploadPictureReference.downloadUrl.addOnSuccessListener {
                                    val downloadUrl = it.toString()
                                    val movieMap = hashMapOf<String, Any>()
                                    movieMap["name"] = binding.movieName.text.toString()
                                    movieMap["year"] = binding.movieYear.text.toString().toInt()
                                    movieMap["comment"] = binding.movieComment.text.toString()
                                    movieMap["rate"] = binding.movieRate.text.toString().toDouble()
                                    movieMap["downloadUrl"] = downloadUrl
                                    movieMap["date"] = Timestamp.now()

                                    firestore.collection("Movies").document(binding.movieName.text.toString()).set(movieMap).addOnSuccessListener {
                                        navController = findNavController()
                                        navController.navigate(R.id.action_adminFragment_to_moviesFragment)
                                        viewModel.isLoading.value = false
                                    }.addOnFailureListener { e ->
                                        Snackbar.make(buttonView, "Bir hata meydana geldi.", Snackbar.LENGTH_SHORT).show()
                                        viewModel.isLoading.value = false
                                    }

                                }
                            }
                        }.addOnFailureListener {
                            Snackbar.make(buttonView, "Bir hata meydana geldi.", Snackbar.LENGTH_SHORT).show()
                            viewModel.isLoading.value = false
                        }
                    }else{
                        Snackbar.make(buttonView,"Lütfen resim seçiniz",Snackbar.LENGTH_SHORT).show()
                        viewModel.isLoading.value = false
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun registerLauncher(view: View){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let {
                        binding.movieImage.setImageURI(it)
                    }
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if (result){
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                Snackbar.make(view, "İzin gerekli!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun visibleLottie(){
        binding.movieImage.visibility = View.INVISIBLE
        binding.movieName.visibility = View.INVISIBLE
        binding.movieComment.visibility = View.INVISIBLE
        binding.movieRate.visibility = View.INVISIBLE
        binding.movieYear.visibility = View.INVISIBLE
        binding.addButton.visibility = View.INVISIBLE
        binding.lottieWait.visibility = View.VISIBLE

    }

    private fun invisibleLottie(){
        binding.movieImage.visibility = View.VISIBLE
        binding.movieName.visibility = View.VISIBLE
        binding.movieComment.visibility = View.VISIBLE
        binding.movieRate.visibility = View.VISIBLE
        binding.movieYear.visibility = View.VISIBLE
        binding.addButton.visibility = View.VISIBLE
        binding.lottieWait.visibility = View.INVISIBLE
    }

}