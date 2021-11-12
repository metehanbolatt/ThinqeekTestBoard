package com.metehanbolat.thinqeek.viewmodel

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.metehanbolat.thinqeek.model.Movies

class AdminFragmentViewModel : ViewModel() {

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>

    fun addMovie(movieName: String, movieYear: String, movieComment: String, movieList: ArrayList<Movies>){

    }

}