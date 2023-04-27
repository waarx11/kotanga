package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import com.example.kotanga.databinding.ActivityCompteBinding
import com.google.firebase.auth.FirebaseAuth
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
        val moyenPaiement = findViewById<LinearLayout>(R.id.linear_Paiement)
        val notifGroup = findViewById<LinearLayout>(R.id.linear_Notif)



        val database = Firebase.database
        val userId = Firebase.auth.currentUser?.uid
        val userRef = database.getReference("users/$userId/name")
        currentUser = database.getReference("users/$userId")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.getValue(String::class.java)
                val firstName = name?.substringBefore(" ")
                val lastName = name?.substringAfter(" ")
                val nomVal = lastName?.substringBefore(" ")
                val EMail = name?.substringAfterLast(" ")

                binding.nameCompte.text = "Nom : $nomVal"
                binding.prenomCompte.text = "\nPrénom : $firstName"
                binding.emailCompte.text = "\nMail : $EMail"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(HomeActivity.TAG, "Failed to read value.", error.toException())
            }
        })

        binding.homebutton.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.settingbutton.setOnClickListener{
            startActivity(Intent(this, ParametersActivity::class.java))
        }

        binding.infoButton.setOnClickListener{
            moyenPaiement.visibility = LinearLayout.INVISIBLE
            notifGroup.visibility = LinearLayout.INVISIBLE
            mesDonnes.visibility = LinearLayout.VISIBLE
        }

        binding.paymentButton.setOnClickListener{
            mesDonnes.visibility = LinearLayout.INVISIBLE
            notifGroup.visibility = LinearLayout.INVISIBLE
            moyenPaiement.visibility = LinearLayout.VISIBLE
        }

        binding.notifButton.setOnClickListener{
            mesDonnes.visibility = LinearLayout.INVISIBLE
            moyenPaiement.visibility = LinearLayout.INVISIBLE
            notifGroup.visibility = LinearLayout.VISIBLE
        }

        binding.middleButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

    }
}