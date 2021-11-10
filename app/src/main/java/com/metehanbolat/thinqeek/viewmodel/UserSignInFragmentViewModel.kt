package com.metehanbolat.thinqeek.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.metehanbolat.thinqeek.view.activities.ApplicationActivity
import com.metehanbolat.thinqeek.view.activities.MainActivity
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

class UserSignInFragmentViewModel : ViewModel() {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun signInUser(email: String, password: String, auth: FirebaseAuth, context: Context, activity: FragmentActivity){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val intent = Intent(context, ApplicationActivity::class.java)
                context.startActivity(intent)
                activity.finish()
            }else{
                Toast.makeText(context, "Bir hata meydana geldi", Toast.LENGTH_SHORT).show()
                isLoading.value = false
            }
        }
    }

}