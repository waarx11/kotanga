package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import com.example.kotanga.databinding.ActivityCompteBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Compte_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityCompteBinding

    companion object {
        const val TAG = "CompteActivity"
    }
    private lateinit var currentUser: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mesDonnes = findViewById<LinearLayout>(R.id.linear_Donnee)

        val database = Firebase.database
        val userId = Firebase.auth.currentUser?.uid
        val userRef = database.getReference("users/$userId/name")
        currentUser = database.getReference("users/$userId")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.getValue(String::class.java)
                val firstName = name?.substringBefore(" ")
                val lastName = name?.substringAfterLast(" ")

                /*binding.name_compte.text = "Nom : $lastName"
                binding.prenom_compte.text = "Prénom : $firstName"*/
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(HomeActivity.TAG, "Failed to read value.", error.toException())
            }
        })

        binding.homebutton.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        /*binding.info_button.setOnClickListener{
            mesDonnes.visibility = LinearLayout.VISIBLE // Rend l'élément invisible
        }*/

    }
}