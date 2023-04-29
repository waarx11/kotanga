package com.example.kotanga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import com.example.kotanga.databinding.ActivityModificationCompteBinding
import com.example.kotanga.databinding.ActivityParametersBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class modificationCompte_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityModificationCompteBinding
    private lateinit var currentUser: DatabaseReference

    private lateinit var tvEmail: EditText

    private lateinit var tvRIB: EditText

    private lateinit var tvMastercard: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificationCompteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvEmail = findViewById(R.id.mail_textfield)

        tvRIB = findViewById(R.id.rib_textfield)

        tvMastercard = findViewById(R.id.carteBanque_textfield)

        val database = Firebase.database
        val userId = Firebase.auth.currentUser?.uid
        currentUser = database.getReference("users/$userId")
        val userRef = database.getReference("users/$userId/name")
        val informations = database.getReference("groupes/$userId/informations")


        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.getValue(String::class.java)
                val eMail = name?.substringAfterLast(" ")

                binding.mailTextfield.text = Editable.Factory.getInstance().newEditable(eMail)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(HomeActivity.TAG, "Failed to read value.", error.toException())
            }
        })

        informations.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val info = dataSnapshot.getValue(Info::class.java)
                val mastercard: String = info?.mastercard.toString()
                val rib: String = info?.rib.toString()

                if (info != null) {
                    binding.carteBanqueTextfield.text = Editable.Factory.getInstance().newEditable(mastercard)
                    binding.ribTextfield.text = Editable.Factory.getInstance().newEditable(rib)
                } else {
                    binding.carteBanqueTextfield.text = Editable.Factory.getInstance().newEditable("1234 5678 9012 3456")
                    binding.ribTextfield.text = Editable.Factory.getInstance().newEditable("FR~")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(HomeActivity.TAG, "Failed to read value.", error.toException())
            }
        })

        binding.btnValide.setOnClickListener{
            val mastercard = tvMastercard.text.toString().trim()
            val rib = tvRIB.text.toString().trim()
            val info = Info(mastercard, rib)
            informations.setValue(info)
            finish()
        }
    }


    data class Info(
        val mastercard: String = "",
        val rib: String = ""
    )
}