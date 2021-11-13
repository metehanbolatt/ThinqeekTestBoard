package com.metehanbolat.thinqeek.viewmodel

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.metehanbolat.thinqeek.view.activities.ApplicationActivity

class UserSignInFragmentViewModel : ViewModel() {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun signInUser(view: View, email: String, password: String, auth: FirebaseAuth, context: Context, activity: FragmentActivity){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val intent = Intent(context, ApplicationActivity::class.java)
                context.startActivity(intent)
                activity.finish()
            }else{
                Snackbar.make(view, "Bir hata meydana geldi", Snackbar.LENGTH_SHORT).show()
                isLoading.value = false
            }
        }
    }

}