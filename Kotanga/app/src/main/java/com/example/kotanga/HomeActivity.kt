package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotanga.databinding.ActivityHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    companion object {
        const val TAG = "HomeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database
        val userId = Firebase.auth.currentUser?.uid
        val userRef = database.getReference("users/$userId/name")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.getValue(String::class.java)
                val firstName = name?.substringBefore(" ")
                Log.d(TAG, "User name is: $name")

                binding.tvName.text = "Bienvenue : $firstName"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        binding.accountbutton.setOnClickListener{
            startActivity(Intent(this, Compte_Activity::class.java))
        }
    }
}
